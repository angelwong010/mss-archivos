package gob.mx.imss.msscabcs.msscabcsarchivos.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleFileNotFoundException(final FileNotFoundException e, final HttpServletRequest request){
        final ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage(),
                request.getServletPath());
        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGeneralException(final Exception e, final HttpServletRequest request){
        final ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),
                request.getServletPath());
        return error;
    }
}
