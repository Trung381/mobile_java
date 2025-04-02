// DepartmentAdapter.java
package com.example.tlucontact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DepartmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Object> items;
    private List<Department> departmentList;  // Original list, used for filtering
    private Context context;
    private boolean isAscending = true;

    public DepartmentAdapter(List<Department> departmentList, Context context) {
        this.departmentList = departmentList;
        this.context = context;
        this.items = new ArrayList<>();
        setSortedList(departmentList, isAscending); // Initial sort
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSortedList(List<Department> departmentList, boolean ascending) {
        isAscending = ascending;
        items.clear();

        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);

        List<Department> sortedList = new ArrayList<>(departmentList);

        Collections.sort(sortedList, (d1, d2) -> {
            String name1 = (d1.getName() == null || d1.getName().isEmpty()) ? "" : d1.getName();
            String name2 = (d2.getName() == null || d2.getName().isEmpty()) ? "" : d2.getName();

            if (isAscending) {
                return collator.compare(name1, name2);
            } else {
                return collator.compare(name2, name1);
            }
        });

        Map<String, List<Department>> groupedDepartments = new LinkedHashMap<>();
        for (Department department : sortedList) { // Iterate over the sorted list
            String firstLetter = (department.getName() != null && !department.getName().isEmpty())
                    ? department.getName().substring(0, 1).toUpperCase()
                    : "#";

            if (!groupedDepartments.containsKey(firstLetter)) {
                groupedDepartments.put(firstLetter, new ArrayList<>());
            }
            groupedDepartments.get(firstLetter).add(department);
        }

        for (Map.Entry<String, List<Department>> entry : groupedDepartments.entrySet()) {
            items.add(entry.getKey());
            items.addAll(entry.getValue());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position) instanceof String) ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
            return new DepartmentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((String) items.get(position));
        } else if (holder instanceof DepartmentViewHolder) {
            Department department = (Department) items.get(position);
            ((DepartmentViewHolder) holder).bind(department);
            ((DepartmentViewHolder) holder).cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DepartmentDetailActivity.class);
                intent.putExtra("ID", department.getId());
                intent.putExtra("NAME", department.getName());
                intent.putExtra("PHONE", department.getPhone());
                intent.putExtra("ADDRESS", department.getAddress());
                intent.putExtra("EMAIL", department.getEmail());
                intent.putExtra("IMAGE_BASE64", department.getImageBase64());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filter(String text) {
        List<Department> filteredDepartments = new ArrayList<>();
        if (text.isEmpty()) {
            filteredDepartments.addAll(departmentList); // Use the original list
        } else {
            text = text.toLowerCase();
            for (Department department : departmentList) {
                if (department.getName().toLowerCase().contains(text) ||
                        department.getPhone().contains(text) ||
                        department.getEmail().toLowerCase().contains(text)) {
                    filteredDepartments.add(department);
                }
            }
        }
        setSortedList(filteredDepartments, isAscending); // Apply grouping and sorting
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView headerTextView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.header_text);
        }

        public void bind(String header) {
            headerTextView.setText(header);
        }
    }

    static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView phoneTextView;
        private CardView cardView;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bind(Department department) {
            nameTextView.setText(department.getName());
            phoneTextView.setText(department.getPhone());
            
            // Chuyển đổi base64 thành bitmap và hiển thị
            if (department.getImageBase64() != null && !department.getImageBase64().isEmpty()) {
                try {
                    String base64String = department.getImageBase64();
                    
                    // Xử lý trường hợp base64 có data URI prefix
                    if (base64String.contains("base64,")) {
                        base64String = base64String.split("base64,")[1];
                    }
                    
                    byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(decodedBitmap);
                } catch (Exception e) {
                    // Nếu có lỗi, hiển thị ảnh mặc định
                    Log.e("DepartmentAdapter", "Error decoding base64: " + e.getMessage(), e);
                    imageView.setImageResource(R.drawable.img_department);
                }
            } else {
                // Nếu không có ảnh, hiển thị ảnh mặc định
                imageView.setImageResource(R.drawable.img_department);
            }
        }
    }
}