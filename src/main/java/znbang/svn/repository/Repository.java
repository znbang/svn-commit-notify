package znbang.svn.repository;

public class Repository {
    private String url;
    private long revision;

    public Repository() {}

    public Repository(String url, long revision) {
        this.url = url;
        this.revision = revision;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url == null) {
            url = "";
        } else {
            url = url.trim();
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
        }
        this.url = url;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }
}
