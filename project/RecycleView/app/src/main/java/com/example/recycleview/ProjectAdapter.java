package com.example.recycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {


    private Project[] projects;

    public ProjectAdapter(Project[] projects) {
        this.projects = projects;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
        // tieem bo cuc da dinh nghia de map voi class chiuj trach nghiem dieu khien no
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.blind(projects[position]);
    }

    @Override
    public int getItemCount() {
        return projects.length;
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView description;
        public ProjectViewHolder(@NonNull View itemView){
            super(itemView);
            imageView= itemView.findViewById(R.id.img_icon);
            title = itemView.findViewById(R.id.txt_view_title);
            description = itemView.findViewById(R.id.txt_view_test);
        }

        public void blind(Project project) {
            title.setText(project.getName());
            description.setText(project.getDescription());
            imageView.setImageResource(project.getImage());
        }
    }
}
