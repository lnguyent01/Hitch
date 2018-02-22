package chub45.benson.hitch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddPostActivity extends AppCompatActivity {

    private static PostFactory postFactory = new DefaultPostFactory();
    private static HitchDatabase db = new HitchDatabase();
    private DatePickerDialogListener dateListener;
    private TimePickerDialogListener timeListener;
    private String departing_area, destination;
    private int year, month, day, hour, minutes;
    private TextView departureDateText, departureTimeText;
    private EditText availableSpotsText, descriptionText;
    private Button dateButton, timeButton, createPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();

        departing_area = "";
        destination = "";
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);

        dateListener = new DatePickerDialogListener();
        timeListener = new TimePickerDialogListener();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final PlaceAutocompleteFragment departingAreaText = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.departingAreaText);
        departingAreaText.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        AddPostActivity.this.setDeparting_area(place.getId());
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        final PlaceAutocompleteFragment destinationText = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destinationText);
        destinationText.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        AddPostActivity.this.setDestination(place.getId());
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        departureDateText = (TextView) findViewById(R.id.departureDateText);
        dateButton = (Button) findViewById(R.id.btn_date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPostActivity.this, dateListener,
                        AddPostActivity.this.year, AddPostActivity.this.month, AddPostActivity.this.day);
                datePickerDialog.show();
            }
        });

        departureTimeText = (TextView) findViewById(R.id.departureTimeText);
        timeButton = (Button) findViewById(R.id.btn_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddPostActivity.this, timeListener,
                        AddPostActivity.this.hour, AddPostActivity.this.minutes, false);
                timePickerDialog.show();
            }
        });

        availableSpotsText = (EditText) findViewById(R.id.availableSpotsText);
        descriptionText = (EditText) findViewById(R.id.displayDescriptionText);

        createPostButton = (Button) findViewById(R.id.createPostButton);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                c.set(AddPostActivity.this.year, AddPostActivity.this.month, AddPostActivity.this.day,
                        AddPostActivity.this.hour, AddPostActivity.this.minutes);
                Date date = c.getTime();
                if (validInputs()) {
                    Post post = postFactory.createPost(departing_area, destination,
                            date, Integer.parseInt(availableSpotsText.getText().toString()),
                            user, descriptionText.getText().toString());
                    db.addPost(post);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid data entered", Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void setDeparting_area(String departing_area) { this.departing_area = departing_area; }

    public void setDestination(String destination) { this.destination = destination; }

    public void setYear(int year) { this.year = year; }

    public void setMonth(int month) { this.month = month; }

    public void setDay(int day) { this.day = day; }

    public void setHour(int hour) { this.hour = hour; }

    public void setMinutes(int minutes) { this.minutes = minutes; }

    private boolean validInputs() {
        // TODO: replace stub
        return true;
    }

    private class DatePickerDialogListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            AddPostActivity.this.setYear(year);
            AddPostActivity.this.setMonth(monthOfYear + 1);
            AddPostActivity.this.setDay(dayOfMonth);
            departureDateText.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
        }
    }

    private class TimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
            AddPostActivity.this.setHour(hour);
            AddPostActivity.this.setMinutes(minutes);
            String AM_PM;
            if (hour < 12) {
                AM_PM = " AM";
            }
            else {
                AM_PM = " PM";
            }
            String min = String.valueOf(minutes);
            if (minutes == 0) {
                min = "00";
            }
            String s_hour = String.valueOf(hour);
            if (hour > 12) {
                s_hour = String.valueOf(hour - 12);
            }
            departureTimeText.setText(s_hour + ":" + min + AM_PM);
        }
    }
}