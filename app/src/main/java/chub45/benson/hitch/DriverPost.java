package chub45.benson.hitch;

/**
 * Created by kailash on 2/4/18.
 */

import java.util.Date;

public class DriverPost implements Post
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
    private Date departure_time;

    /**
     * The number of spots left in the driver's vehicle
     */
    private int available_spots;

    /**
     * The driver's account
     */
    private User author;

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
     * @param author the author of the post
     */
    public DriverPost(String departing_area, String destination,
                Date departure_time, int available_spots, User author)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author = author;
        this.description = "";
    }

    /**
     * Creates a post with a non-empty description
     * @param departing_area the departing_area
     * @param destination the trip's destination
     * @param departure_time the time the driver is leaving
     * @param available_spots the number of available spots for passengers
     * @param author the author of the post
     * @param description the post's description
     */
    public DriverPost(String departing_area, String destination,
                Date departure_time, int available_spots, User author,
                String description)
    {
        this.departing_area = departing_area;
        this.destination = destination;
        this.departure_time = departure_time;
        this.available_spots = available_spots;
        this.author = author;
        this.description = description;
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
    public int get_ride_size_restriction()
    {
        return available_spots;
    }

    /**
     * Gets the post's author
     * @return the post's author
     */
    public User get_author()
    {
        return author;
    }

    /**
     * Gets the post's optional description
     * Returns an empty string if the description is blank
     * @return the post's description
     */
    public String get_description()
    {
        return description;
    }

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
}