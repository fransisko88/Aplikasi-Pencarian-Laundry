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

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyViewHolder>{

    Context context;
    ArrayList<ModelUser> UserArraylist;

    public AdapterUser(Context context, ArrayList<ModelUser> UserArraylist) {
        this.context = context;
        this.UserArraylist = UserArraylist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemuser,parent,false);
        return new AdapterUser.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelUser user = UserArraylist.get(position);
        holder.nama.setText(user.fName);
        holder.alamat.setText(user.alamat);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), InformasiUser.class);
                i.putExtra("fName", user.fName.toString());
                i.putExtra("alamat", user.alamat.toString());
                i.putExtra("IdUser", user.idUser.toString());
                i.putExtra("telepon", user.telepon.toString());
                i.putExtra("email", user.email.toString());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserArraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama,alamat;
        Button btn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.Username);
            alamat = itemView.findViewById(R.id.Alamat);
            btn = itemView.findViewById(R.id.btn_info_user);
        }
    }
}
