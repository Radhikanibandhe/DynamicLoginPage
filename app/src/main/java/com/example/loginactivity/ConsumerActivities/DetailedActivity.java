package com.example.loginactivity.ConsumerActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.loginactivity.R;
import com.example.loginactivity.models.ViewAllModel;

public class DetailedActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name, price, rating, description;
    Button addToCart;
    ImageView addItem, removeItem;
    Toolbar toolbar;

    ViewAllModel viewAllModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Object object = getIntent().getSerializableExtra("details");

        if(object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }

        imageView = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        price = findViewById(R.id.detailed_price);
        rating = findViewById(R.id.detailed_rating);
        description = findViewById(R.id.detailed_description);
        addToCart = findViewById(R.id.add_to_cart);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        if(viewAllModel!=null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(imageView);
            rating.setText(viewAllModel.getRating());
            description.setText(viewAllModel.getDescription());
            price.setText("Price : ₹"+viewAllModel.getPrice());
            name.setText(viewAllModel.getName());
        }


    }
}