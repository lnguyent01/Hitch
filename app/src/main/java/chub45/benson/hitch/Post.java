package chub45.benson.hitch;

/**
 * Created by kailash on 2/4/18.
 */

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public interface Post
{
    public String get_departing_area();
    public String get_destination();
    public Date get_departure_time();
    public Integer get_ride_size_restriction();
    public FirebaseUser get_author();
    //public Uri get_profile_pic();
    public String get_description();
    public void set_departing_area(String departing_area);
    public void set_destination(String destination);
    public void set_departure_time(Date time);
    public void set_ride_size_restriction(int size);
    public void set_description(String description);
}
