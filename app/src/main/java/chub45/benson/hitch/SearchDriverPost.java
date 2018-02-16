package chub45.benson.hitch;

/**
 * Using Date for the date makes this class necessary
 */

import android.net.Uri;

import java.util.Date;

public class SearchDriverPost
{
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
    private String departure_time;

    /**
     * The number of spots left in the driver's vehicle
     */
    private int available_spots;

    /**
     * The driver's account email
     */
    private String author_email;

    /**
     * The driver's profile picture
     */
    private Uri profile_pic;

    /**
     * An optional description for any other information the driver
     * wants to include
     */
    private String description;

    /**
     * Creates a post with an empty description
     * @param departing_area the departing_area
     * @param destination the trip's destination
     * @param departure_time the time the driver is leaving
     * @param available_spots the number of available spots for passengers
     * @param author_email the account email of the post's author
     * @param profile_pic the author's profile picture
     */
    public SearchDriverPost() {}

    public SearchDriverPost(String departing_area, String destination,
                            String departure_time, int available_spots, String author_email,
                            Uri profile_pic)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author_email = author_email;
        this.profile_pic = profile_pic;
        this.description = "";
    }

    /**
     * Creates a post with a non-empty description
     * @param departing_area the departing_area
     * @param destination the trip's destination
     * @param departure_time the time the driver is leaving
     * @param available_spots the number of available spots for passengers
     * @param author_email the author of the post
     * @param description the post's description
     */
    public SearchDriverPost(String departing_area, String destination,
                            String departure_time, int available_spots, String author_email,
                            Uri profile_pic, String description)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author_email = author_email;
        this.profile_pic = profile_pic;
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
     * Gets the number of spots left in the driver's vehicle
     * @return number of available spots in the driver's vehicle
     */
    public Integer getavailable_spots()
    {
        return available_spots;
    }

    /**
     * Gets the post's author
     * @return the post's author
     */
    public String get_author()
    {
        return author_email;
    }

    /**
     * Gets the author's profile picture
     * @return the url to the author's profile picture
     */
    public Uri get_profile_pic() { return profile_pic; }

    /**
     * Gets the post's optional description
     * Returns an empty string if the description is blank
     * @return the post's description
     */
    public String getdescription()
    {
        return description;
    }

    public void set_departing_area(String departing_area) {
        this.departing_area = departing_area;
    }

    public void set_destination(String destination) {
        this.destination = destination;
    }

    public void set_departure_time(String time) {
        this.departure_time = time;
    }

    public void set_available_spots(int size) {
        this.available_spots = size;
    }

    public void set_description(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.getdeparting_area();
    }
}