package com.example.laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Admin extends AppCompatActivity {
    ImageButton btn_back,btn_user,btn_laundry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btn_back = findViewById(R.id.btn_backAdmin);
        btn_user = findViewById(R.id.btn_adminUser);
        btn_laundry =findViewById(R.id.btn_adminLaundry);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this , MainActivity.class));
                finish();
            }
        });

        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this , User.class));
            }
        });

        btn_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this , Laundry.class));
            }
        });

    }
}