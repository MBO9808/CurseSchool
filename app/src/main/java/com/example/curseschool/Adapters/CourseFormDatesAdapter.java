package com.example.curseschool.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curseschool.CourseView.CourseDatesFragment;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.CourseDate;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CourseFormDatesAdapter extends RecyclerView.Adapter<CourseFormDatesAdapter.DatesListHolder> {

    private Context context;
    private ArrayList<CourseDate> courseDates;
    private CourseDatesFragment courseDatesFragment;

    public CourseFormDatesAdapter(Context context, ArrayList<CourseDate> courseDateArrayList, CourseDatesFragment datesFragment) {
        this.context = context;
        this.courseDates = courseDateArrayList;
        this.courseDatesFragment = datesFragment;
    }

    @NonNull
    @Override
    public CourseFormDatesAdapter.DatesListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dates_course_card,
                parent, false);

        return new CourseFormDatesAdapter.DatesListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseFormDatesAdapter.DatesListHolder holder, int position) {
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

    class DatesListHolder extends RecyclerView.ViewHolder {
        private TextView courseDate;
        private TextView timeStart;
        private TextView timeEnd;
        private TextView classRoom;

        public DatesListHolder(@NonNull View itemView) {
            super(itemView);
            this.courseDate = itemView.findViewById(R.id.courseFormDate);
            this.timeStart = itemView.findViewById(R.id.courseFormTimeStart);
            this.timeEnd = itemView.findViewById(R.id.courseFormTimeEnd);
            this.classRoom = itemView.findViewById(R.id.courseFormClass);
        }

        public void setDetails(CourseDate courseDate) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String date = courseDate.getCourseDate().toString();
            String start = timeFormat.format(courseDate.getCourseTimeStart());
            String end = timeFormat.format(courseDate.getCourseTimeEnd());
            this.courseDate.setText(date);
            this.timeStart.setText(start);
            this.timeEnd.setText(end);
            this.classRoom.setText(getCourseClassRoom(courseDate.getClassRoomId()));
        }

        private String getCourseClassRoom(int classRoomId) {
            String classRoom = "";
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                Connection connect = connectionHelper.getConnection();
                if (connect != null) {
                    String query = "Select number from class_room where id = " + classRoomId;
                    Statement statement = connect.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        int room = resultSet.getInt(1);
                        classRoom = String.valueOf(room);
                    }
                    connect.close();

                } else {
                    String connectionResult = "Check Connection";
                }
            } catch (Exception ex) {
                Log.e("Error :", ex.getMessage());
            }
            return classRoom;
        }
    }
}