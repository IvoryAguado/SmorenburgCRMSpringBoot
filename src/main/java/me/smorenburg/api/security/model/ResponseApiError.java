package me.smorenburg.api.security.model;

/**
 * Created by MR x on 18/02/2017.
 */
public class ResponseApiError {

    private Long timestamp;
    private Integer status;
    private String error;
    private String exception;
    private String message;
    private String path;

    public ResponseApiError(Exception exception, String path) {
        this.timestamp = System.currentTimeMillis();
        this.status = 400;
        this.error = "Bad Request";
        this.exception = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.path = path;
    }

    public ResponseApiError(Exception exception, String message, String path) {
        this.timestamp = System.currentTimeMillis();
        this.status = 400;
        this.error = "Bad Request";
        this.exception = exception.getClass().getSimpleName();
        this.message = message;
        this.path = path;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
