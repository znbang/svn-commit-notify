package znbang.svn.user;

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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ErrorFactory errorFactory;

    @MockBean
    private UserStore userStore;

    @Test
    public void test() throws Exception {
        User user1 = new User("aaa@gmail.com");
        User user2 = new User("bbb@gmail.com");

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        given(userStore.list()).willReturn(list);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()));
    }

    @Test
    public void testInvalidEmail() throws Exception {
        User user = new User("domain.com");
        String requestBody = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
