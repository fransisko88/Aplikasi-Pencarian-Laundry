package com.example.laundry;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterFavorit extends RecyclerView.Adapter<AdapterFavorit.MyViewHolder>{
    public String latitudeUser,longitudeUser,IdUser,namaUser,hv;
    Context context;
    ArrayList<ModelLaundry> LaundryArraylist;

    public AdapterFavorit(Context context, ArrayList<ModelLaundry> LaundryArraylist) {
        this.context = context;
        this.LaundryArraylist = LaundryArraylist;
    }
    @NonNull
    @Override
    public AdapterFavorit.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemfavorit,parent,false);
        return new AdapterFavorit.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelLaundry user = LaundryArraylist.get(position);
        holder.nama_laundry.setText(user.nama_laundry);
//        DecimalFormat df = new DecimalFormat("#.##");
        //holder.txt_jarak.setText(String.valueOf(df.format(user.jarak)));
       // holder.txt_jarak.setText(df.format(user.jarak));
        holder.jamBuka.setText(user.jamBuka);
        holder.jamTutup.setText(user.jamTutup);
        holder.jenis.setText(user.jenis);


        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        //String currentid = user1.getUid();




        reference = firestore.collection("users").document(user1.getUid());

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    latitudeUser = task.getResult().getString("latitude");
                    longitudeUser = task.getResult().getString("longitude");
                    IdUser = task.getResult().getString("idUser");
                    namaUser = task.getResult().getString("fName");
                    holder.lat.setText(latitudeUser);
                    holder.lng.setText(longitudeUser);
                }
            }
        });

        holder.btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), DetailLaundry.class);
                i.putExtra("nama_laundry", user.nama_laundry.toString());
                i.putExtra("jenis", user.jenis.toString());
                i.putExtra("jamBuka", user.jamBuka.toString());
                i.putExtra("jamTutup", user.jamTutup.toString());
                i.putExtra("telepon", user.telepon.toString());
                i.putExtra("informasi", user.informasi.toString());
                i.putExtra("alamat", user.alamat.toString());
                i.putExtra("IdLaundry", user.IdLaundry.toString());
                i.putExtra("latitudeLaundry", user.latitudeLaundry.toString());
                i.putExtra("longitudeLaundry", user.longitudeLaundry.toString());
                i.putExtra("latitudeUser", holder.lat.getText().toString());
                i.putExtra("longitudeUser", holder.lng.getText().toString());
                i.putExtra("idUser", IdUser);
                i.putExtra("fName", namaUser);
                i.putExtra("jarak", "");
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return LaundryArraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama_laundry,jamBuka,jamTutup,btn_info,jenis,lat,lng,txt_jarak;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_laundry = itemView.findViewById(R.id.UsernameLaundry);
            jamBuka = itemView.findViewById(R.id.JamBuka);
            jamTutup = itemView.findViewById(R.id.JamTutup);
            jenis = itemView.findViewById(R.id.jenis);
            btn_info = itemView.findViewById(R.id.btn_info_laundry);
            lat = itemView.findViewById(R.id.latitudeUser);
            lng = itemView.findViewById(R.id.longitudeUser);

        }
    }
}
