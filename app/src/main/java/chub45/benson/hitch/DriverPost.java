package chub45.benson.hitch;

/**
 * Created by kailash on 2/4/18.
 */

import android.net.Uri;


//import java.sql.Driver;
import java.util.Date;

public class DriverPost implements Post
{
    private static int post_counter = 2;
    /**
     * The area the driver is leaving from
     */
    private String departing_area;

    /**
     * The driver's destination
     */
    private String destination;

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
     * Creates a post with an empty description
     * @param departing_area the departing_area
     * @param destination the trip's destination
     * @param departure_time the time the driver is leaving
     * @param available_spots the number of available spots for passengers
     * @param author_email the post's author's email
     * @param author_profile_pic the post's author's profile picture
     * @param author_uid the post's author's account UID
     */
    public DriverPost(String departing_area, String destination,
                Date departure_time, int available_spots,
                String author_email, Uri author_profile_pic, String author_uid)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author_email = author_email;
        this.author_profile_pic = author_profile_pic;
        this.author_uid = author_uid;
        this.description = "";
        this.post_id = this.post_counter;
        this.post_counter++;
    }

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
    public DriverPost(String departing_area, String destination,
                Date departure_time, int available_spots,
                String author_email, Uri author_profile_pic, String author_uid,
                String description)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author_email = author_email;
        this.author_profile_pic = author_profile_pic;
        this.author_uid = author_uid;
        this.description = description;
        this.post_id = this.post_counter;
        this.post_counter++;
    }

    /**
     * Gets the departing area
     * @return the departing area
     */
    public String getdeparting_area()
    {
        return departing_area;
    }

    /**
     * Gets the trip destination
     * @return the trip destination
     */
    public String getdestination()
    {
        return destination;
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

    public void set_departure_time(Date time) {
        this.departure_time = time;
    }

    public void set_available_spots(int size) {
        this.available_spots = size;
    }

    public void set_description(String description) {
        this.description = description;
    }

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
