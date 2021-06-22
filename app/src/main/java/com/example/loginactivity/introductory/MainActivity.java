package com.example.loginactivity.introductory;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textlogo);
        lottieAnimationView = findViewById(R.id.animation);
        mAuth = FirebaseAuth.getInstance();


        //Adding action listner to show proper output with delay
        textView.animate().translationY(1700).setDuration(1000).setStartDelay(5000);
        lottieAnimationView.animate().translationY(1600).setDuration(1000).setStartDelay(5000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


                if (mAuth.getCurrentUser() != null) {
                    Toast.makeText(MainActivity.this, "Please wait you are already Login!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), NavigationDrawer.class));
                    finish();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());


                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_manager,paperOnboardingFragment);
                fragmentTransaction.commit();

//                Adding right swap action listener to redirect on Login page
                paperOnboardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
                @Override
                public void onRightOut() {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                }
                });


            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {

        PaperOnboardingPage src1 = new PaperOnboardingPage("Fresh Food","Get best quality food everyday",
                Color.parseColor("#F85C70"),R.drawable.restaurant,R.drawable.dish);

        PaperOnboardingPage src2 = new PaperOnboardingPage("Fast Delivery","Get fast delivery at your doorstep",
                Color.parseColor("#F85C70"),R.drawable.delivery,R.drawable.fast);

        PaperOnboardingPage src3 = new PaperOnboardingPage("Easy Payment","Get Payment done as fast as Cheetah",
                Color.parseColor("#F85C70"),R.drawable.payment,R.drawable.card);

        ArrayList <PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(src1);
        elements.add(src2);
        elements.add(src3);
        return elements;



    }
}

//ifsc acount pan addhar other
