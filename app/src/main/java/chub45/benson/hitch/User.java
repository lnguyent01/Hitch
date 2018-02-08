package chub45.benson.hitch;

/**
 * Created by kailash on 2/7/18.
 */

public class User {
    private String email;
    private String username;

    public User(String email) {
        this.email = email;
    }

    public User(String email, String username){
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() { return username; }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String toString(){
        return username;
    }
}
