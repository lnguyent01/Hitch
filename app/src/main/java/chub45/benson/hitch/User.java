package chub45.benson.hitch;

/**
 * Created by kailash on 2/7/18.
 */

public class User {
    private String uid;
    private String username;
    private String fullName;
    private String city;
    private String state;
    private String rideRequests;
    private String activeRides;

    public User(String uid, String username, String fullName, String city, String state){
        this.uid = uid;
        this.username = username;
        this.fullName = fullName;
        this.city = city;
        this.state = state;
        this.rideRequests = "";
        this.activeRides = "";
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() { return username; }

    public String getFullName(){
        return fullName;
    }

    public String getCity() { return city; }

    public String getState() { return state; }

    public String getRideRequests() { return rideRequests; }

    public String getActiveRides() { return activeRides; }

    public void addRideRequest(String postID){
        if(rideRequests.isEmpty()){
            rideRequests = postID;
        }
        else{
            rideRequests += "|" + postID;
        }
    }
}
