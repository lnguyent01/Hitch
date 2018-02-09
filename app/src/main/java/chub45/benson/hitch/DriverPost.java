package chub45.benson.hitch;

/**
 * Created by kailash on 2/4/18.
 */

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class DriverPost implements Post
{
    private static int post_counter = 0;
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
     * The post's author
     */
    private FirebaseUser author;

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
     * Creates a post with an empty description
     * @param departing_area the departing_area
     * @param destination the trip's destination
     * @param departure_time the time the driver is leaving
     * @param available_spots the number of available spots for passengers
     * @param author the post's author
     */
    public DriverPost(String departing_area, String destination,
                Date departure_time, int available_spots, FirebaseUser author)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author = author;
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
     * @param author the post's author
     * @param description the post's description
     */
    public DriverPost(String departing_area, String destination,
                Date departure_time, int available_spots, FirebaseUser author, String description)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author = author;
        this.description = description;
        this.post_id = this.post_counter;
        this.post_counter++;
    }

    /**
     * Gets the departing area
     * @return the departing area
     */
    public String get_departing_area()
    {
        return departing_area;
    }

    /**
     * Gets the trip destination
     * @return the trip destination
     */
    public String get_destination()
    {
        return destination;
    }

    /**
     * Gets the time the driver is leaving
     * @return the trip's departure time
     */
    public Date get_departure_time()
    {
        return departure_time;
    }

    /**
     * Gets the number of spots left in the driver's vehicle
     * @return number of available spots in the driver's vehicle
     */
    public Integer get_ride_size_restriction()
    {
        return available_spots;
    }

    /**
     * Gets the post's author
     * @return the post's author
     */
    public FirebaseUser get_author()
    {
        return author;
    }

    /**
     * Gets the author's profile picture
     * @return the url to the author's profile picture
     */
    public Uri get_profile_pic() { return author.getPhotoUrl(); }

    /**
     * Gets the post's optional description
     * Returns an empty string if the description is blank
     * @return the post's description
     */
    public String get_description()
    {
        return description;
    }

    /**
     * Gets the post's id
     * @return the post's id
     */
    public int get_post_id() { return this.post_id; }

    public void set_departing_area(String departing_area) {
        this.departing_area = departing_area;
    }

    public void set_destination(String destination) {
        this.destination = destination;
    }

    public void set_departure_time(Date time) {
        this.departure_time = time;
    }

    public void set_ride_size_restriction(int size) {
        this.available_spots = size;
    }

    public void set_description(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return Integer.toString(this.get_post_id());
    }
}