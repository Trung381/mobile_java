package com.example.tlucontact;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreManager {
    private static final String TAG = "FirestoreManager";
    private final FirebaseFirestore db;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public interface OnDepartmentsLoadedListener {
        void onDepartmentsLoaded(List<Department> departments);
        void onDepartmentsError(Exception e);
    }

    public interface OnStaffLoadedListener {
        void onStaffLoaded(List<Staff> staffList);
        void onStaffError(Exception e);
    }

    public void getAllDepartments(OnDepartmentsLoadedListener listener) {
        db.collection("departments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Department> departments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Department department = document.toObject(Department.class);
                            // Đảm bảo ID được thiết lập đúng
                            department.setId(document.getId());
                            departments.add(department);
                            Log.d(TAG, "Department: " + document.getId() + " => " + document.getData());
                        }
                        listener.onDepartmentsLoaded(departments);
                    } else {
                        Log.w(TAG, "Error getting departments", task.getException());
                        listener.onDepartmentsError(task.getException());
                    }
                });
    }

    public void getAllStaff(OnStaffLoadedListener listener) {
        db.collection("staff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Staff> staffList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Staff staff = document.toObject(Staff.class);
                            // Đảm bảo ID được thiết lập đúng
                            staff.setId(document.getId());
                            staffList.add(staff);
                            Log.d(TAG, "Staff: " + document.getId() + " => " + document.getData());
                        }
                        listener.onStaffLoaded(staffList);
                    } else {
                        Log.w(TAG, "Error getting staff", task.getException());
                        listener.onStaffError(task.getException());
                    }
                });
    }
} 