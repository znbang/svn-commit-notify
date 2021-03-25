package znbang.svn.repository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.tmatesoft.svn.core.SVNException;
import znbang.svn.common.ErrorFactory;
import znbang.svn.common.SvnKit;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class RepositoryController {
    private ErrorFactory errorFactory;
    private RepositoryStore repositoryStore;
    private SvnKit svnKit;

    public RepositoryController(ErrorFactory errorFactory, RepositoryStore repositoryStore, SvnKit svnKit) {
        this.errorFactory = errorFactory;
        this.repositoryStore = repositoryStore;
        this.svnKit = svnKit;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(new RepositoryValidator());
    }

    @GetMapping("/api/repositories")
    public ResponseEntity list() {
        try {
            return ResponseEntity.ok().body(repositoryStore.list());
        } catch (IOException e) {
            return errorFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, "repository.list_url_failed", e.getMessage());
        }
    }

    @PostMapping("/api/repositories")
    public ResponseEntity create(@Valid @RequestBody Repository repository) {
        try {
            repository.setRevision(svnKit.getRevision(repository.getUrl()));
            repositoryStore.save(repository);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            return errorFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, "repository.save_url_failed", e.getMessage());
        } catch (SVNException e) {
            return errorFactory.error(HttpStatus.BAD_REQUEST, "repository.get_revision_failed", e.getMessage());
        }
    }

    @DeleteMapping("/api/repositories")
    public ResponseEntity delete(@RequestParam String url) {
        try {
            repositoryStore.delete(new Repository(url, -1));
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            return errorFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, "repository.delete_url_failed", e.getMessage());
        }
    }

    private class RepositoryValidator implements Validator {
        @Override
        public boolean supports(Class<?> clazz) {
            return Repository.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            Repository repository = (Repository) target;
            if (repository.getUrl().isEmpty()) {
                errors.rejectValue("url", "repository.url_is_required");
            } else if (!repository.getUrl().startsWith("http://")) {
                errors.rejectValue("url", "repository.url_is_invalid");
            }
        }
    }
}
