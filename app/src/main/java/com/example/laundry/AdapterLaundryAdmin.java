package com.example.laundry;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterLaundryAdmin extends RecyclerView.Adapter<AdapterLaundryAdmin.MyViewHolder>{
    public String latitudeUser,longitudeUser,Lx,Tx;
    Context context;
    ArrayList<ModelLaundry> LaundryArraylist;

    public AdapterLaundryAdmin(Context context, ArrayList<ModelLaundry> LaundryArraylist) {
        this.context = context;
        this.LaundryArraylist = LaundryArraylist;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_admin,parent,false);
        return new AdapterLaundryAdmin.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelLaundry user = LaundryArraylist.get(position);
        holder.nama_laundry.setText(user.nama_laundry);
        holder.id_laundry.setText(user.IdLaundry);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), InformasiLaundry.class);
                i.putExtra("nama_laundry", user.nama_laundry.toString());
                i.putExtra("jenis", user.jenis.toString());
                i.putExtra("jamBuka", user.jamBuka.toString());
                i.putExtra("jamTutup", user.jamTutup.toString());
                i.putExtra("informasi", user.informasi.toString());
                i.putExtra("telepon", user.telepon.toString());
                i.putExtra("alamat", user.alamat.toString());
                i.putExtra("IdLaundry", user.IdLaundry.toString());
                i.putExtra("latitudeLaundry", user.latitudeLaundry.toString());
                i.putExtra("longitudeLaundry", user.longitudeLaundry.toString());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return LaundryArraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama_laundry,id_laundry;
        Button btn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_laundry = itemView.findViewById(R.id.UsernameLaundry);
            id_laundry = itemView.findViewById(R.id.id_laundry);
            btn = itemView.findViewById(R.id.btn_info_laundry_admin);

        }
    }
}
