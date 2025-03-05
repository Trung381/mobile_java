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

public class DepartmentDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameTextView, phoneTextView, addressTextView, emailTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);

        imageView = findViewById(R.id.detailImageView);
        nameTextView = findViewById(R.id.detailNameTextView);
        phoneTextView = findViewById(R.id.detailPhoneTextView);
        addressTextView = findViewById(R.id.detailAddressTextView);
        emailTextView = findViewById(R.id.detailEmailTextView);
        backButton = findViewById(R.id.backButton);

        // lấy dữ liệu từ intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String phone = intent.getStringExtra("PHONE");
        String address = intent.getStringExtra("ADDRESS");
        String email = intent.getStringExtra("EMAIL");
        int imageResource = intent.getIntExtra("IMAGE", R.drawable.img_department);

        // đổ dữ liệu vào view
        nameTextView.setText(name);
        phoneTextView.setText(phone);
        addressTextView.setText(address);
        emailTextView.setText(email);
        imageView.setImageResource(imageResource);

        // sự kiện quay trở lại
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

