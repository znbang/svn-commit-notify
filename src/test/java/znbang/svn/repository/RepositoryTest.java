package znbang.svn.repository;

import org.junit.Test;

public class RepositoryTest {
    @Test
    public void test() {
        Repository a = new Repository();
        a.setUrl(null);
        assert a.getUrl().isEmpty();

        a.setUrl("  url/  ");
        assert "url".equals(a.getUrl());
    }
}
