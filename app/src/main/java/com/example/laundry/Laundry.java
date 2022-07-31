package com.example.laundry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Laundry extends AppCompatActivity {
    Button btn_tambah;
    ImageButton btn_back;
    RecyclerView recyclerView;
    ArrayList<ModelLaundry> LaundryArraylist;
    AdapterLaundryAdmin myAdapter;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    ModelLaundry dataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);
        btn_tambah = findViewById(R.id.btn_tambahLaundry);
        btn_back = findViewById(R.id.btn_backLaundry);

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Laundry.this , RegisterLaundry.class));
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Laundry.this , MainActivity.class));
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Mencari Laundry...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), "Lokasi Laundry Tidak Tersedia", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
                if(LaundryArraylist.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Lokasi Laundry Tidak Tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        },2000);


        recyclerView = findViewById(R.id.recycler_view_listLaundryAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        LaundryArraylist = new ArrayList<ModelLaundry>();
        myAdapter = new AdapterLaundryAdmin(Laundry.this,LaundryArraylist);
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();


    }

    private void EventChangeListener() {
        db.collection("laundry").orderBy("nama_laundry").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    if(progressDialog.isShowing())progressDialog.dismiss();
                    Log.e("Database Error",error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    dataModel = new ModelLaundry();
                    String nama = dc.getDocument().getString("nama_laundry");
                    String id = dc.getDocument().getString("IdLaundry");
                    String jenis = dc.getDocument().getString("jenis");
                    String telepon = dc.getDocument().getString("telepon");
                    String jamBUka = dc.getDocument().getString("jamBuka");
                    String jamTutup = dc.getDocument().getString("jamTutup");
                    String informasi = dc.getDocument().getString("informasi");
                    String alamat = dc.getDocument().getString("alamat");

                    String latLa = dc.getDocument().getString("latitudeLaundry");
                    String longLa = dc.getDocument().getString("longitudeLaundry");

                    dataModel.setNama_laundry(nama);
                    dataModel.setIdLaundry(id);
                    dataModel.setTelepon(telepon);
                    dataModel.setInformasi(informasi);
                    dataModel.setJamTutup(jamTutup);
                    dataModel.setJamBuka(jamBUka);
                    dataModel.setJenis(jenis);
                    dataModel.setAlamat(alamat);
                    dataModel.setLatitudeLaundry(latLa);
                    dataModel.setLongitudeLaundry(longLa);
                    LaundryArraylist.add(dataModel);
//                    if(dc.getType() == DocumentChange.Type.ADDED){
//                        LaundryArraylist.add(dc.getDocument().toObject(ModelLaundry.class));
//                    }
                    myAdapter.notifyDataSetChanged();
//                    if (LaundryArraylist.isEmpty()){
//                        progressDialog.hide();
//                    }
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }
}