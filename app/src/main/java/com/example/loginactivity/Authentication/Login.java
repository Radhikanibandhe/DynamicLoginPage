package com.example.loginactivity.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    Button login;
    TextView textView;
    EditText gmail_field, pass_field;
    String gmail_login, pass_login;
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_btn);
        textView = findViewById(R.id.link);
        gmail_field = findViewById(R.id.email);
        pass_field = findViewById(R.id.password);

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

                mAuth.signInWithEmailAndPassword(gmail_login, pass_login).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_LONG).show();

                        checkUserAccessLevel(authResult.getUser().getUid());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
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

    }

    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fstore.collection("userAuthentication").document(uid);
        //Extract data from firestore
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "OnSuccess: "+ documentSnapshot.getData());

                //identify the user access level
                if(documentSnapshot.getString("isAdmin")!= null){
//                    startActivity(new Intent(Login.this, ProducerModernDashboard.class));
                    Toast.makeText(Login.this, "Login as admin", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(documentSnapshot.getString("isUser")!= null){
//                    startActivity(new Intent(Login.this, ModernDashboard.class));
                    Toast.makeText(Login.this, "Login as customer", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if(documentSnapshot.getString("isAdmin")!=null){
//                        startActivity(new Intent(getApplicationContext(), ProducerModernDashboard.class));
                        Toast.makeText(Login.this, "Login as admin", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                    if(documentSnapshot.getString("isUser")!=null){
//                        startActivity(new Intent(getApplicationContext(), ModernDashboard.class));
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
        }

    }
}