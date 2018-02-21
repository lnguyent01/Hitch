package chub45.benson.hitch;

/**
 * Created by kailash on 2/4/18.
 */

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public interface Post
{
    public String getdeparting_area();
    public String getdestination();
    public Date getdeparture_time();
    public Integer getavailable_spots();

    public String getauthor();
    public String getauthor_email();
    public Integer get_post_id();
    public Uri getauthor_profile_pic();
    public String getdescription();
    public String getpotential_passengers();
    public String getaccepted_passengers();

    public static Place getPlaceFromId(Context context, String id) {
        GoogleApiClient client = new GoogleApiClient.Builder(context).addApi(Places.GEO_DATA_API).build();
        client.connect();

        PendingResult<PlaceBuffer> departing_result = Places.GeoDataApi.getPlaceById(client, id);
        PlaceBuffer place = departing_result.await();
        return place.get(0);
    }

    public void set_departing_area(String departing_area);
    public void set_destination(String destination);
    public void set_departure_time(Date time);
    public void set_available_spots(int size);
    public void set_description(String description);
    public void setpotential_passengers(String potential_passengers);
    public void setaccepted_passengers(String accepted_passengers);
    public void add_potential_passenger(String passenger);
    public void add_accepted_passenger(String passenger);
    public void remove_potential_passenger(String passenger);
    public void remove_accepted_passenger(String passenger);
}
