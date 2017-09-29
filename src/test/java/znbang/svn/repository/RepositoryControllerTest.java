package znbang.svn.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import znbang.svn.common.ErrorFactory;
import znbang.svn.common.SvnKit;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RepositoryController.class)
public class RepositoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ErrorFactory errorFactory;

    @MockBean
    private RepositoryStore repositoryStore;

    @MockBean
    private SvnKit svnKit;

    @Test
    public void test() throws Exception {
        Repository repo1 = new Repository("http://1234.com", 1234);
        Repository repo2 = new Repository("http://5678.com", 5678);

        List<Repository> list = new ArrayList<>();
        list.add(repo1);
        list.add(repo2);

        given(repositoryStore.list()).willReturn(list);

        mockMvc.perform(get("/api/repositories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].url").value(repo1.getUrl()))
                .andExpect(jsonPath("$[0].revision").value(repo1.getRevision()))
                .andExpect(jsonPath("$[1].url").value(repo2.getUrl()))
                .andExpect(jsonPath("$[1].revision").value(repo2.getRevision()));
    }

    @Test
    public void testInvalidUrl() throws Exception {
        Repository repo = new Repository("1234.com", 1234);
        String requestBody = objectMapper.writeValueAsString(repo);
        mockMvc.perform(post("/api/repositories")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
