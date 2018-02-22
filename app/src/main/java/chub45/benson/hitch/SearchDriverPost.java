package chub45.benson.hitch;

/**
 * Using Date for the date makes this class necessary
 */

import android.net.Uri;

import java.util.Date;

public class SearchDriverPost
{

    private String departing_area;

    /**
     * The area the driver is leaving from in form of a google id
     */
    private String departing_area_id;

    /**
     * The driver's destination
     */
    private String destination;

    /**
     * The driver's destination in form of a google id
     */
    private String destination_id;

    /**
     * The time the driver is leaving
     */
    private String departure_time;

    /**
     * The number of spots left in the driver's vehicle
     */
    private String available_spots;

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
    private String post_id;

    /**
     * Passengers who have requested to join the ride
     */
    private String potential_passengers;

    /**
     * Passengers accepted by the driver
     */
    private String accepted_passengers;

    public SearchDriverPost() {}

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
    public SearchDriverPost(String departing_area, String destination,
                      String departure_area_id, String destination_id,
                      String departure_time, String available_spots,
                      String author_email, Uri author_profile_pic, String author_uid)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departing_area_id = departure_area_id;
        this.destination_id = destination_id;
        this.departure_time = departure_time.toString();
        this.available_spots = available_spots;
        this.author_email = author_email;
        this.author_profile_pic = author_profile_pic;
        this.author_uid = author_uid;
        this.description = "";

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
    public SearchDriverPost(String departing_area, String destination,
                      String departure_time, String available_spots,
                      String author_email, Uri author_profile_pic, String author_uid,
                      String description)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time.toString();
        this.available_spots = available_spots;
        this.author_email = author_email;
        this.author_profile_pic = author_profile_pic;
        this.author_uid = author_uid;
        this.description = description;

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
    public String getdeparture_time()
    {
        return departure_time;
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
     * Gets the number of spots left in the driver's vehicle
     * @return number of available spots in the driver's vehicle
     */
    public String getavailable_spots()
    {
        return available_spots;
    }

    /**
     * Gets the post's author's account UID
     * @return the post's author's account UID
     */
    public String getauthor_uid()
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
    public String getpost_id() { return this.post_id; }

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

    public void set_departing_area_id(String departing_area_id) {
        this.departing_area_id = departing_area_id;
    }

    public void set_destination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    public void set_destination(String destination) {
        this.destination = destination;
    }

    public void set_departure_time(String time) {
        this.departure_time = time;
    }

    public void set_available_spots(String size) {
        this.available_spots = size;
    }

    public void set_description(String description) {
        this.description = description;
    }

    public void setpotential_passengers(String potential_passengers) { this.potential_passengers = potential_passengers; }

    public void setaccepted_passengers(String accepted_passengers) { this.accepted_passengers = accepted_passengers; }


    @Override
    public String toString() {
        return this.getpost_id();
    }
}
