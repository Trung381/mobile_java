package com.example.tlucontact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
    private ImageButton callButton, emailButton;

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
        callButton = findViewById(R.id.callButton);
        emailButton = findViewById(R.id.emailButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String position = intent.getStringExtra("POSITION");
        String department = intent.getStringExtra("DEPARTMENT");
        String phone = intent.getStringExtra("PHONE");
        String email = intent.getStringExtra("EMAIL");
        String imageBase64 = intent.getStringExtra("IMAGE_BASE64");

        nameTextView.setText(name);
        positionTextView.setText(position);
        departmentTextView.setText(department);
        phoneTextView.setText(phone);
        emailTextView.setText(email);
        
        // Chuyển đổi base64 thành bitmap và hiển thị
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            try {
                String base64String = imageBase64;
                
                // Xử lý trường hợp base64 có data URI prefix
                if (base64String.contains("base64,")) {
                    base64String = base64String.split("base64,")[1];
                }
                
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedBitmap);
            } catch (Exception e) {
                // Nếu có lỗi, hiển thị ảnh mặc định
                Log.e("StaffDetailActivity", "Error decoding base64: " + e.getMessage(), e);
                imageView.setImageResource(R.drawable.img_person);
            }
        } else {
            // Nếu không có ảnh, hiển thị ảnh mặc định
            imageView.setImageResource(R.drawable.img_person);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email));
                startActivity(emailIntent);
            }
        });
    }
}

