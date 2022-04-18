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

import com.example.curseschool.CourseView.EditCourseDatesForm;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.NewObjectsHandlers.NewCourseDateHandler;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CourseDatesAdapter extends RecyclerView.Adapter<CourseDatesAdapter.CourseDateHolder> {

    private Context context;
    private ArrayList<CourseDate> courseDates;
    private EditCourseDatesForm editCourseDatesForm;

    public CourseDatesAdapter(Context context, ArrayList<CourseDate> courseDates, EditCourseDatesForm courseDatesForm) {
        this.context = context;
        this.courseDates = courseDates;
        this.editCourseDatesForm = courseDatesForm;
    }

    @NonNull
    @Override
    public CourseDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_date_layout,
                parent, false);

        return new CourseDateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseDateHolder holder, int position) {
        CourseDate courseDate = courseDates.get(position);
        holder.setDetails(courseDate);
    }

    @Override
    public int getItemCount() {
        return courseDates.size();
    }

    public Context getContext() {
        return context;
    }

    public void setCourseDates(ArrayList<CourseDate> courseDateArrayList) {
        this.courseDates = courseDateArrayList;
        notifyDataSetChanged();
    }

    public void deleteCourseDate(int id) {
        CourseDate courseDate = courseDates.get(id);
        delete(courseDate.getId());
        courseDates.remove(id);
        notifyItemRemoved(id);
    }

    private void delete(int id) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "DELETE FROM class_room WHERE id = " + id;
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

    public void editCourseDate(int id) {
        CourseDate courseDate = courseDates.get(id);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String date = courseDate.getCourseDate().toString();
        String start = timeFormat.format(courseDate.getCourseTimeStart());
        String end = timeFormat.format(courseDate.getCourseTimeEnd());
        Bundle bundle = new Bundle();
        bundle.putInt("id", courseDate.getId());
        bundle.putString("courseDate", date);
        bundle.putString("startTime", start);
        bundle.putString("endTime", end);
        NewCourseDateHandler handler = new NewCourseDateHandler();
        handler.setArguments(bundle);
        handler.show(editCourseDatesForm.getSupportFragmentManager(), NewCourseDateHandler.TAG);

    }

    class CourseDateHolder extends RecyclerView.ViewHolder {
        private TextView courseDate;
        private TextView timeStart;
        private TextView timeEnd;

        public CourseDateHolder(@NonNull View itemView) {
            super(itemView);
            this.courseDate = itemView.findViewById(R.id.editCourseFormDate);
            this.timeStart = itemView.findViewById(R.id.editCourseFormTimeStart);
            this.timeEnd = itemView.findViewById(R.id.editCourseFormTimeEnd);
        }

        public void setDetails(CourseDate courseDate) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String date = courseDate.getCourseDate().toString();
            String start = timeFormat.format(courseDate.getCourseTimeStart());
            String end = timeFormat.format(courseDate.getCourseTimeEnd());
            this.courseDate.setText(date);
            this.timeStart.setText(start);
            this.timeEnd.setText(end);
        }
    }

}