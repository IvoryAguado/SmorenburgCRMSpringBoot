package me.smorenburg.api.rest.media;

import me.smorenburg.api.rest.media.storage.StorageFileNotFoundException;
import me.smorenburg.api.rest.media.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("${jwt.routes.apiendpoint}")
public class MediaRestService {

    private final StorageService storageService;
    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    public MediaRestService(StorageService storageService) {
        this.storageService = storageService;
    }

//    @GetMapping("/")
//    public String listUploadedFiles(Model model) throws IOException {
//
//        model.addAttribute("files", storageService
//                .loadAll()
//                .map(path ->
//                        MvcUriComponentsBuilder
//                                .fromMethodName(MediaRestService.class, "serveFile", path.getFileName().toString())
//                                .build().toString())
//                .collect(Collectors.toList()));
//
//        return "uploadForm";
//    }

    //    @GetMapping("/files/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//
//        Resource file = storageService.loadAsResource(filename);
//        return ResponseEntity
//                .ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
//                .body(file);
//    }
    @GetMapping("/media")
    public ResponseEntity<List<Media>> getAllMedia() {
        return ResponseEntity.ok(mediaRepository.findAll());
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Media> getMedia(@PathVariable("id") Long id) throws Exception {

        return ResponseEntity.ok(mediaRepository.findOne(id));
    }

    @DeleteMapping("/media/{id}")
    public ResponseEntity<Media> deleteMedia(@PathVariable("id") Long id) throws Exception {

        Media one = mediaRepository.findOne(id);
        if (one == null)
            throw new ResourceNotFoundException("No Media were found when deleting with id: " + id);

        boolean delete = new File(one.getLocalPath()).delete();
        if (delete)
            mediaRepository.delete(one);

        return ResponseEntity.ok(one);
    }

    @GetMapping("/media/download/{fileName}")
    public void downloadMedia(@PathVariable("fileName") String fileName, HttpServletResponse response) throws Exception {

        Media one = mediaRepository.findByFileName(fileName);
        if (one == null)
            throw new ResourceNotFoundException("No Media were found when downloading with name: " + fileName);

        try {
            response.setContentType(one.getMimeType());
            // get your file as InputStream
            InputStream is = new BufferedInputStream(new FileInputStream(new File(one.getLocalPath())));
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
//                logger.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PostMapping("/media")
    public ResponseEntity<Media> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {

        storageService.store(file);

        return ResponseEntity.ok(mediaRepository.save(new Media(file, httpServletRequest.getRequestURL().toString())));
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}