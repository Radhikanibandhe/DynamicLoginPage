package com.example.loginactivity.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.loginactivity.R;
import com.example.loginactivity.introductory.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.widget.Toast;

import com.example.loginactivity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button registerBtn;
    EditText nameEdit, addressEdit, phoneEdit, emailEdit, passwordEdit, cityEdit, stateEdit;
    String name, address, phone, email, password, city, country;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    ProgressBar progressBar;

    FusedLocationProviderClient fusedLocationProviderClient;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.register_btn);
        nameEdit = findViewById(R.id.fullName);
        addressEdit = findViewById(R.id.address_register);
        phoneEdit = findViewById(R.id.phone_register);
        emailEdit = findViewById(R.id.email_register);
        passwordEdit = findViewById(R.id.passwrod_register);
        cityEdit = findViewById(R.id.cityRegister);
        stateEdit = findViewById(R.id.stateRegister);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameEdit.getText().toString().trim();
                address = addressEdit.getText().toString();
                phone = phoneEdit.getText().toString().trim();
                email = emailEdit.getText().toString().trim();
                password = passwordEdit.getText().toString().trim();
                city = cityEdit.getText().toString().trim();
                country = stateEdit.getText().toString().trim();


                if(name.isEmpty()){
                    nameEdit.setError("Name is required!");
                    return;
                }

                if(address.isEmpty()){
                    addressEdit.setError("Enter valid address!");
                    return;
                }

                if(phone.isEmpty()){
                    phoneEdit.setError("Enter valid phone!");
                    return;
                }

                if(email.isEmpty()){
                    emailEdit.setError("Enter valid email!");
                    return;
                }


                if(password.isEmpty()){
                    passwordEdit.setError("Enter password!");
                    return;
                }
                if(password.length() < 6){
                    passwordEdit.setError("password length should be more than 6!");
                    return;
                }

                if (city.isEmpty()) {
                    cityEdit.setError("City is required!");
                    return;
                }

                if (country.isEmpty()) {
                    stateEdit.setError("State is required!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                mAuth = FirebaseAuth.getInstance();
                mStore = FirebaseFirestore.getInstance();

                mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        //send to next page

                        Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();

                        DocumentReference df = mStore.collection("userAuthentication").document(user.getUid());

                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("FullName", name);
                        userInfo.put("Address", address);
                        userInfo.put("PhoneNumber", phone);
                        userInfo.put("UserEmail", email);
                        userInfo.put("City", city);
                        userInfo.put("Country", country);
                        userInfo.put("FullName",name);
                        userInfo.put("Address", address);
                        userInfo.put("PhoneNumber", phone);
                        userInfo.put("UserEmail", email);
                        userInfo.put("imageUrl", "https://firebasestorage.googleapis.com/v0/b/loginactivity-ace62.appspot.com/o/imageUrl%2Fuser.png?alt=media&token=bce251f4-cbd8-4713-871c-53746ce1724c");
                        userInfo.put("isUser", "0");


                        df.set(userInfo);
                        progressBar.setVisibility(View.GONE);


                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);


                        Toast.makeText(Register.this, "Unable to create user, please try again!", Toast.LENGTH_SHORT).show();
                    }
                });

                progressBar.setVisibility(View.GONE);

            }
        });

    }


    @SuppressLint("MissingPermission")
    private void getLocation() {


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){

                    try {

                        progressBar.setVisibility(View.VISIBLE);

                        Geocoder geocoder = new Geocoder(Register.this,
                                Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude()
                                , location.getLongitude(), 1);

                        cityEdit.setText(addresses.get(0).getLocality());
                        stateEdit.setText(addresses.get(0).getCountryName());
                        addressEdit.setText(addresses.get(0).getAddressLine(0));


                        if(!cityEdit.getText().toString().isEmpty()){
                            Toast.makeText(Register.this, "Your current Location", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);


                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }
            }
        });


    }

    public void getLocation(View view) {

        //check permission
        if (ActivityCompat.checkSelfPermission(Register.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(Register.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


    }


}

