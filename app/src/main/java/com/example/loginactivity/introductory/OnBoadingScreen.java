package com.example.loginactivity.introductory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.loginactivity.Authentication.Login;
import com.example.loginactivity.ConsumerActivities.drawerElements.NavigationDrawer;
import com.example.loginactivity.R;
import com.example.loginactivity.adapters.SliderAdapter;

public class OnBoadingScreen extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotLayout;

    Button let_get_started, next_btn ,skip_btn;

    Animation animation;

    SliderAdapter sliderAdapter;
    TextView[] dots;

    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boading_screen);

        viewPager = findViewById(R.id.slider);
        dotLayout = findViewById(R.id.dots);
        let_get_started = findViewById(R.id.get_started_btn);
        next_btn = findViewById(R.id.next_btn);
        skip_btn = findViewById(R.id.skip_btn);

        //call adapter
        sliderAdapter = new SliderAdapter(this);

        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void skip(View view){

        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void next(View view){

        viewPager.setCurrentItem(currentPosition+1);

    }


    private void addDots(int position){

        dots = new TextView[3];
        dotLayout.removeAllViews();

        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.status));
        }

    }


    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);

            currentPosition = position;

            if(position==0){
                skip_btn.setVisibility(View.VISIBLE);
                next_btn.setVisibility(View.VISIBLE);
                let_get_started.setVisibility(View.INVISIBLE);
            }
            else if(position==1){
                skip_btn.setVisibility(View.VISIBLE);
                next_btn.setVisibility(View.VISIBLE);
                let_get_started.setVisibility(View.INVISIBLE);
            }
            else {
                skip_btn.setVisibility(View.INVISIBLE);
                next_btn.setVisibility(View.INVISIBLE);
                animation = AnimationUtils.loadAnimation(OnBoadingScreen.this, R.anim.bottom_animation);
                let_get_started.setAnimation(animation);
                let_get_started.setVisibility(View.VISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}