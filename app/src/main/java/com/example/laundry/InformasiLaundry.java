package com.example.laundry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class InformasiLaundry extends AppCompatActivity {
    public String informasi,nama_laundry,jenis,jamBuka,jamTutup,alamat,telepon,IdLaundry,latitudeLaundry,longitudeLaundry;
    TextView DetailId,totalFavorit;
    StorageReference storageReference;
    TextInputEditText DetailTelepon,DetailBuka,DetailTutup,DetailALamat,DetailNama,DetailInformasi;
    CircleImageView profil_image;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    Button btn_ubah;
    ImageButton btn_wa,btn_call,btn_hapus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_laundry);
        DetailNama = findViewById(R.id.UsernameLaundryDetail);
        DetailId = findViewById(R.id.IdLaundryDetail);
        DetailTelepon = findViewById(R.id.teleponDetail);
        DetailBuka = findViewById(R.id.jamBuka_Detail);
        DetailTutup = findViewById(R.id.jamTutup_Detail);
        DetailALamat = findViewById(R.id.Alamat_Detail);
        profil_image = findViewById(R.id.detailFoto_Laundry);
        totalFavorit = findViewById(R.id.banyakFavorit);
        btn_wa = findViewById(R.id.btn_waLaundry);
        btn_call = findViewById(R.id.btn_callLaundry);
        btn_hapus = findViewById(R.id.btn_hapusLaundry);
        btn_ubah = findViewById(R.id.btn_ubah_Laundry);
        DetailInformasi = findViewById(R.id.Informasi_Detail);

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();


        Intent data = getIntent();
        nama_laundry = data.getStringExtra("nama_laundry");
        jenis = data.getStringExtra("jenis");
        jamBuka = data.getStringExtra("jamBuka");
        jamTutup = data.getStringExtra("jamTutup");
        alamat = data.getStringExtra("alamat");
        informasi = data.getStringExtra("informasi");
        telepon = data.getStringExtra("telepon");
        IdLaundry = data.getStringExtra("IdLaundry");
        latitudeLaundry = data.getStringExtra("latitudeLaundry");
        longitudeLaundry = data.getStringExtra("longitudeLaundry");

        StorageReference profileRef = storageReference.child("laundry/"+IdLaundry+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profil_image);
//                progressDialog.dismiss();
            }
        });

        db.collection("laundry_favorit").whereEqualTo("IdLaundry",IdLaundry).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                                String h = Integer.toString(count);
                                totalFavorit.setText(h);



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        DetailNama.setText(nama_laundry);
        DetailId.setText(IdLaundry);
        DetailTelepon.setText("0"+telepon);
        DetailBuka.setText(jamBuka);
        DetailTutup.setText(jamTutup);
        DetailALamat.setText(alamat);
        DetailInformasi.setText(informasi);



        btn_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomor = "+62"+ telepon;
                String pesan = "Hallo "+ nama_laundry + " jasa laundry  ";
                startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s", nomor, pesan)
                                )
                        )
                );
            }
        });

        btn_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> laundry =new HashMap<>();
                laundry.put("nama_laundry",DetailNama.getText().toString());
                laundry.put("jamBuka",DetailBuka.getText().toString());
                laundry.put("jamTutup",DetailTutup.getText().toString());
                laundry.put("alamat",DetailALamat.getText().toString());
                laundry.put("telepon",DetailTelepon.getText().toString());

                db.collection("laundry").document(IdLaundry).update(laundry).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(InformasiLaundry.this,"Laundry Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(InformasiLaundry.this , Laundry.class));
                        finish();
                    }
                });
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
                db.collection("laundry").document(IdLaundry).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(InformasiLaundry.this,"Laundry " + nama_laundry + " Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(InformasiLaundry.this,Laundry.class));
                        finish();
                    }
                });
            }
        });

    }
}