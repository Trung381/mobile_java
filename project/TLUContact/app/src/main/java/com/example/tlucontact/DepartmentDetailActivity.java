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
    private ImageButton callButton, emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);

        // Initialize UI components
        imageView = findViewById(R.id.detailImageView);
        nameTextView = findViewById(R.id.detailNameTextView);
        phoneTextView = findViewById(R.id.detailPhoneTextView);
        addressTextView = findViewById(R.id.detailAddressTextView);
        emailTextView = findViewById(R.id.detailEmailTextView);
        backButton = findViewById(R.id.backButton);
        callButton = findViewById(R.id.callButton);
        emailButton = findViewById(R.id.emailButton);

        // Get data from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String phone = intent.getStringExtra("PHONE");
        String address = intent.getStringExtra("ADDRESS");
        String email = intent.getStringExtra("EMAIL");
        int imageResource = intent.getIntExtra("IMAGE", R.drawable.ic_department);

        // Set data to views
        nameTextView.setText(name);
        phoneTextView.setText(phone);
        addressTextView.setText(address);
        emailTextView.setText(email);
        imageView.setImageResource(imageResource);

        // Set up back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up call button
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });

        // Set up email button
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

