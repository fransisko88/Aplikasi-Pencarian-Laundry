package com.example.laundry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ListLaundry extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ImageButton btn_filter;
    Button btn_favorit;
    RecyclerView recyclerView;
    ArrayList<ModelLaundry> LaundryArraylist;
    AdapterLaundry myAdapter;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    TextView jrk,hl;
    FirebaseFirestore firestore;
    ModelLaundry dataModel;
    DocumentReference reference,Redit;
    DatabaseReference rfs;
    Context context;
    SeekBar jarak_bar;
    HashMap<String,Object> userMAP;
    public String latitudeUser,longitudeUser,id_user,nilaiJarak,batas,jrk_lokasi;
    TextView latUser,longUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_laundry);



        btn_filter = findViewById(R.id.btn_filter);
        latUser = findViewById(R.id.latitudeUser);
        longUser = findViewById(R.id.longitudeUser);

        Intent data1 = getIntent();
        latitudeUser = data1.getStringExtra("latitude");
        longitudeUser = data1.getStringExtra("longitude");
        id_user = data1.getStringExtra("idUser");
        jrk_lokasi = data1.getStringExtra("jarak_lokasi");

        firestore = FirebaseFirestore.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }


        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ListLaundry.this, R.style.BottomSheetDialohTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_botton_sheet,(LinearLayout)findViewById(R.id.bottomsheetcontainer));
                jarak_bar = bottomSheetView.findViewById(R.id.radius_jarak);
                jrk = bottomSheetView.findViewById(R.id.halo);

                jarak_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        nilaiJarak = String.valueOf(i);
                        if (String.valueOf(i).equals("0")){
                            jrk.setVisibility(View.GONE);
                        }else{
                            jrk.setVisibility(View.VISIBLE);
                            jrk.setText("Jarak Pencarian : " + nilaiJarak + " Meter");
                        }

                        //Toast.makeText(getApplicationContext(), "Jarak Radius : " + seekBar.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                bottomSheetView.findViewById(R.id.btn_fav).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), Favorit.class);
                        i.putExtra("idUser", id_user);
                        startActivity(i);
                    }
                });

                bottomSheetView.findViewById(R.id.btn_tentukan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        double value = Double.parseDouble(nilaiJarak);
                        double hasil_value = value/1000;
                        batas = String.valueOf(hasil_value);
                        db.collection("laundry").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                LaundryArraylist.clear();
                                for (DocumentChange dc : value.getDocumentChanges()){

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


                                    double lat2 = Double.parseDouble(latitudeUser);
                                    double lon2 = Double.parseDouble(longitudeUser);
                                    double lat1 = Double.parseDouble(latLa);
                                    double lon1 = Double.parseDouble(longLa);

                                    Double pi = 3.14159265358979;
                                    Double R = 6371e3;

                                    Double latRad1 = lat1 * (pi / 180);
                                    Double latRad2 = lat2 * (pi / 180);
                                    Double deltaLatRad = (lat2 - lat1) * (pi / 180);
                                    Double deltaLonRad = (lon2 - lon1) * (pi / 180);

                                    /* RUMUS HAVERSINE */
                                    Double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) + Math.cos(latRad1) * Math.cos(latRad2) * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);
                                    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                                    Double s = (R * c) / 1000; // hasil jarak dalam meter


                            if(s <= hasil_value){
                                LaundryArraylist.add(new ModelLaundry(s,id, alamat, informasi, jamBUka, jamTutup, jenis, latLa, longLa, nama, telepon));
                            }

                            Collections.sort(LaundryArraylist, new Comparator<ModelLaundry>() {
                                @Override
                                public int compare(ModelLaundry modelLaundry, ModelLaundry t1) {
                                    return modelLaundry.jarak.compareTo(t1.jarak);
                                }

                            });

                            myAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                                }
                            }
                        });


                        Toast.makeText(getApplicationContext(), "Mencari Jarak Radius " + nilaiJarak + " Meter", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.hide();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Mencari Laundry...");
        progressDialog.show();


        recyclerView = findViewById(R.id.recycler_view_listLaundry);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), "Lokasi Laundry Tidak Tersedia", Toast.LENGTH_SHORT).show();

                progressDialog.cancel();
                if(LaundryArraylist.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Mencari Lokasi Laundry", Toast.LENGTH_SHORT).show();
                }
            }
        },2000);

        db = FirebaseFirestore.getInstance();

        LaundryArraylist = new ArrayList<ModelLaundry>();
        dataModel = new ModelLaundry();
        tampil_list();


        myAdapter = new AdapterLaundry(ListLaundry.this,LaundryArraylist);

        recyclerView.setAdapter(myAdapter);


    }

    private void EventChangeListener() {
        db.collection("laundry").orderBy("nama_laundry", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    if(progressDialog.isShowing())progressDialog.dismiss();
                    Log.e("Database Error",error.getMessage());
                    return;
                }
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

    public void tampil_list(){
        db.collection("laundry").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                LaundryArraylist.clear();
                for (DocumentChange dc : value.getDocumentChanges()){
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


                    double lat2 = Double.parseDouble(latitudeUser);
                    double lon2 = Double.parseDouble(longitudeUser);
                    double lat1 = Double.parseDouble(latLa);
                    double lon1 = Double.parseDouble(longLa);

                    Double pi = 3.14159265358979;
                    Double R = 6371e3;

                    Double latRad1 = lat1 * (pi / 180);
                    Double latRad2 = lat2 * (pi / 180);
                    Double deltaLatRad = (lat2 - lat1) * (pi / 180);
                    Double deltaLonRad = (lon2 - lon1) * (pi / 180);

                    /* RUMUS HAVERSINE */
                    Double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) + Math.cos(latRad1) * Math.cos(latRad2) * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);
                    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    Double s = (R * c) / 1000; // hasil jarak dalam kilo meter

                    //LaundryArraylist.add(new ModelLaundry(s,id, alamat, informasi, jamBUka, jamTutup, jenis, latLa, longLa, nama, telepon));
                    //if(s <= 1.19){
                        LaundryArraylist.add(new ModelLaundry(s,id, alamat, informasi, jamBUka, jamTutup, jenis, latLa, longLa, nama, telepon));
                    //}
                    Collections.sort(LaundryArraylist, new Comparator<ModelLaundry>() {
                        @Override
                        public int compare(ModelLaundry modelLaundry, ModelLaundry t1) {
                            return modelLaundry.jarak.compareTo(t1.jarak);

                        }

                    });

                    myAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }



    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        double latUser = Double.parseDouble(latitudeUser);
        double longiUser = Double.parseDouble(longitudeUser);

        LatLng PosisiUser = new LatLng(latUser, longiUser);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PosisiUser,12));
        MarkerOptions options = new MarkerOptions().position(PosisiUser).title("Lokasi Saya");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(options);

//        mMap.addMarker(new MarkerOptions().position(PosisiUser).title("Lokasi Saya"));

        firestore.collection("laundry").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc : value.getDocumentChanges()){
                    String latLaundry = dc.getDocument().getString("latitudeLaundry");
                    String longiLaundry = dc.getDocument().getString("longitudeLaundry");
                    String nama_laundry = dc.getDocument().getString("nama_laundry");

                    double latL = Double.parseDouble(latLaundry);
                    double longiL = Double.parseDouble(longiLaundry);

                    LatLng PosisiLaundry = new LatLng(latL, longiL);
                    mMap.addMarker(new MarkerOptions().position(PosisiLaundry).title(nama_laundry));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PosisiLaundry,12));
                }
            }
        });
    }
}