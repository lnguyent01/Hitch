package chub45.benson.hitch;

import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kailash on 2/4/18.
 */

public class DefaultPostFactory implements PostFactory {
    public Post createPost(String departure_area, String destination, Date departure_time, int available_spots, FirebaseUser user, String description) {
        Post post = new DriverPost(departure_area, destination, departure_time, available_spots, user.getEmail(),
                                   user.getPhotoUrl(), user.getUid(), description);
        return post;
    }

    public Post createPostFromDb(String departure_area, String destination, String departure_time, int available_spots, String userId, String email,
                                 String description, int post_id, String potential_passengers, String accepted_passengers) {
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date;

        try {
            date = formatter.parse(departure_time);
        } catch (Exception e) {
            // should never happen, if it does there are errors in how the posts are being
            // saved in the database
            Calendar c = Calendar.getInstance();
            date = c.getTime();
        }

        Post post = new DriverPost(departure_area, destination, date, available_spots, email, userId, description, post_id,
                                    potential_passengers, accepted_passengers);
        return post;
    }
}
