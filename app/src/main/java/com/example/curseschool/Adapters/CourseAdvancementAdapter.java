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
import com.example.curseschool.Objects.CourseAdvancement;
import com.example.curseschool.Dictionaries.CourseAdvancementDictionary;
import com.example.curseschool.NewObjectsHandlers.NewCourseAdvancementHandler;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseAdvancementAdapter extends RecyclerView.Adapter<CourseAdvancementAdapter.CourseAdvancementHolder> {

    private Context context;
    private ArrayList<CourseAdvancement> courseAdvancements;
    private CourseAdvancementDictionary courseAdvancementDictionary;

    public CourseAdvancementAdapter(Context context, ArrayList<CourseAdvancement> courseAdvancements, CourseAdvancementDictionary courseAdvancementDictionary) {
        this.context = context;
        this.courseAdvancements = courseAdvancements;
        this.courseAdvancementDictionary = courseAdvancementDictionary;
    }

    @NonNull
    @Override
    public CourseAdvancementAdapter.CourseAdvancementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_advancement_dictionary_layout,
                parent, false);

        return new CourseAdvancementAdapter.CourseAdvancementHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdvancementAdapter.CourseAdvancementHolder holder, int position) {
        CourseAdvancement courseAdvancement = courseAdvancements.get(position);
        holder.setDetails(courseAdvancement);
    }

    @Override
    public int getItemCount() {
        return courseAdvancements.size();
    }

    public Context getContext() {
        return context;
    }

    public void setCourseAdvancements(ArrayList<CourseAdvancement> courseAdvancementArrayList) {
        this.courseAdvancements = courseAdvancementArrayList;
        notifyDataSetChanged();
    }

    public void deleteCourseAdvancement(int id) {
        CourseAdvancement courseAdvancement = courseAdvancements.get(id);
        deleteItem(courseAdvancement.getId());
        courseAdvancements.remove(id);
        notifyItemRemoved(id);
    }

    private void deleteItem(int id) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "DELETE FROM course_advancement WHERE id = " + id;
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

    public void editCourseAdvancement(int id) {
        CourseAdvancement courseAdvancement = courseAdvancements.get(id);
        Bundle bundle = new Bundle();
        bundle.putInt("id", courseAdvancement.getId());
        bundle.putString("name", courseAdvancement.getName());
        NewCourseAdvancementHandler handler = new NewCourseAdvancementHandler();
        handler.setArguments(bundle);
        handler.show(courseAdvancementDictionary.getSupportFragmentManager(), NewCourseAdvancementHandler.TAG);

    }

    class CourseAdvancementHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public CourseAdvancementHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.courseAdvancementName);
        }

        public void setDetails(CourseAdvancement courseAdvancement) {
            String courseAdvancementName = courseAdvancement.getName();
            name.setText(courseAdvancementName);
        }
    }
}
