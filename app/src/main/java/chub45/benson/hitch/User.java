package chub45.benson.hitch;

/**
 * Created by kailash on 2/7/18.
 */

public class User {
    private String uid;
    private String username;
    private String profilePicUrl;
    private String fullName;
    private String phoneNo;
    private String city;
    private String state;
    private String rideRequests;
    private String activeRides;

    public User() {}

    public User(String uid, String profilePicUrl, String username, String fullName, String phoneNo, String city, String state){
        this.uid = uid;
        this.profilePicUrl = profilePicUrl;
        this.username = username;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.city = city;
        this.state = state;
        this.rideRequests = "";
        this.activeRides = "";
    }

    public String getUid() {
        return uid;
    }

    public String getProfilePicUrl() { return profilePicUrl; }

    public String getUsername() { return username; }

    public String getFullName(){
        return fullName;
    }

    public String getPhoneNo() { return phoneNo; }

    public String getCity() { return city; }

    public String getState() { return state; }

    public String getRideRequests() { return rideRequests; }

    public String getActiveRides() { return activeRides; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setRideRequests(String rideRequests) {
        this.rideRequests = rideRequests;
    }

    public void setActiveRides(String activeRides) {
        this.activeRides = activeRides;
    }

    public void addRideRequest(String postID){
        if(rideRequests.isEmpty()){
            rideRequests = postID;
        }
        else{
            rideRequests += "|" + postID;
        }
    }
}
