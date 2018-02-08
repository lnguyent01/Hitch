package chub45.benson.hitch;

import java.util.Date;

/**
 * Created by kailash on 2/4/18.
 */

public interface PostFactory {
    public Post createPost(String departure_area, String destination, Date departure_time, int available_spots, User author, String description);
}
