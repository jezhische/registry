package com.tagsoft.registry.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/** Handler for common REST exceptions handling extends {@link ResponseEntityExceptionHandler}.
 * <p>How it works: </p>
 * <p>The {@link ControllerAdvice} annotation allows to consolidate multiple, scattered {@link ExceptionHandler}
 * into a single, global error handling component. </p>
 * The {@link ResponseEntityExceptionHandler} parent class has a facade method
 * {@link #handleException(Exception, WebRequest)}, that returns some method of exception handling depends on
 * actual type of {@code Exception} argument.
 * <p>The array of the possible {@code Exception} types is present in {@code ResponseEntityExceptionHandler} parent class
 * and has {@link ExceptionHandler} annotation. It includes classes for handling some standard internal Spring MVC
 * exceptions. Facade method {@code #handleException(Exception, WebRequest)} pass the Exception object of one of
 * these classes to the appropriate method</p>
 * <p>Some of these methods I can override. For other exceptions that don't handle with this methods I can create
 * my own {@code @ExceptionHandler} handlers.</p>
 * @see ApiExceptionDetails ApiExceptionDetails as parameter of overrided methods.
 * NB: I can define this class as private static.
 * @author https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 * @author https://habr.com/ru/post/342214/
 * @author Viktor Kuchuhurnyi (annotation marked Ivan)
 */

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

// --------------------------------------------------------------------------------------------------- 400 BAD_REQUEST
    /** thrown when argument annotated with @Valid failed validation.
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container
     * */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
            errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        for (ObjectError objectError : ex.getBindingResult().getGlobalErrors())
            errors.add(objectError.getObjectName() + ": " + objectError.getDefaultMessage());
        ApiExceptionDetails exDetails = ApiExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Some model fields is invalid: " + ex.getLocalizedMessage())
                .errors(errors)
                .build();
        return ResponseEntity.badRequest().body(exDetails);
//                handleExceptionInternal(ex, exDetails, headers, exDetails.getStatus(), request);
    }

// ---------------------------------------------------------------------------------------------------- 400 BAD_REQUEST
    /** reports the result of constraint violations.
     * @see ApiExceptionDetails
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + "" + violation.getPropertyPath() + ": "
                    + violation.getMessage());
        }
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Some model fields cause constraint violation: " + ex.getLocalizedMessage())
                .errors(errors)
                .build();
        return
//                ResponseEntity.badRequest().body(details);
                ResponseEntity.status(details.getStatus()).headers(new HttpHeaders()).body(details);
//                new ResponseEntity<Object>(details, new HttpHeaders(), details.getStatus());
    }

// ---------------------------------------------------------------------------------------------------- 400 BAD_REQUEST
    /** thrown when the part of a multipart request not found
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .error("Parameters invalid. The " + ex.getVariableName() + " variable is missed. " +
                        "Please review the guideline")
                .build();
        return new ResponseEntity<Object>(details, new HttpHeaders(), details.getStatus());
    }

// ---------------------------------------------------------------------------------------------------- 400 BAD_REQUEST
    /** thrown when method argument is not the expected type.
     * @see ApiExceptionDetails
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = ex.getRequiredType() != null?
                ex.getName() + " should be of type " + ex.getRequiredType().getName()
                : "Unknown required type MethodArgumentTypeMismatchException";
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .error(error)
                .build();
        return new ResponseEntity<Object>(details, new HttpHeaders(), details.getStatus());
    }


    //    =====================================================================
// ------------------------------------------------------------------------------------------------------ 404 NOT_FOUND
    /** executes when NoHandlerFoundException is thrown, i.e. when no handlers found to fulfill the request.
     * NB that 404 error is handled by DispatcherServlet by default, so to throw this exception, it's required
     * to set the property {@code setThrowExceptionIfNoHandlerFound} of DispatcherServlet to true
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container */
    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {
        String error = ex.getClass().getSimpleName();
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND)
//                .message("controller method has thrown " + ex.getClass().getSimpleName())
                .message(ex.getLocalizedMessage()) // = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL()
                .error(error)
                .build();
        return handleExceptionInternal(ex, details, headers, status, request);
    }
//    =====================================================================

// --------------------------------------------------------------------------------------------- 405 METHOD_NOT_ALLOWED
    /** thrown when one sends a requested with an unsupported HTTP method
     * @see ApiExceptionDetails
     * @see #handleException(Exception, WebRequest)
     * @return a ResponseEntity instance containing all the basic exception info in the ApiExceptionDetails container
     * */
    @Override
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // "...Ambiguous @ExceptionHandler method mapped..."
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getMethod()).append(" method is not supported for this request. Supported methods are ");
        if (ex.getSupportedHttpMethods() != null) ex.getSupportedHttpMethods().forEach(t -> sb.append(t).append(" "));
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .message(ex.getLocalizedMessage())
                .error(sb.toString())
                .build();
        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    }

// ----------------------------------------------------------------------------------------- 500 INTERNAL_SERVER_ERROR
        @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request) {
        ApiExceptionDetails details = ApiExceptionDetails.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("The item was not found.")
                .error(ex.getLocalizedMessage() + ": " + request.getDescription(false))
                .build();
        return ResponseEntity.status(details.getStatus()).headers(new HttpHeaders()).body(details);
    }
}

