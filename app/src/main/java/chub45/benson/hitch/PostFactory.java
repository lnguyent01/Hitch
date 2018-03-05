package chub45.benson.hitch;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

/**
 * Created by kailash on 2/4/18.
 */

public interface PostFactory {
    public Post createPost(String departure_area, String destination, String departure_area_id, String destination_id,
                           Date departure_time, int available_spots, FirebaseUser user, String description, int post_id);
    public Post createPostFromDb(String departure_area, String destination, String departure_area_id, String destination_id,
                                 String departure_time, int available_spots, String userId, String email,
                                 String description, int post_id, String potential_passengers, String accepted_passengers);
}
