package com.example.laundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Register extends AppCompatActivity {
    TextView txt_login,mFullName,mEmail,mPassword;
    ImageButton btn_daftar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_login = findViewById(R.id.txtLogin);
        btn_daftar = findViewById(R.id.btn_daftar);
        mFullName   = findViewById(R.id.txtNamaRegister);
        mEmail      = findViewById(R.id.txtEmailRegister);
        mPassword   = findViewById(R.id.txtPassRegister);
//        fAuth = FirebaseAuth.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this , Login.class));
                finish();
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Tidak Boleh Kosong");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password Tidak Boleh Kosong");
                    return;
                }
                if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Nama Tidak Boleh Kosong");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Harus lebih dari 6 karakter");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Silahankan Cek Email Anda Untuk Verifikasi", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Error: Email tidak Terkirim " + e.getMessage());
                                }
                            });

                            Toast.makeText(Register.this, "Perdaftaran Berhasil", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("idUser",userID);
                            user.put("password",password);
                            user.put("alamat","");
                            user.put("telepon","");
                            mFullName.setText("");
                            mEmail.setText("");
                            mPassword.setText("");

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Login.class));
                            finish();
                        }else {
                            Toast.makeText(Register.this, "Error : Email Sudah Terdaftar", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });
    }
}