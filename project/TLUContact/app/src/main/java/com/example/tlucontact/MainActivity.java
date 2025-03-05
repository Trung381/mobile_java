package com.example.tlucontact;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

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

    // Lists to hold our data
    private List<Department> departmentList;
    private List<Staff> staffList;

    // Adapters
    private DepartmentAdapter departmentAdapter;
    private StaffAdapter staffAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        btnDepartments = findViewById(R.id.btnDepartments);
        btnStaff = findViewById(R.id.btnStaff);
        searchView = findViewById(R.id.searchView);
        btnSort = findViewById(R.id.btnSort);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data
        initializeDepartmentData();
        initializeStaffData();

        // Set up adapters
        departmentAdapter = new DepartmentAdapter(departmentList, this);
        staffAdapter = new StaffAdapter(staffList, this);

        // Set default view to departments
        recyclerView.setAdapter(departmentAdapter);

        // Set up button click listeners
        btnDepartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(departmentAdapter);
                updateSearchHint("Search departments...");
                btnSort.setText("Sort by Name");
            }
        });

        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(staffAdapter);
                updateSearchHint("Search staff...");
                btnSort.setText("Sort by Name");
            }
        });

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

        // Set up sort functionality
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getAdapter() == departmentAdapter) {
                    sortDepartments();
                } else {
                    sortStaff();
                }
            }
        });
    }

    private void updateSearchHint(String hint) {
        searchView.setQueryHint(hint);
    }

    private void sortDepartments() {
        if (isSortedAscending) {
            Collections.sort(departmentList, new Comparator<Department>() {
                @Override
                public int compare(Department d1, Department d2) {
                    return d1.getName().compareTo(d2.getName());
                }
            });
            btnSort.setText("Sort Z-A");
        } else {
            Collections.sort(departmentList, new Comparator<Department>() {
                @Override
                public int compare(Department d1, Department d2) {
                    return d2.getName().compareTo(d1.getName());
                }
            });
            btnSort.setText("Sort A-Z");
        }
        isSortedAscending = !isSortedAscending;
        departmentAdapter.notifyDataSetChanged();
    }

    private void sortStaff() {
        if (isSortedAscending) {
            Collections.sort(staffList, new Comparator<Staff>() {
                @Override
                public int compare(Staff s1, Staff s2) {
                    return s1.getName().compareTo(s2.getName());
                }
            });
            btnSort.setText("Sort Z-A");
        } else {
            Collections.sort(staffList, new Comparator<Staff>() {
                @Override
                public int compare(Staff s1, Staff s2) {
                    return s2.getName().compareTo(s1.getName());
                }
            });
            btnSort.setText("Sort A-Z");
        }
        isSortedAscending = !isSortedAscending;
        staffAdapter.notifyDataSetChanged();
    }

    // Initialize sample department data
    private void initializeDepartmentData() {
        departmentList = new ArrayList<>();

        departmentList.add(new Department(
                "Khoa Công nghệ thông tin",
                "024.38522028",
                "Tầng 2, Nhà C1, Đại học Thủy Lợi, 175 Tây Sơn, Đống Đa, Hà Nội",
                "cntt@tlu.edu.vn",
                R.drawable.img_department
        ));

        departmentList.add(new Department(
                "Khoa Kinh tế và Quản lý",
                "024.35638252",
                "Tầng 6, Nhà A5, Đại học Thủy Lợi, 175 Tây Sơn, Đống Đa, Hà Nội",
                "ktql@tlu.edu.vn",
                R.drawable.img_department2
        ));

        departmentList.add(new Department(
                "Khoa Kỹ thuật xây dựng",
                "024.35632211",
                "Tầng 3, Nhà A1, Đại học Thủy Lợi, 175 Tây Sơn, Đống Đa, Hà Nội",
                "ktxd@tlu.edu.vn",
                R.drawable.img_department
        ));

        departmentList.add(new Department(
                "Phòng Đào tạo",
                "024.35632211",
                "Tầng 1, Nhà A1, Đại học Thủy Lợi, 175 Tây Sơn, Đống Đa, Hà Nội",
                "daotao@tlu.edu.vn",
                R.drawable.img_department
        ));

        departmentList.add(new Department(
                "Phòng Công tác sinh viên",
                "024.35638364",
                "Tầng 1, Nhà A1, Đại học Thủy Lợi, 175 Tây Sơn, Đống Đa, Hà Nội",
                "ctsv@tlu.edu.vn",
                R.drawable.img_department
        ));
    }

    // Initialize sample staff data
    private void initializeStaffData() {
        staffList = new ArrayList<>();

        staffList.add(new Staff(
                "TS. Nguyễn Thanh Tùng",
                "Trưởng khoa",
                "Khoa Công nghệ thông tin",
                "0912345678",
                "tungnt@tlu.edu.vn",
                R.drawable.img_person
        ));

        staffList.add(new Staff(
                "TS. Phạm Thị Hương",
                "Phó Trưởng khoa",
                "Khoa Công nghệ thông tin",
                "0923456789",
                "huongpt@tlu.edu.vn",
                R.drawable.img_person2
        ));

        staffList.add(new Staff(
                "PGS.TS. Trần Văn Nam",
                "Trưởng khoa",
                "Khoa Kinh tế và Quản lý",
                "0934567890",
                "namtv@tlu.edu.vn",
                R.drawable.img_person2
        ));

        staffList.add(new Staff(
                "ThS. Lê Thị Minh",
                "Giảng viên",
                "Khoa Kinh tế và Quản lý",
                "0945678901",
                "minhlt@tlu.edu.vn",
                R.drawable.img_person2
        ));

        staffList.add(new Staff(
                "PGS.TS. Đỗ Văn Hải",
                "Trưởng khoa",
                "Khoa Kỹ thuật xây dựng",
                "0956789012",
                "haidv@tlu.edu.vn",
                R.drawable.img_person2
        ));

        staffList.add(new Staff(
                "TS. Nguyễn Thị Lan",
                "Trưởng phòng",
                "Phòng Đào tạo",
                "0967890123",
                "lannt@tlu.edu.vn",
                R.drawable.img_person2
        ));

        staffList.add(new Staff(
                "ThS. Vũ Đình Tuấn",
                "Trưởng phòng",
                "Phòng Công tác sinh viên",
                "0978901234",
                "tuanvd@tlu.edu.vn",
                R.drawable.img_person2
        ));
    }
}

