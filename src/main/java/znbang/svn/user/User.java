package znbang.svn.user;

public class User {
    private String email;

    public User() {}

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            this.email = "";
        } else {
            this.email = email.trim();
        }
    }
}
