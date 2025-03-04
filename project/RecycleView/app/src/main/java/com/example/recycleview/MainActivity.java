package com.example.recycleview;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvProject;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Project[] projects = {
                new Project("name 1", "des 1", R.drawable.anh1),
                new Project("name 2", "des 2", R.drawable.anh2),
                new Project("name 3", "des 3", R.drawable.anh3),
                new Project("name 4", "des 4", R.drawable.anh4),
                new Project("name 5", "des 5", R.drawable.anh5),
                new Project("name 6", "des 6", R.drawable.anh6),
                new Project("name 7", "des 1", R.drawable.anh1),
                new Project("name 8", "des 2", R.drawable.anh2),
                new Project("name 9", "des 3", R.drawable.anh3),
                new Project("name 10", "des 4", R.drawable.anh4),
                new Project("name 11", "des 5", R.drawable.anh5),
                new Project("name 12", "des 6", R.drawable.anh6)
        };
        rcvProject = findViewById(R.id.rcv_projects);
//        GridLayoutManager grid = new GridLayoutManager(this);
//        ProjectAdapter projectAdapter = new ProjectAdapter(projects);
//        rcvProject.setAdapter(projectAdapter);
//        rcvProject = findViewById(R.id.rcv_projects);
//        GridLayoutManager grid = new GridLayoutManager(this, 1); // Sửa lỗi ở đây, thêm số cột
        rcvProject.setLayoutManager(new LinearLayoutManager(this));
        ProjectAdapter projectAdapter = new ProjectAdapter(projects);
        rcvProject.setAdapter(projectAdapter);
    }
}