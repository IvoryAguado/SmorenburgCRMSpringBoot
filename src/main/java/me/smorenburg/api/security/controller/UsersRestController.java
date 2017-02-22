package me.smorenburg.api.security.controller;


import me.smorenburg.api.security.model.ResponseApiError;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UsersRestController {


    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseApiError indesx(HttpServletRequest request) {
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "beans/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseApiError getAutshentixcatfedUser(HttpServletRequest request) throws ResourceNotFoundException {
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "beans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseApiError getAutheantivcatedUser(HttpServletRequest request) {
        throw new ResourceNotFoundException();
    }


    @RequestMapping(value = "beans/**", produces = MediaType.TEXT_HTML_VALUE)
    public HttpServletRequest getAuthentgixcatedUser(HttpServletRequest request) {
        request.setAttribute("status", 404);
        request.setAttribute("type", "Not Found");
        return request;
    }

    @RequestMapping(value = "beans", produces = MediaType.TEXT_HTML_VALUE)
    public HttpServletRequest getAutshentasdicatedUser(HttpServletRequest request) {
        request.setAttribute("status", 404);
        request.setAttribute("type", "Not Found");
        return request;
    }

    @RequestMapping(value = "profile/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseApiError getAutshaentixcatfedUser(HttpServletRequest request) throws ResourceNotFoundException {
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseApiError getAutheasntivcatedUser(HttpServletRequest request) {
        throw new ResourceNotFoundException();
    }


    @RequestMapping(value = "profile/**", produces = MediaType.TEXT_HTML_VALUE)
    public HttpServletRequest getAuthsaentgixcatedUser(HttpServletRequest request) {
        request.setAttribute("status", 404);
        request.setAttribute("type", "Not Found");
        return request;
    }

    @RequestMapping(value = "profile", produces = MediaType.TEXT_HTML_VALUE)
    public HttpServletRequest getAutshenticatedUser(HttpServletRequest request) {
        request.setAttribute("status", 404);
        request.setAttribute("type", "Not Found");
        return request;
    }

    @RequestMapping(value = "users/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseApiError getAutshentixcatedUser(HttpServletRequest request) throws ResourceNotFoundException {
        throw new ResourceNotFoundException();
    }

    @RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseApiError getAutheanticatedUser(HttpServletRequest request) throws ResourceNotFoundException {
        throw new ResourceNotFoundException();
    }


    @RequestMapping(value = "users/**", produces = MediaType.TEXT_HTML_VALUE)
    public HttpServletRequest getAuthentixcatedUser(HttpServletRequest request) {
        request.setAttribute("status", 404);
        request.setAttribute("type", "Not Found");
        return request;
    }

    @RequestMapping(value = "users", produces = MediaType.TEXT_HTML_VALUE)
    public HttpServletRequest getAuthenticatedUser(HttpServletRequest request) {
        request.setAttribute("status", 404);
        request.setAttribute("type", "Not Found");
        return request;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseApiError> httpMediaTypeNotAcceptableException(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ResponseApiError(e, request.getServletPath()));
    }

}
