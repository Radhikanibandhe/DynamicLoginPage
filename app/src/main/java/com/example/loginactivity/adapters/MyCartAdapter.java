package com.example.loginactivity.adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginactivity.ConsumerActivities.drawerElements.MyCartsFragment;
import com.example.loginactivity.R;
import com.example.loginactivity.models.MyCartModel;
import com.example.loginactivity.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;



public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<MyCartModel> cartModelList;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public MyCartAdapter(Context context, List<MyCartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @NotNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyCartAdapter.ViewHolder holder, int position) {

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Glide.with(context).load(cartModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText("Price : ₹ "+cartModelList.get(position).getProductPrice());
        holder.quantity.setText("Quantity : "+cartModelList.get(position).getTotalQuantity());
        holder.totalPrice.setText("Total "+cartModelList.get(position).getTotalPrice());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uiid = mAuth.getCurrentUser().getUid();

                db.collection("AddToCart/" +uiid+ "/CurrentUser")
                       .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                        int game = 0;

                        for (QueryDocumentSnapshot snapshot:task.getResult()){
                            if(game==position){
                                db.collection("AddToCart/" +uiid+ "/CurrentUser").document(snapshot.getId()).delete();
                                Toast.makeText(context, cartModelList.get(position).getProductName()+" removed from cart please refresh the cart", Toast.LENGTH_SHORT).show();
                            }
                            game+=1;
                        }

                    }
                });

                MyCartAdapter myCartAdapter = new MyCartAdapter(v.getContext(), cartModelList);
                myCartAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, deleteBtn;
        TextView name, price, quantity, totalPrice;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_img);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.product_quantity);
            totalPrice = itemView.findViewById(R.id.product_totalPrice);
            deleteBtn = itemView.findViewById(R.id.delete_current_product);
        }
    }
}
