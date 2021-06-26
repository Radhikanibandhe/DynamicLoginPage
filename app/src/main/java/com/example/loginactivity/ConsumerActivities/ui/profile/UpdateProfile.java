package com.example.loginactivity.ConsumerActivities.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.loginactivity.Authentication.Login;
import com.example.loginactivity.Authentication.Register;
import com.example.loginactivity.ConsumerActivities.drawerElements.NavigationDrawer;
import com.example.loginactivity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {

    EditText name, address, phone, city, country;
    ImageView imageView;
    private static final int GALLERY_REQUEST_CODE = 123;
    Uri mImageUri;

    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    StorageReference mStorageRef;
    ProgressBar progressBar;

    String updatedName, updatedAdderss, updatedPhone, updatedEmail,updatedUri, updatedCity, updatedCountry;

    Button save;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = findViewById(R.id.updateName);
        address = findViewById(R.id.updateAddress);
        phone = findViewById(R.id.updatePhone);
        city = findViewById(R.id.updateCity);
        country = findViewById(R.id.updateCountry);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        imageView = findViewById(R.id.circleImageView);
        save = findViewById(R.id.save);

        //Extract values form previous activity
        Intent intent = getIntent();
        name.setText(intent.getExtras().getString("FullName"));
        address.setText(intent.getExtras().getString("Address"));
        phone.setText(intent.getExtras().getString("PhoneNumber"));
        city.setText(intent.getExtras().getString("City"));
        country.setText(intent.getExtras().getString("Country"));


        if(!intent.getExtras().getString("imageUrl").isEmpty()){
            Glide.with(getApplicationContext())
                    .load(intent.getExtras().getString("imageUrl"))
                    .centerCrop().into(imageView);
        }

        //Firebase objects
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("imageUrl");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatedName = name.getText().toString().trim();
                updatedAdderss = address.getText().toString().trim();
                updatedPhone = phone.getText().toString().trim();
                updatedEmail = intent.getExtras().getString("Email");
                updatedUri = intent.getExtras().getString("imageUrl");
                updatedCity = city.getText().toString().trim();
                updatedCountry = country.getText().toString().trim();


                if(updatedName.isEmpty()){
                    name.setError("Required Field");
                    return;
                }
                if(updatedAdderss.isEmpty()){
                    address.setError("Required Field");
                    return;
                }
                if(updatedPhone.isEmpty()){
                    phone.setError("Required Field");
                    return;
                }
                if(updatedPhone.length()!=10){
                    phone.setError("Phone number should have 10 digits");
                    return;
                }
                if(updatedCity.isEmpty()){
                    phone.setError("Required Field");
                    return;
                }
                if(updatedCountry.isEmpty()){
                    phone.setError("Required Field");
                    return;
                }

                uploadImage();
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pick an image"), GALLERY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            mImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    private void uploadImage() {

        if(mImageUri != null)
        {
            FirebaseUser user = mAuth.getCurrentUser();
            //send to next page

            DocumentReference df = fstore.collection("userAuthentication").document(user.getUid());
            StorageReference ref = mStorageRef.child( UUID.randomUUID().toString());

            ref.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    progressBar.setVisibility(View.VISIBLE);
                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("FullName",updatedName);
                                    userInfo.put("Address", updatedAdderss);
                                    userInfo.put("PhoneNumber", updatedPhone);
                                    userInfo.put("UserEmail", updatedEmail);
                                    userInfo.put("City", updatedCity);
                                    userInfo.put("Country", updatedCountry);

                                    userInfo.put("isUser", "1");

                                    if(mImageUri!=null){
                                        userInfo.put("imageUrl", uri.toString());
                                    }

                                    df.set(userInfo);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Updated Successfully!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), NavigationDrawer.class));
                                    finish();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateProfile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UpdateProfile.this, "Updating please Wait! ", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{


            FirebaseUser user = mAuth.getCurrentUser();
            //send to next page

            DocumentReference df = fstore.collection("userAuthentication").document(user.getUid());
            progressBar.setVisibility(View.VISIBLE);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("FullName",updatedName);
            userInfo.put("Address", updatedAdderss);
            userInfo.put("PhoneNumber", updatedPhone);
            userInfo.put("UserEmail", updatedEmail);
            userInfo.put("City", updatedCity);
            userInfo.put("Country", updatedCountry);

            userInfo.put("isUser", "1");
            userInfo.put("imageUrl", updatedUri);

            df.set(userInfo);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(),"Updated Successfully!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), NavigationDrawer.class));
            finish();

        }

    }


}