package com.example.tlucontact;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnDepartments, btnStaff;
    private SearchView searchView;
    private Button btnSort;
    private boolean isSortedAscending = true;

    private List<Department> departmentList = new ArrayList<>();
    private List<Staff> staffList = new ArrayList<>();

    private DepartmentAdapter departmentAdapter;
    private StaffAdapter staffAdapter;
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // liên kết biến với các view
        recyclerView = findViewById(R.id.recyclerView);
        btnDepartments = findViewById(R.id.btnDepartments);
        btnStaff = findViewById(R.id.btnStaff);
        searchView = findViewById(R.id.searchView);
        btnSort = findViewById(R.id.btnSort);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo FirestoreManager thay vì DatabaseManager
        firestoreManager = new FirestoreManager();

        // tạo adapter với context chuẩn bị dữ liệu cho recyclerView
        departmentAdapter = new DepartmentAdapter(departmentList, this);
        staffAdapter = new StaffAdapter(staffList, this);

        // set hiển thị mặc định với departmentAdapter
        recyclerView.setAdapter(departmentAdapter);

        // Tải dữ liệu từ Firestore
        loadDepartmentsFromFirestore();

        // hàm click vào để hiển thị department
        btnDepartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(departmentAdapter);
                updateSearchHint("Tìm phòng ban...");
                btnSort.setText(isSortedAscending ? "Z-A" : "A-Z");
            }
        });

        // hàm click vào để hiển thị staff
        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nếu staffList trống, tải dữ liệu từ Firestore
                if (staffList.isEmpty()) {
                    loadStaffFromFirestore();
                }
                
                recyclerView.setAdapter(staffAdapter);
                updateSearchHint("Tìm CBNV...");
                btnSort.setText(isSortedAscending ? "Z-A" : "A-Z");
            }
        });

        // hàm tìm kiếm, gọi đến hàm filter được định nghĩa trong DepartmentAdapter và StaffAdapter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // không cần xử lý, vì dữ liệu được lọc theo mỗi ký tự nhập vào
                // trả về false sẽ thực hiện hành động mặc định của searchView
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (recyclerView.getAdapter() == departmentAdapter) {
                    departmentAdapter.filter(newText);
                } else {
                    staffAdapter.filter(newText);
                }
                return true;
            }
        });

        // xử lý sự kiện click vào nút sort
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSortedAscending = !isSortedAscending;
                if (recyclerView.getAdapter() == departmentAdapter) {
                    departmentAdapter.setSortedList(departmentList, isSortedAscending);
                } else {
                    staffAdapter.setSortedList(staffList, isSortedAscending);
                }
                btnSort.setText(isSortedAscending ? "Z-A" : "A-Z");
            }
        });
    }

    private void loadDepartmentsFromFirestore() {
        firestoreManager.getAllDepartments(new FirestoreManager.OnDepartmentsLoadedListener() {
            @Override
            public void onDepartmentsLoaded(List<Department> departments) {
                departmentList.clear();
                departmentList.addAll(departments);
                departmentAdapter.setSortedList(departmentList, isSortedAscending);
            }

            @Override
            public void onDepartmentsError(Exception e) {
                Toast.makeText(MainActivity.this, "Lỗi khi tải danh sách phòng ban: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStaffFromFirestore() {
        firestoreManager.getAllStaff(new FirestoreManager.OnStaffLoadedListener() {
            @Override
            public void onStaffLoaded(List<Staff> staff) {
                staffList.clear();
                staffList.addAll(staff);
                staffAdapter.setSortedList(staffList, isSortedAscending);
            }

            @Override
            public void onStaffError(Exception e) {
                Toast.makeText(MainActivity.this, "Lỗi khi tải danh sách nhân viên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSearchHint(String hint) {
        searchView.setQueryHint(hint);
    }
}

