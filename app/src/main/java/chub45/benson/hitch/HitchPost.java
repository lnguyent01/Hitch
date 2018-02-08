package chub45.benson.hitch;

import java.util.Date;

/**
 * Created by Mattmitch on 2/7/18.
 */

public class HitchPost implements Post {

    private String departingArea;
    private String destination;
    private Date departureTime;
    private Integer rideSize;
    private User author;
    private String description;

    public HitchPost(String departingArea, String destination, Date departureTime, int rideSize, User author, String description) {
        this.departingArea = departingArea;
        this.destination = destination;
        this.departureTime = departureTime;
        this.rideSize = rideSize;
        this.author = author;
        this.description = description;
    }


    public String get_departing_area() { return departingArea; }
    public String get_destination()  { return destination; }
    public Date get_departure_time()  { return departureTime; }
    public Integer get_ride_size_restriction()  { return rideSize; }
    public User get_author()  { return author; }
    public String get_description()  { return description; }

    public void set_departing_area(String departing_area) {
        this.departingArea = departing_area;
    }
    public void set_destination(String destination){
        this.destination = destination;
    }
    public void set_departure_time(Date time){
        this.departureTime = time;
    }
    public void set_ride_size_restriction(int size) {
        this.rideSize = size;
    }
    public void set_description(String description){
        this.description = description;
    }

    public String toString(){
        return author.getUsername();
    }
}
