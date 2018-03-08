package chub45.benson.hitch;

/**
 * Created by kailash on 2/4/18.
 */

import android.content.Context;
import android.net.Uri;
import android.util.Log;


//import java.sql.Driver;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.Date;

public class DriverPost implements Post
{
    /**
     * The area the driver is leaving from in human readable format
     */
    private String departing_area;

    /**
     * The area the driver is leaving from in form of a google id
     */
    private String departing_area_id;

    /**
     * The driver's destination in human readable format
     */
    private String destination;

    /**
     * The driver's destination in form of a google id
     */
    private String destination_id;

    /**
     * The time the driver is leaving
     */
    private Date departure_time;

    /**
     * The number of spots left in the driver's vehicle
     */
    private int available_spots;

    /**
     * The post's author's email
     */
    private String author_email;

    /**
     * The post's author's profile picture
     */
    private Uri author_profile_pic;

    /**
     * The post's author's account UID
     */
    private String author_uid;

    /**
     * An optional description for any other information the driver
     * wants to include
     */
    private String description;

    /**
     * The post's id
     */
    private int post_id;

    /**
     * Passengers who have requested to join the ride
     */
    private String potential_passengers;

    /**
     * Passengers accepted by the driver
     */
    private String accepted_passengers;

    public DriverPost() {}

    /**
     * Creates a post with a non-empty description
     * @param departing_area the departing_area
     * @param destination the trip's destination
     * @param departure_time the time the driver is leaving
     * @param available_spots the number of available spots for passengers
     * @param author_email the post's author's email
     * @param author_profile_pic the post's author's profile picture
     * @param author_uid the post's author's account UID
     * @param description the post's description
     */
    public DriverPost(String departing_area, String destination, String departing_area_id, String destination_id,
                Date departure_time, int available_spots,
                String author_email, Uri author_profile_pic, String author_uid,
                String description, int post_id)
    {
        this.post_id = post_id;
        this.departing_area = departing_area;
        this.destination = destination;
        this.departing_area_id = departing_area_id;
        this.destination_id = destination_id;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author_email = author_email;
        this.author_profile_pic = author_profile_pic;
        this.author_uid = author_uid;
        this.description = description;
        this.potential_passengers = "";
        this.accepted_passengers = "";
    }

    /**
     * Creates a post in which all details are known
     * Currently used for recreating posts from database
     * @param departing_area the departing_area
     * @param destination the trip's destination
     * @param departure_time the time the driver is leaving
     * @param available_spots the number of available spots for passengers
     * @param author_email the post's author's email
     * // temporarily instantiated blank @param author_profile_pic the post's author's profile picture
     * @param author_uid the post's author's account UID
     * @param description the post's description
     * @param post_id the post's id
     * @param potential_passengers the trip's potential passengers
     * @param accepted_passengers the trip's accepted passengers
     */
    public DriverPost(String departing_area, String destination, String departing_area_id, String destination_id,
                      Date departure_time, int available_spots,
                      String author_email, String author_uid,
                      String description, int post_id, String potential_passengers,
                      String accepted_passengers)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.departing_area_id = departing_area_id;
        this.destination_id = destination_id;
        this.available_spots = available_spots;
        this.author_email = author_email;
        this.author_profile_pic = new Uri.Builder().path("").build();
        this.author_uid = author_uid;
        this.description = description;
        this.post_id = post_id;
        this.potential_passengers = potential_passengers;
        this.accepted_passengers = accepted_passengers;
    }

    public String getdeparting_area() {
        return departing_area;
    }

    public String getdestination() {
        return destination;
    }

    /**
     * Gets a Google Place id representation of the departing area
     * @return Google Place id of the departing area
     */
    public String getdeparting_area_id()
    {
        return departing_area_id;
    }

    /**
     * Gets a Google Place id representation of the trip destination
     * @return Google Place id of the trip destination
     */
    public String getdestination_id()
    {
        return destination_id;
    }

    /**
     * Gets the time the driver is leaving
     * @return the trip's departure time
     */
    public Date getdeparture_time()
    {
        return departure_time;
    }

    /**
     * Gets the number of spots left in the driver's vehicle
     * @return number of available spots in the driver's vehicle
     */
    public Integer getavailable_spots()
    {
        return available_spots;
    }

    /**
     * Gets the post's author's account UID
     * @return the post's author's account UID
     */
    public String getauthor()
    {
        return author_uid;
    }

    /**
     * Gets the post's author's email
     */
    public String getauthor_email()
    {
        return author_email;
    }

    /**
     * Gets the author's profile picture
     * @return the url to the author's profile picture
     */
    public Uri getauthor_profile_pic() { return author_profile_pic; }

    /**
     * Gets the post's optional description
     * Returns an empty string if the description is blank
     * @return the post's description
     */
    public String getdescription()
    {
        return description;
    }

    /**
     * Gets the post's id
     * @return the post's id
     */
    public Integer get_post_id() { return this.post_id; }

    /**
     * Gets the potential passengers
     * @return the ride's potential passengers
     */
    public String getpotential_passengers() { return this.potential_passengers; }

    /**
     * Gets the accepted passengers
     * @return the ride's accepted passengers
     */
    public String getaccepted_passengers() { return this.accepted_passengers; }

    public void set_departing_area(String departing_area) {
        this.departing_area = departing_area;
    }

    public void set_destination(String destination) {
        this.destination = destination;
    }

    public void set_departing_area_id(String departing_area_id) {
        this.departing_area_id = departing_area_id;
    }

    public void set_destination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    public void set_departure_time(Date time) {
        this.departure_time = time;
    }

    public void set_available_spots(int size) {
        this.available_spots = size;
    }

    public void set_description(String description) {
        this.description = description;
    }

    public void set_post_id(int post_id) { this.post_id = post_id; }

    public void setpotential_passengers(String potential_passengers) { this.potential_passengers = potential_passengers; }

    public void setaccepted_passengers(String accepted_passengers) { this.accepted_passengers = accepted_passengers; }

    public void add_potential_passenger(String passenger) {
        this.potential_passengers += passenger + "|";
    }

    public void add_accepted_passenger(String passenger) {
        this.accepted_passengers += passenger + "|";
        this.available_spots--;
    }

    public void remove_potential_passenger(String passenger) {
        this.potential_passengers.replace(passenger + "|", "");
    }

    public void remove_accepted_passenger(String passenger) {
        this.accepted_passengers.replace(passenger + "|", "");
        this.available_spots++;
    }

    @Override
    public String toString() {
        return Integer.toString(this.get_post_id());
    }
}
