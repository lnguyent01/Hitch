package chub45.benson.hitch;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by kailash on 2/22/18.
 */

public class PostFactoryTest {
    private final PostFactory factory;

    public PostFactoryTest() {
        factory = new DefaultPostFactory();
    }

    @Test
    public void testCreatePostFromDb() {
        String departing_area = "Place";
        String departing_area_id = "Place id";
        String destination = "Destination";
        String destination_id = "Destination id";
        Date d = new Date();
        String departure_time = d.toString();
        int available_spots = 3;
        String userId = "User's id";
        String userEmail = "user@gmail.com";
        String description = "testing post";
        int post_id = 4;
        String potential_passengers = "no requests";
        String accepted_passengers = "Bill|Joe";
        Post factoryPost = factory.createPostFromDb(departing_area, destination, departing_area_id, destination_id, departure_time,
                available_spots, userId, userEmail, description, post_id, potential_passengers, accepted_passengers);
        Post post = new DriverPost(departing_area, destination, departing_area_id, destination_id, d, available_spots,
                userEmail, userId, description, post_id, potential_passengers, accepted_passengers);
        assertEquals(factoryPost, post);
    }
}