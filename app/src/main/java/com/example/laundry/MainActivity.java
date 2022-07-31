package com.example.laundry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ImageButton btn_keluar,btn_carilaundry,btn_profil,btn_tentang,btn_admin;
    TextView txtNamaUser;
    FirebaseAuth fAuth;
    ArrayList<ModelLaundry> LaundryArraylist;
    public String id_user,namaUser,emailUser,passwordUser,teleponUser,alamatUser,latitudeUser,longitudeUser;

    CircleImageView profil_image;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    FirebaseFirestore fStore,firestore;
    ModelLaundry dataModel;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_keluar = findViewById(R.id.btn_keluar);
        btn_tentang = findViewById(R.id.btn_tentang);
        btn_profil = findViewById(R.id.btn_profil);
        fAuth = FirebaseAuth.getInstance();
        btn_admin=findViewById(R.id.btn_ADMIN);
        txtNamaUser=findViewById(R.id.txtNamaUser);
        btn_carilaundry=findViewById(R.id.btn_cari);
        profil_image=findViewById(R.id.profile_image);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        },2000);

        if(fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        DocumentReference reference;
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String currentid = user.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profil_image);
//                progressDialog.dismiss();
            }
        });

        reference = firestore.collection("users").document(user.getUid());
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    id_user = task.getResult().getString("idUser");
                    namaUser = task.getResult().getString("fName");
                    emailUser = task.getResult().getString("email");
                    passwordUser = task.getResult().getString("password");
                    teleponUser = task.getResult().getString("telepon");
                    alamatUser = task.getResult().getString("alamat");
                    latitudeUser = task.getResult().getString("latitude");
                    longitudeUser = task.getResult().getString("longitude");
                    txtNamaUser.setText(namaUser);

                    if(emailUser.equals("fransiskosihombing@gmail.com")) {

                        btn_admin.setVisibility(View.VISIBLE);
                        btn_carilaundry.setVisibility(View.GONE);
                    }else{

                        btn_admin.setVisibility(View.GONE);
                        btn_carilaundry.setVisibility(View.VISIBLE);
                    }

                }else{
                    Toast.makeText(MainActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }
            }
        });

        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();//logout
                Toast.makeText(MainActivity.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , Admin.class));

            }
        });

        btn_tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , tentang.class));

            }
        });

        btn_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ProfilUser.class);
                i.putExtra("fName", namaUser);
                i.putExtra("email", emailUser);
                i.putExtra("password", passwordUser);
                i.putExtra("telepon", teleponUser);
                i.putExtra("alamat", alamatUser);
                i.putExtra("latitude", latitudeUser);
                i.putExtra("longitude", longitudeUser);
                i.putExtra("idUser", id_user);

                startActivity(i);
            }
        });

        btn_carilaundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (alamatUser.isEmpty()) {
                    Toast.makeText(MainActivity.this, "lengkapi Alamat Anda", Toast.LENGTH_SHORT).show();
                }else{


                Intent i = new Intent(view.getContext(), ListLaundry.class);
                i.putExtra("fName", namaUser);
                i.putExtra("idUser", id_user);
                i.putExtra("email", emailUser);
                i.putExtra("password", passwordUser);
                i.putExtra("telepon", teleponUser);
                i.putExtra("alamat", alamatUser);
                i.putExtra("latitude", latitudeUser);
                i.putExtra("longitude", longitudeUser);

//                firestore.collection("laundry").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            dataModel = new ModelLaundry();
//                            String id = dc.getDocument().getString("IdLaundry");
//                            String latLa = dc.getDocument().getString("latitudeLaundry");
//                            String longLa = dc.getDocument().getString("longitudeLaundry");
//
//
//                            double lat2 = Double.parseDouble(latitudeUser);
//                            double lon2 = Double.parseDouble(longitudeUser);
//                            double lat1 = Double.parseDouble(latLa);
//                            double lon1 = Double.parseDouble(longLa);
//
//                            Double pi = 3.14159265358979;
//                            Double R = 6371e3;
//
//                            Double latRad1 = lat1 * (pi / 180);
//                            Double latRad2 = lat2 * (pi / 180);
//                            Double deltaLatRad = (lat2 - lat1) * (pi / 180);
//                            Double deltaLonRad = (lon2 - lon1) * (pi / 180);
//
//                            /* RUMUS HAVERSINE */
//                            Double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) + Math.cos(latRad1) * Math.cos(latRad2) * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);
//                            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//                            Double s = (R * c) / 1000; // hasil jarak dalam meter
//
//                            DecimalFormat df = new DecimalFormat("#.##");
//
//                            String hasil = String.valueOf(df.format(s));
//
//                            //firestore.collection("laundry").document(id).update("jarak", hasil);
//
//
//                            i.putExtra("jarak_lokasi", hasil);
//
//
////                            LaundryArraylist = new ArrayList<ModelLaundry>();
////                            LaundryArraylist.add(dc.getDocument().toObject(ModelLaundry.class));
//
//                        }
//                    }
//                });

                startActivity(i);

            }
            }
        });

    }
}