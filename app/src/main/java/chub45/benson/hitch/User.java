package chub45.benson.hitch;

/**
 * Created by kailash on 2/7/18.
 */

public class User {
    private String email;
    private String username;
    private String fullName;

    public User(String email) {
        this.email = email;
    }

    public User(String email, String username, String fullName){
        this.email = email;
        this.username = username;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName(){
        return fullName;
    }

    public String getUsername() { return username; }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String toString(){
        return username;
    }
}
