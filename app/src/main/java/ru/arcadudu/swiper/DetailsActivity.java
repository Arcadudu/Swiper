package ru.arcadudu.swiper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        image = findViewById(R.id.detailed_image);
        title = findViewById(R.id.detailed_title);
        description = findViewById(R.id.detailed_description);

        Intent incomingIntent = getIntent();
        Model model = incomingIntent.getParcelableExtra("selected_model");
        Log.d("model", "onCreate: details activity " + (model == null));
//        image.setImageResource(model.getImage());
//        title.setText(model.getTitle());
//        description.setText(model.getTitle());

    }
}