package com.example.tlucontact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StaffDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTextView, positionTextView, departmentTextView, phoneTextView, emailTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);

        imageView = findViewById(R.id.detailImageView);
        nameTextView = findViewById(R.id.detailNameTextView);
        positionTextView = findViewById(R.id.detailPositionTextView);
        departmentTextView = findViewById(R.id.detailDepartmentTextView);
        phoneTextView = findViewById(R.id.detailPhoneTextView);
        emailTextView = findViewById(R.id.detailEmailTextView);
        backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String position = intent.getStringExtra("POSITION");
        String department = intent.getStringExtra("DEPARTMENT");
        String phone = intent.getStringExtra("PHONE");
        String email = intent.getStringExtra("EMAIL");
        int imageResource = intent.getIntExtra("IMAGE", R.drawable.img_person);

        nameTextView.setText(name);
        positionTextView.setText(position);
        departmentTextView.setText(department);
        phoneTextView.setText(phone);
        emailTextView.setText(email);
        imageView.setImageResource(imageResource);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

