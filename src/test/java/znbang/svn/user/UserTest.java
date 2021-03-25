package znbang.svn.user;

import org.junit.Test;

public class UserTest {
    @Test
    public void test() {
        User user = new User();
        user.setEmail(null);
        assert user.getEmail().isEmpty();

        user.setEmail("  email  ");
        assert "email".equals(user.getEmail());
    }
}
