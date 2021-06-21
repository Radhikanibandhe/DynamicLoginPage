package com.example.loginactivity.Authentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.ConsumerActivities.drawerElements.NavigationDrawer;
import com.example.loginactivity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity {

    Button login;
    TextView textView, forgetPass, producerlink;
    EditText gmail_field, pass_field;
    String gmail_login, pass_login;
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_btn);
        textView = findViewById(R.id.link);
        producerlink = findViewById(R.id.producerlink);
        gmail_field = findViewById(R.id.email);
        pass_field = findViewById(R.id.password);
        forgetPass = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(Login.this);
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setTitle("Forgot Password")
                        .setMessage("Enter your Email address?")
                        .setView(taskEditText)
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());

                                if(task.isEmpty()){
                                    Toast.makeText(Login.this, "Please enter valid Email!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                progressBar.setVisibility(View.VISIBLE);
                                FirebaseAuth auth = FirebaseAuth.getInstance();

                                auth.sendPasswordResetEmail(task.trim())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Login.this, "Resent link sent to your Email!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gmail_login = gmail_field.getText().toString().trim();
                pass_login = pass_field.getText().toString();

                if(gmail_login.isEmpty()){
                    gmail_field.setError("Please Enter Email!");
                    return;
                }
                if(pass_login.isEmpty()){
                    pass_field.setError("Invalid Password!");
                    return;
                }

                //Date is valid
                //Login the user
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(gmail_login, pass_login).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_LONG).show();

                        checkUserAccessLevel(authResult.getUser().getUid());
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    }
                });


            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                //finish();
            }
        });

        producerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProducerRegistration.class));
            }
        });

    }

    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fstore.collection("userAuthentication").document(uid);
        //Extract data from firestore
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "OnSuccess: "+ documentSnapshot.getData());

               if(documentSnapshot.getString("isUser")!= null){
                    startActivity(new Intent(Login.this, NavigationDrawer.class));
                    Toast.makeText(Login.this, "Login as customer", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        DocumentReference df1 = fstore.collection("producerAuthentications").document(uid);
        //Extract data from firestore
        df1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "OnSuccess: "+ documentSnapshot.getData());

                //identify the user access level
                if(documentSnapshot.getString("isAdmin")!= null){
//                    startActivity(new Intent(Login.this, ProducerModernDashboard.class));
                    Toast.makeText(Login.this, "Login as admin", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("userAuthentication").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if(documentSnapshot.getString("isUser")!=null){
                        startActivity(new Intent(getApplicationContext(), NavigationDrawer.class));
                        Toast.makeText(Login.this, "Login as customer", Toast.LENGTH_SHORT).show();

                        finish();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });


            DocumentReference df1 = FirebaseFirestore.getInstance().collection("producerAuthentications").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if(documentSnapshot.getString("isAdmin")!=null){
//                        startActivity(new Intent(getApplicationContext(), ProducerModernDashboard.class));
                        Toast.makeText(Login.this, "Login as admin", Toast.LENGTH_SHORT).show();

                        finish();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });



        }

    }
}