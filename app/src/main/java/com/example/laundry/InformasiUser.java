package com.example.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformasiUser extends AppCompatActivity {
    public String Username,alamat,telepon,IdUser,emailUser;
    TextView DetailUsername,DetailAlamat,DetailIdUser,DetailTelepon,DetailEmail;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    CircleImageView profil_image;
    FirebaseFirestore db;
    ImageButton btn_wa,btn_call,btn_hapus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_user);
        DetailUsername = findViewById(R.id.UsernameDetail);
        DetailAlamat = findViewById(R.id.AlamatDetail);
        DetailIdUser = findViewById(R.id.IdUserDetail);
        DetailTelepon = findViewById(R.id.teleponDetail);
        profil_image = findViewById(R.id.detailFoto);
        btn_wa = findViewById(R.id.btn_wa);
        btn_call = findViewById(R.id.btn_call);
        btn_hapus = findViewById(R.id.btn_hapus);
        DetailEmail = findViewById(R.id.EmailUserDetail);


        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();


        Intent data = getIntent();
        Username = data.getStringExtra("fName");
        alamat = data.getStringExtra("alamat");
        telepon = data.getStringExtra("telepon");
        IdUser = data.getStringExtra("IdUser");
        emailUser = data.getStringExtra("email");

        DetailUsername.setText(Username);
        DetailAlamat.setText(alamat);
        DetailIdUser.setText(IdUser);
        DetailTelepon.setText("0"+telepon);
        DetailEmail.setText(emailUser);


        StorageReference profileRef = storageReference.child("users/"+IdUser+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profil_image);
//                progressDialog.dismiss();
            }
        });

        btn_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomor = "+62"+ telepon;
                String pesan = "Hallo "+ Username + " pengguna aplikasi jasa laundry  ";
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

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users").document(IdUser).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(InformasiUser.this,"User " + Username + " Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(InformasiUser.this,User.class));
                        finish();
                    }
                });
            }
        });

    }
}