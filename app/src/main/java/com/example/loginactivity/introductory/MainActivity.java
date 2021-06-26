package com.example.loginactivity.introductory;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.example.loginactivity.Authentication.Login;
import com.example.loginactivity.ConsumerActivities.drawerElements.NavigationDrawer;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    LottieAnimationView lottieAnimationView;
    FirebaseAuth mAuth;
    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textlogo);
        lottieAnimationView = findViewById(R.id.animation);
        mAuth = FirebaseAuth.getInstance();


        //Adding action listner to show proper output with delay
        textView.animate().translationY(1700).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1600).setDuration(1000).setStartDelay(4000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mAuth.getCurrentUser() != null) {
                    Toast.makeText(MainActivity.this, "Please wait!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), NavigationDrawer.class));
                    finish();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mAuth.getCurrentUser() != null) {
                    Toast.makeText(MainActivity.this, "Please wait!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), NavigationDrawer.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), OnBoadingScreen.class));
                    finish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
