package znbang.svn.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import znbang.svn.common.ErrorFactory;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class UserController {
    private ErrorFactory errorFactory;
    private UserStore userStore;

    public UserController(ErrorFactory errorFactory, UserStore userStore) {
        this.errorFactory = errorFactory;
        this.userStore = userStore;
    }

    @InitBinder
    public void initbinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(new UserValidator());
    }

    @GetMapping("/api/users")
    public ResponseEntity list() {
        try {
            return ResponseEntity.ok().body(userStore.list());
        } catch (IOException e) {
            return errorFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, "user.list_email_failed", e.getMessage());
        }
    }

    @PostMapping("/api/users")
    public ResponseEntity create(@Valid @RequestBody User user) {
        try {
            userStore.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            return errorFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, "user.save_email_failed", e.getMessage());
        }
    }

    @DeleteMapping("/api/users")
    public ResponseEntity delete(@RequestParam String email) {
        try {
            userStore.delete(new User(email));
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            return errorFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, "user.delete_email_failed", e.getMessage());
        }
    }

    private class UserValidator implements Validator {
        @Override
        public boolean supports(Class<?> clazz) {
            return User.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            User user = (User) target;
            if (user.getEmail().isEmpty()) {
                errors.rejectValue("email", "user.email_is_required");
            } else if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
                errors.rejectValue("email", "user.email_is_invalid");
            }
        }
    }
}
