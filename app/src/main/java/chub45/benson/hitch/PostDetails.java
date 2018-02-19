package chub45.benson.hitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PostDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_post_details);

        Intent intent = getIntent();
        TextView here = (TextView) findViewById(R.id.here);
        here.setText(intent.getExtras().getString("necessary_info"));
    }
}
