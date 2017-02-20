package me.smorenburg.web.config;

import me.smorenburg.api.security.model.ResponseApiError;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
class GlobalControllerExceptionHandlerextends {


    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<ResponseApiError> orghibernateexceptionConstraintViolationException(HttpServletRequest request, org.hibernate.exception.ConstraintViolationException e) {
        String localizedMessage = e.getSQLException().getMessage();
        localizedMessage = localizedMessage.substring(localizedMessage.lastIndexOf(":") + 2, localizedMessage.length()).replaceAll("\\(", "").replace(")", "");
        return ResponseEntity.badRequest().body(new ResponseApiError(e, localizedMessage, request.getServletPath()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseApiError> ConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        StringBuilder localizedMessage = new StringBuilder();
        for (ConstraintViolation<?> next : e.getConstraintViolations()) {
            String propertyPath = next.getPropertyPath().toString().substring(0, 1).toUpperCase() + next.getPropertyPath().toString().substring(1);
            localizedMessage.append(propertyPath)
                    .append(": ")
                    .append(next.getMessage())
                    .append("\n ");
        }
        return ResponseEntity.badRequest().body(new ResponseApiError(e, localizedMessage.toString(), request.getServletPath()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseApiError> ResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseApiError(e, request.getServletPath()));
    }



//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
//    }

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
//        List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
//        String error;
//        for (FieldError fieldError : fieldErrors) {
//            error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
//            errors.add(error);
//        }
//        for (ObjectError objectError : globalErrors) {
//            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
//            errors.add(error);
//        }
//         return ResponseEntity.badRequest().body(request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        String unsupported = "Unsupported method type: " + ex.getMethod();
//        String supported = "Supported method types: " + ex.getSupportedHttpMethods().toString();
////        ErrorMessage errorMessage = new ErrorMessage(status.value(), unsupported, supported);
//        return ResponseEntity.badRequest().body(request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        String unsupported = "Unsupported content type: " + ex.getContentType();
//        String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
////        ResponseApiError errorMessage = new ResponseApiError(status.value(), unsupported, supported);
//        return ResponseEntity.badRequest().body(request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        Throwable mostSpecificCause = ex.getMostSpecificCause();
//        ResponseApiError errorMessage;
//        if (mostSpecificCause != null) {
//            String exceptionName = mostSpecificCause.getClass().getSimpleName();
//            String message = mostSpecificCause.getMessage().substring(0, mostSpecificCause.getLocalizedMessage().indexOf(":"));
////            errorMessage = new ResponseApiError(status.value(), exceptionName, message);
//        } else {
////            errorMessage = new ResponseApiError(ex.getMessage());
//        }
//        return ResponseEntity.badRequest().body(request);
//    }


}