package com.example.laundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetailLaundry extends AppCompatActivity implements OnMapReadyCallback{

    TextView hh,latDetail,longiDetail,DetailInformasi,DetailNamaLaundry,DetailTelepon,DetailBuka,DetailTutup,DetailAlamat,DetailJenis,DetailJarak;
    public String idUser,namaUser,informasi,documentID,jarak_lokasi,nama_laundry,jenis,jamBuka,jamTutup,alamat,telepon,IdLaundry,latitudeLaundry,longitudeLaundry,latitudeUser,longitudeUser;
    StorageReference storageReference;
    public String id_fav,id_u,id_l;
    FirebaseAuth fAuth;
    private GoogleMap mMapDetail;
    CircleImageView foto;
    Button btn_tambah,btn_hapus;
    DocumentReference reference;
    private List<Polyline> polylines=null;
    FirebaseFirestore firestore;
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission=false;
    ImageButton btn_wa,btn_call;
    public double latUser,longiUser,latLaundry,longiLaundry;
    ArrayList markerPoints= new ArrayList();


    @SuppressLint("WrongViewCast")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laundry);
        DetailNamaLaundry = findViewById(R.id.UsernameLaundryDetail);
        DetailTelepon = findViewById(R.id.teleponDetail);
        DetailJenis = findViewById(R.id.jenisLaundry);
        DetailBuka = findViewById(R.id.jamBukaDetail);
        DetailTutup = findViewById(R.id.jamTutupDetail);
        DetailAlamat = findViewById(R.id.AlamatDetail);
        foto = findViewById(R.id.detailFotoLaundry);
        DetailJarak = findViewById(R.id.JarakDetail);
        DetailInformasi = findViewById(R.id.Informasi_Detail);
        btn_tambah = findViewById(R.id.btn_tambahFavorit);
        btn_wa = findViewById(R.id.btn_waDetail);
        btn_call = findViewById(R.id.btn_callDetail);
        latDetail = findViewById(R.id.latDetailLaundry);
        longiDetail = findViewById(R.id.longiDetailLaundry);
        btn_hapus = findViewById(R.id.btn_hapusFavorit);
        hh = findViewById(R.id.hh);

        Intent data = getIntent();
        nama_laundry = data.getStringExtra("nama_laundry");
        jenis = data.getStringExtra("jenis");
        jamBuka = data.getStringExtra("jamBuka");
        jamTutup = data.getStringExtra("jamTutup");
        alamat = data.getStringExtra("alamat");
        telepon = data.getStringExtra("telepon");
        IdLaundry = data.getStringExtra("IdLaundry");
        latitudeLaundry = data.getStringExtra("latitudeLaundry");
        longitudeLaundry = data.getStringExtra("longitudeLaundry");
        latitudeUser = data.getStringExtra("latitudeUser");
        longitudeUser = data.getStringExtra("longitudeUser");
        informasi = data.getStringExtra("informasi");
        idUser = data.getStringExtra("idUser");
        jarak_lokasi = data.getStringExtra("jarak");
        namaUser = data.getStringExtra("fName");

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("laundry/"+IdLaundry+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(foto);
//                progressDialog.dismiss();
            }
        });


        DetailNamaLaundry.setText(nama_laundry);
        DetailTelepon.setText("0"+telepon);
        DetailJenis.setText(jenis);
        DetailBuka.setText(jamBuka);
        DetailTutup.setText(jamTutup);
        DetailAlamat.setText(alamat);

        if(jarak_lokasi.isEmpty()){
            hh.setVisibility(View.GONE);
            DetailJarak.setVisibility(View.GONE);
        }else{
            DetailJarak.setText(jarak_lokasi);
        }



        DetailInformasi.setText(informasi);
        latDetail.setText("Latitude : "+latitudeLaundry);
        longiDetail.setText("Longitude : "+longitudeLaundry);

        firestore = FirebaseFirestore.getInstance();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDetail);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }


        latUser = Double.parseDouble(latitudeUser);
        longiUser= Double.parseDouble(longitudeUser);
        latLaundry = Double.parseDouble(latitudeLaundry);
        longiLaundry= Double.parseDouble(longitudeLaundry);

        btn_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomor = "+62"+ telepon;
                String pesan = "Hallo "+ nama_laundry;
                startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s", nomor, pesan)
                                )
                        )
                );
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+DetailTelepon.getText().toString()));
                startActivity(intent);
            }
        });


        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> user = new HashMap<>();
                documentID = firestore.collection("laundry_favorit").document().getId();
                user.put("IdLaundry_favorit", IdLaundry+idUser);
                user.put("IdLaundry", IdLaundry);
                user.put("idUser", idUser);
                user.put("nama_laundry", nama_laundry);
                user.put("jenis", jenis);
//                user.put("jarak", jarak_lokasi);
                user.put("telepon", telepon);
                user.put("jamBuka", jamBuka);
                user.put("jamTutup", jamTutup);
                user.put("informasi", informasi);
                user.put("alamat", alamat);
                user.put("latitudeLaundry", latitudeLaundry);
                user.put("longitudeLaundry", longitudeLaundry);

                    firestore.collection("laundry_favorit").document(IdLaundry+idUser).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(DetailLaundry.this, "Menambah ke Favorit", Toast.LENGTH_SHORT).show();
//
//                            startActivity(new Intent(DetailLaundry.this, ListLaundry.class));
//                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Task", "Error", e.getCause());
                        }
                    });


            }
        });
        reference = firestore.collection("laundry_favorit").document(IdLaundry+idUser);


        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    btn_hapus.setVisibility(View.VISIBLE);
                    btn_tambah.setVisibility(View.GONE);



                }else{
                    btn_tambah.setVisibility(View.VISIBLE);
                    btn_hapus.setVisibility(View.GONE);

                }
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("laundry_favorit").document(IdLaundry+idUser).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailLaundry.this,"Laundry " + nama_laundry + " Berhasil Dihapus dari favorit", Toast.LENGTH_SHORT).show();

//                        Intent i = new Intent(view.getContext(), Favorit.class);
//                        startActivity(i);
//                        finish();
                        startActivity(new Intent(DetailLaundry.this, MainActivity.class));
                        finish();

                    }
                });
            }
        });



    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMapDetail = googleMap;

         LatLng posisiUser = new LatLng(latUser, longiUser);

        mMapDetail.moveCamera(CameraUpdateFactory.newLatLngZoom(posisiUser,15));
        MarkerOptions options = new MarkerOptions().position(posisiUser).title("Lokasi Saya");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMapDetail.addMarker(options);

        LatLng posisiLaundry = new LatLng(latLaundry, longiLaundry);
        mMapDetail.addMarker(new MarkerOptions().position(posisiLaundry).title(nama_laundry));
        mMapDetail.moveCamera(CameraUpdateFactory.newLatLngZoom(posisiLaundry,15));




    }




}