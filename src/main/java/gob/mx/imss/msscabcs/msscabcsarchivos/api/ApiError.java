package gob.mx.imss.msscabcs.msscabcsarchivos.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiError {

    private final Date timestamp;
    private int status;
    private String message;
    private String url;
    private Map<String, String> validationErrors;

    {
        timestamp = new Date();
    }

    public ApiError(final int status, final String message, final String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(final Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}