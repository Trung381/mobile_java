package com.example.tlucontact;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {
    private List<Department> departmentList;
    private List<Department> departmentListFull;
    private Context context;

    public DepartmentAdapter(List<Department> departmentList, Context context) {
        this.departmentList = departmentList;
        this.departmentListFull = new ArrayList<>(departmentList);
        this.context = context;
    }

    /*
    Khi recycler view tao view holder mới để inflate layout cho item sẽ gọi
    tạo một ViewHolder mới
    */
    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
        return new DepartmentViewHolder(view);
    }

    /*

     */
    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        // lấy thằng department tại vị trí đang chọn
        Department department = departmentList.get(position);
        // bind dữ liệu vào view holder để gán dữ liệu vào các view tên, sđt ...
        holder.bind(department);

        /*
        sk click cho cardview
        tạo intent mở department detail
        truyền dữ liệu của department vào intent
        chuyển sang activity department
         */
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DepartmentDetailActivity.class);
                intent.putExtra("NAME", department.getName());
                intent.putExtra("PHONE", department.getPhone());
                intent.putExtra("ADDRESS", department.getAddress());
                intent.putExtra("EMAIL", department.getEmail());
                intent.putExtra("IMAGE", department.getImageResource());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    public void filter(String text) {
        departmentList.clear();
        if (text.isEmpty()) {
            departmentList.addAll(departmentListFull);
        } else {
            text = text.toLowerCase();
            for (Department department : departmentListFull) {
                if (department.getName().toLowerCase().contains(text) ||
                        department.getPhone().contains(text) ||
                        department.getEmail().toLowerCase().contains(text)) {
                    departmentList.add(department);
                }
            }
        }
        notifyDataSetChanged();
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
            imageView.setImageResource(department.getImageResource());
        }
    }
}

