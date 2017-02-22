package me.smorenburg.api.rest.media;

import me.smorenburg.api.rest.media.storage.StorageProperties;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;

/**
 * Created by stephan on 11.03.16.
 */

@Entity
@Table(name = "MEDIAS")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FILENAME", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String fileName;

    @Column(name = "TITLE", length = 50)
    @Size(min = 4, max = 50)
    private String title;

    @Column(name = "URL", length = 100)
    @Size(max = 100)
    private String url;

    @Column(name = "LOCAL_PATH", length = 100, unique = true)
    @NotNull
    @Size(max = 100)
    private String localPath;

    @Column(name = "DESCRIPTION", length = 100)
    @Size(max = 100)
    private String description;

    @Column(name = "MIME_TYPE", length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    private String mimeType;

    @Column(name = "FILE_EXTENSION", length = 10)
    @NotNull
    @Size(min = 1, max = 10)
    private String fileExtension;

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date createAt;

    public Media() {
    }

    public Media(MultipartFile file) {
        proccessMultiPartFile(file);
    }

    public Media(MultipartFile file, String url) {
        proccessMultiPartFile(file);
        this.url = url + "/download/" + getFileName();
    }

    private void proccessMultiPartFile(MultipartFile file) {
        mimeType = file.getContentType();

        String extension = file.getOriginalFilename();
        fileExtension = ".unknown";
        if (extension.contains(".")) {
            fileExtension = extension.substring(extension.lastIndexOf("."), extension.length());
        }

        createAt = new Date(System.currentTimeMillis());
        localPath = Paths.get(new StorageProperties().getLocation(), file.getOriginalFilename()).toUri().getPath();
//        url=Paths.get(new StorageProperties().getLocation(),file.getOriginalFilename()).toUri();
        fileName = nextFileStringIdId();
        title = file.getOriginalFilename();
        boolean b = new File(localPath).renameTo(new File(Paths.get(new StorageProperties().getLocation(), fileName
                        + fileExtension
                ).toUri().getPath())
        );
        if (b) {
            localPath = Paths.get(new StorageProperties().getLocation(), fileName
                    + fileExtension
            ).toUri().getPath();
        }
    }

    private String nextFileStringIdId() {
        return new BigInteger(130, new Random(System.currentTimeMillis())).toString(32);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
