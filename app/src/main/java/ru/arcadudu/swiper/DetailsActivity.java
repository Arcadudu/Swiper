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
        Model model = (Model) incomingIntent.getSerializableExtra("selected_model");


        String modelTitle = model.getTitle();
        String modelDescription = model.getDescription();
        int modelImage = model.getImage();
        title.setText(modelTitle);
        description.setText(modelDescription);
        image.setImageResource(modelImage);

    }


}