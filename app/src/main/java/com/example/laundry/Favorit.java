package com.example.laundry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Favorit extends AppCompatActivity {
    ImageButton btn_back;
    RecyclerView recyclerView;
    ArrayList<ModelLaundry> LaundryArraylist;
    AdapterFavorit myAdapter;
    TextView txt;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    ModelLaundry dataModel;
    public String id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);
        btn_back = findViewById(R.id.btn_backFavorit);
        txt = findViewById(R.id.textView8);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Favorit.this , ListLaundry.class));
                finish();
            }
        });

        Intent data1 = getIntent();
        id_user = data1.getStringExtra("idUser");


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Mencari Laundry...");
        progressDialog.show();




        recyclerView = findViewById(R.id.recycler_view_favorit);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        LaundryArraylist = new ArrayList<ModelLaundry>();
        dataModel = new ModelLaundry();

//        db.collection("laundry_favorit").whereEqualTo("idUser", id_user).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                for (DocumentChange dc : value.getDocumentChanges()){
//                    if(dc.getType() == DocumentChange.Type.ADDED){
//                        LaundryArraylist.add(dc.getDocument().toObject(ModelLaundry.class));
//                    }
//                    myAdapter.notifyDataSetChanged();
//                    if(progressDialog.isShowing())
//                        progressDialog.dismiss();
//                }
//            }
//        });
        tampil_favorit();

        myAdapter = new AdapterFavorit(Favorit.this,LaundryArraylist);

        recyclerView.setAdapter(myAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), "Lokasi Laundry Tidak Tersedia", Toast.LENGTH_SHORT).show();

                progressDialog.cancel();
                if(LaundryArraylist.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Daftar Favorit Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        },2000);





    }

    public void tampil_favorit(){
        db.collection("laundry_favorit").whereEqualTo("idUser", id_user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        LaundryArraylist.add(dc.getDocument().toObject(ModelLaundry.class));
                    }
                    myAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }
}