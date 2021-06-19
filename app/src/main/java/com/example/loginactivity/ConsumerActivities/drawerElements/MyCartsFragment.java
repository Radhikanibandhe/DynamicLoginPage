package com.example.loginactivity.ConsumerActivities.drawerElements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginactivity.R;
import com.example.loginactivity.adapters.MyCartAdapter;
import com.example.loginactivity.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class MyCartsFragment extends Fragment {


    FirebaseFirestore db;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    Button refresh;

    View layout1;
    View layout2;

    TextView textView;

    public MyCartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        layout1 = root.findViewById(R.id.constraint1);
        refresh = root.findViewById(R.id.refresh);
        layout2 = root.findViewById(R.id.constraint2);
        layout2.setVisibility(View.GONE);


        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);
        textView = root.findViewById(R.id.textView2);

        db.collection("AddToCart").document(mAuth.getCurrentUser().getUid())
                .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                int totalPrice=0;
                for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){

                    MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                    cartModelList.add(cartModel);
                    String[] a = cartModel.getTotalPrice().toString().split(" ");
                    totalPrice+= Integer.parseInt(a[a.length-1]);
                    cartAdapter.notifyDataSetChanged();


                }
                textView.setText("Total Price : "+ String.valueOf(totalPrice));

                if(totalPrice!=0){
                    layout2.setVisibility(View.VISIBLE);
                }else {
                    layout1.setVisibility(View.VISIBLE);
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().detach(MyCartsFragment.this).attach(MyCartsFragment.this).commit();
            }
        });

        return root;
    }

}