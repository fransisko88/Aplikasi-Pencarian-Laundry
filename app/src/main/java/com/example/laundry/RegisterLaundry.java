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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterLaundry extends AppCompatActivity implements LocationListener{
    ImageButton btn_back,btn_daftar;
    TextInputEditText nama_laundry,telepon,jam_buka,jam_tutup,informasi,alamat;
    Spinner spinner;
    Button btn_alamat;
    CircleImageView profile_image;
    TextView latitude,longitude;
    public  String jenis_jasa_laundry,documentID;
    StorageReference storageReference;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;
    ProgressBar progressBar;
    LocationManager locationManager;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_laundry);
        btn_back = findViewById(R.id.btn_back_LaundryR);
        btn_daftar = findViewById(R.id.btn_register_laundry);
        nama_laundry = findViewById(R.id.txtNamaLaundryRegister);
        spinner = findViewById(R.id.jenis);
        telepon = findViewById(R.id.txtNoLaundryRegister);
        jam_buka = findViewById(R.id.txtJamBuka);
        jam_tutup = findViewById(R.id.txtJamTutup);
        informasi = findViewById(R.id.txtInformasiLaundry);
        telepon = findViewById(R.id.txtNoLaundryRegister);
        btn_alamat = findViewById(R.id.btnAlamat);
        alamat = findViewById(R.id.txtAlamatLaundry);
        latitude = findViewById(R.id.latitudeLaundry);
        longitude  = findViewById(R.id.longitudeLaundry);
        profile_image = findViewById(R.id.foto_laundry);
        progressBar = findViewById(R.id.progress_circular_RL);

        storageReference = FirebaseStorage.getInstance().getReference();


        fAuth = FirebaseAuth.getInstance();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jenis_laundry, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//
//        spinner.setOnItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jenis_jasa_laundry = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(RegisterLaundry.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RegisterLaundry.this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
                }else{
                    getLocation();

                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.ACTION_PICK, true);
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterLaundry.this , Laundry.class));
                finish();
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nama_laundry.getText().toString().isEmpty() || telepon.getText().toString().isEmpty() || jam_buka.getText().toString().isEmpty() || jam_tutup.getText().toString().isEmpty() || alamat.getText().toString().isEmpty() || informasi.getText().toString().isEmpty()){
                    Toast.makeText(RegisterLaundry.this, "Periksa Data ! Data Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                    return;
                }else if(spinner.equals("---Pilih---")){
                    Toast.makeText(RegisterLaundry.this, "Pilih jenis laundry !", Toast.LENGTH_SHORT).show();
                }else {

                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    HashMap<String,Object> user=new HashMap<>();
                    documentID = firestore.collection("laundry").document().getId();
                    user.put("IdLaundry",documentID);
                    user.put("nama_laundry",nama_laundry.getText().toString());
                    user.put("jenis",jenis_jasa_laundry);
                    user.put("telepon",telepon.getText().toString());
                    user.put("jamBuka",jam_buka.getText().toString());
                    user.put("jamTutup",jam_tutup.getText().toString());
                    user.put("informasi",informasi.getText().toString());
                    user.put("alamat",alamat.getText().toString());
                    user.put("jarak","0");
                    user.put("latitudeLaundry",latitude.getText().toString());
                    user.put("longitudeLaundry",longitude.getText().toString());
                    //user.put("Role","onProses");




                    firestore.collection("laundry").document(documentID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterLaundry.this,"Berhasil ditambah !", Toast.LENGTH_SHORT).show();

                            StorageReference filepath = storageReference.child("laundry/"+documentID+"/profile.jpg");
                            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

                            nama_laundry.setText("");
                            telepon.setText("");
                            jam_buka.setText("");
                            jam_tutup.setText("");
                            informasi.setText("");

                            startActivity(new Intent(RegisterLaundry.this,Laundry.class));
                            finish();
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Task","Error",e.getCause());
                        }
                    });

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            profile_image.setImageURI(mImageUri);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {

            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, (LocationListener) RegisterLaundry.this);

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Geocoder geocoder = new Geocoder(RegisterLaundry.this, Locale.getDefault());
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
        alamat.setText(address);
        latitude.setText(latitudeUser);
        longitude.setText(longitudeUser);
        progressBar.setVisibility(View.GONE);
    }


}