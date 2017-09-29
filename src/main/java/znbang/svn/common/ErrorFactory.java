package znbang.svn.common;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorFactory {
    private MessageSource messageSource;

    public ErrorFactory(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String getMessage(String code, String ...args) {
        try {
            return messageSource.getMessage(code, args, null);
        } catch (NoSuchMessageException e) {
            return code;
        }
    }

    public ResponseEntity error(HttpStatus status, String code, String ...args) {
        return ResponseEntity.status(status).body(new ErrorMessage(status.value(), getMessage(code, args)));
    }
}
