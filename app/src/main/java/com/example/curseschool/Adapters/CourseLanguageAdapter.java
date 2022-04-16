package com.example.curseschool.Adapters;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.CourseLanguage;
import com.example.curseschool.Dictionaries.CourseLanguagesDictionary;
import com.example.curseschool.NewObjectsHandlers.NewCourseLanguageHandler;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseLanguageAdapter extends RecyclerView.Adapter<CourseLanguageAdapter.LanguageHolder> {

    private Context context;
    private ArrayList<CourseLanguage> courseLanguages;
    private CourseLanguagesDictionary courseLanguagesDictionary;

    public CourseLanguageAdapter(Context context, ArrayList<CourseLanguage> courseLanguages, CourseLanguagesDictionary courseLanguagesDictionary) {
        this.context = context;
        this.courseLanguages = courseLanguages;
        this.courseLanguagesDictionary = courseLanguagesDictionary;
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

    public Context getContext() {
        return context;
    }

    public void setCourseLanguages(ArrayList<CourseLanguage> courseLanguageArrayList) {
        this.courseLanguages = courseLanguageArrayList;
        notifyDataSetChanged();
    }

    public void deleteLanguage(int id){
        CourseLanguage language = courseLanguages.get(id);
        deleteItem(language.getId());
        courseLanguages.remove(id);
        notifyItemRemoved(id);
    }

    private void deleteItem(int id){
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "DELETE FROM course_languages WHERE id = " + id;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                connect.close();
            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    public void editLanguage(int id){
        CourseLanguage language = courseLanguages.get(id);
        Bundle bundle = new Bundle();
        bundle.putInt("id", language.getId());
        bundle.putString("language", language.getLanguage());
        NewCourseLanguageHandler handler = new NewCourseLanguageHandler();
        handler.setArguments(bundle);
        handler.show(courseLanguagesDictionary.getSupportFragmentManager(), NewCourseLanguageHandler.TAG);

    }

    class LanguageHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public LanguageHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.languageName);
        }

       public void setDetails(CourseLanguage courseLanguage) {
            name.setText(courseLanguage.getLanguage());
        }

    }
}
