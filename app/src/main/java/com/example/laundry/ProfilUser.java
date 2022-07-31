package com.example.laundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilUser extends AppCompatActivity implements LocationListener {

    ImageButton btn_back,btn_edit,btn_edit_foto;
    TextInputEditText editnama,editemail,edithp,edit_alamat;
    CircleImageView profil_image;

    FirebaseAuth fAuth;
    TextView latitude,longitude;
    Button button_lokasi;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    FirebaseUser user;
    LocationManager locationManager;
    StorageReference storageReference;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);
        btn_back = findViewById(R.id.btn_back_profil);

        editnama=findViewById(R.id.txtNamaTeknisi);
        editemail=findViewById(R.id.txtEditemail);
        edit_alamat=findViewById(R.id.edit_tekslokasi);
        profil_image=findViewById(R.id.edit_foto_profil);
        btn_edit=findViewById(R.id.btn_update);
        edithp=findViewById(R.id.txtEditnohp);
        button_lokasi=findViewById(R.id.btn_lokasi);
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        btn_edit_foto = findViewById(R.id.btn_edit_foto);

        progressBar = findViewById(R.id.progress_circular);
        storageReference = FirebaseStorage.getInstance().getReference();


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        Intent data1 = getIntent();
        String hnamaUser = data1.getStringExtra("fName");
        String hIdUser = data1.getStringExtra("idUser");
        String hemail = data1.getStringExtra("email");
        String htelepon = data1.getStringExtra("telepon");
        String halamat = data1.getStringExtra("alamat");
        String hlatitude = data1.getStringExtra("latitude");
        String hlongitude = data1.getStringExtra("longitude");



        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profil_image);
//                progressDialog.dismiss();
            }
        });

        editnama.setText(hnamaUser);
        editemail.setText(hemail);
        editemail.setEnabled(false);
        edithp.setText(htelepon);
        edit_alamat.setText(halamat);
        latitude.setText(hlatitude);
        longitude.setText(hlongitude);




        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilUser.this , MainActivity.class));
                finish();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editnama.getText().toString().isEmpty() || editemail.getText().toString().isEmpty() || edithp.getText().toString().isEmpty() ){
                    Toast.makeText(ProfilUser.this, "Data Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                    return;
                }
//                final String email = hemail;
//                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
//                        Map<String,Object> edited = new HashMap<>();
////                        edited.put("email",email);
//                        edited.put("fName",editnama.getText().toString());
//                        edited.put("telepon",edithp.getText().toString());
//                        edited.put("alamat",edit_alamat.getText().toString());
//                        edited.put("latitude",latitude.getText().toString());
//                        edited.put("longitude",longitude.getText().toString());
//
//                        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
//                        profileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//
//                                    }
//                                });
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(ProfilUser.this, "Profil Berhasil Diubah", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(view.getContext(), MainActivity.class);
////                                i.putExtra("email",hemail);
////                                i.putExtra("fName",editnama.getText().toString());
////                                i.putExtra("telepon",edithp.getText().toString());
//                                startActivity(i);
//                                finish();
//                            }
//                        });
////                        Toast.makeText(EdituserActivity.this, "Email is changed.", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ProfilUser.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

                HashMap<String,Object> edited  =new HashMap<>();

                edited.put("fName",editnama.getText().toString());

                edited.put("telepon",edithp.getText().toString());
                edited.put("alamat",edit_alamat.getText().toString());
                edited.put("latitude",latitude.getText().toString());
                edited.put("longitude",longitude.getText().toString());


                fStore.collection("users").document(hIdUser).update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(ProfilUser.this,"Profil Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfilUser.this , MainActivity.class));
                        finish();
                    }
                });


            }
        });

        btn_edit_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
                profileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(ProfilUser.this, "Foto Profil Berhasil Diubah", Toast.LENGTH_SHORT).show();
            }
        });


        profil_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.ACTION_PICK, true);
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        button_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create method
                progressBar.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(ProfilUser.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfilUser.this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
                }else{
                    getLocation();

                }
            }
        });




    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {

            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,ProfilUser.this);

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Geocoder geocoder = new Geocoder(ProfilUser.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);

        double latitudeU = location.getLatitude();
        double longitudeU = location.getLongitude();
        String latitudeUser = String.valueOf(latitudeU);
        String longitudeUser = String.valueOf(longitudeU);

        latitude.setVisibility(View.VISIBLE);
        longitude.setVisibility(View.VISIBLE);
        edit_alamat.setText(address);
        latitude.setText(latitudeUser);
        longitude.setText(longitudeUser);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            profil_image.setImageURI(mImageUri);
        }


    }

}