package com.example.curseschool;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseLanguageAdapter extends RecyclerView.Adapter<CourseLanguageAdapter.LanguageHolder> {

    private Context context;
    private ArrayList<CourseLanguage> courseLanguages;

    public CourseLanguageAdapter(Context context, ArrayList<CourseLanguage> courseLanguages) {
        this.context = context;
        this.courseLanguages = courseLanguages;
    }

    @NonNull
    @Override
    public LanguageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.language_dictionary_layout,
                parent, false);

        return new LanguageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseLanguageAdapter.LanguageHolder holder, int position) {
        CourseLanguage courseLanguage = courseLanguages.get(position);
        holder.setDetails(courseLanguage);
    }

    @Override
    public int getItemCount() {
        return courseLanguages.size();
    }

    public void setCourseLanguages(ArrayList<CourseLanguage> courseLanguageArrayList) {
        this.courseLanguages = courseLanguageArrayList;
        notifyDataSetChanged();
    }

    class LanguageHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public LanguageHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.languageName);
        }

        void setDetails(CourseLanguage courseLanguage) {
            name.setText(courseLanguage.getLanguage());
        }
    }
}
