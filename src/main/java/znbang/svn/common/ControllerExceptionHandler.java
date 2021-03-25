package znbang.svn.common;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {
    private MessageSource messageSource;

    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, Locale locale) {
        BindingResult bindingResult = exception.getBindingResult();
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (!errors.containsKey(fieldError.getField())) {
                errors.put(fieldError.getField(), messageSource.getMessage(fieldError.getCode(), fieldError.getArguments(), locale));
            }
        }
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("statusCode", HttpStatus.BAD_REQUEST.value());
        errorAttributes.put("message", errors.values().iterator().next());
        errorAttributes.put("errors", errors);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(errorAttributes);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception exception) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorAttributes.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON_UTF8).body(errorAttributes);
    }
}
