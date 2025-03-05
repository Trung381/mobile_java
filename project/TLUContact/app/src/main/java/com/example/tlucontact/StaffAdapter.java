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

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    private List<Staff> staffList;
    private List<Staff> staffListFull;
    private Context context;

    public StaffAdapter(List<Staff> staffList, Context context) {
        this.staffList = staffList;
        this.staffListFull = new ArrayList<>(staffList);
        this.context = context;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        holder.bind(staff);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StaffDetailActivity.class);
                intent.putExtra("NAME", staff.getName());
                intent.putExtra("POSITION", staff.getPosition());
                intent.putExtra("DEPARTMENT", staff.getDepartment());
                intent.putExtra("PHONE", staff.getPhone());
                intent.putExtra("EMAIL", staff.getEmail());
                intent.putExtra("IMAGE", staff.getImageResource());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public void filter(String text) {
        staffList.clear();
        if (text.isEmpty()) {
            staffList.addAll(staffListFull);
        } else {
            text = text.toLowerCase();
            for (Staff staff : staffListFull) {
                if (staff.getName().toLowerCase().contains(text) ||
                        staff.getPosition().toLowerCase().contains(text) ||
                        staff.getDepartment().toLowerCase().contains(text) ||
                        staff.getPhone().contains(text) ||
                        staff.getEmail().toLowerCase().contains(text)) {
                    staffList.add(staff);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView positionTextView;
        private TextView departmentTextView;
        private CardView cardView;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            departmentTextView = itemView.findViewById(R.id.departmentTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bind(Staff staff) {
            nameTextView.setText(staff.getName());
            positionTextView.setText(staff.getPosition());
            departmentTextView.setText(staff.getDepartment());
            imageView.setImageResource(staff.getImageResource());
        }
    }
}

