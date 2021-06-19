package com.example.loginactivity.ConsumerActivities.ui.profile;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.loginactivity.Authentication.Login;
import com.example.loginactivity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
public class ProfileFragment extends Fragment {
    TextView name,address,city,country,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId, ImageUrl;
    Button ok, update;
    ImageView imageView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        name = root.findViewById(R.id.profileName);
        address = root.findViewById(R.id.profileAddress);
        city = root.findViewById(R.id.profileCity);
        country = root.findViewById(R.id.profileCountry);
        email = root.findViewById(R.id.profileEmail);
        phone = root.findViewById(R.id.profilePhone);
        ok = root.findViewById(R.id.buttonLogout);
        update = root.findViewById(R.id.buttonUpdate);
        imageView = root.findViewById(R.id.profileImage);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("userAuthentication").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText(documentSnapshot.getString("FullName"));
                address.setText(documentSnapshot.getString("Address"));
                phone.setText(documentSnapshot.getString("PhoneNumber"));
                email.setText(documentSnapshot.getString("UserEmail"));
                country.setText(documentSnapshot.getString("Country"));
                city.setText(documentSnapshot.getString("City"));
                ImageUrl = (String) documentSnapshot.get("imageUrl");
                Glide.with(getContext()).load(documentSnapshot.get("imageUrl")).into(imageView);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class));
                getActivity().finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = (new Intent(getActivity(),UpdateProfile.class));
                i.putExtra("FullName" , name.getText().toString());
                i.putExtra("Address" , address.getText().toString());
                i.putExtra("PhoneNumber" , phone.getText().toString());
                i.putExtra("Country" , country.getText().toString());
                i.putExtra("City" , city.getText().toString());
                i.putExtra("Email", email.getText().toString());
                i.putExtra("imageUrl" , ImageUrl);
                startActivity(i);

            }
        });
        return root;
    }
}
