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
import com.example.curseschool.Objects.GradeName;
import com.example.curseschool.Dictionaries.GradeNameDictionary;
import com.example.curseschool.NewObjectsHandlers.NewGradeNameHandler;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GradeNameAdapter extends RecyclerView.Adapter<GradeNameAdapter.GradeNameHolder> {

    private Context context;
    private ArrayList<GradeName> gradeNames;
    private GradeNameDictionary gradeNameDictionary;

    public GradeNameAdapter(Context context, ArrayList<GradeName> gradeNames, GradeNameDictionary gradeNameDictionary) {
        this.context = context;
        this.gradeNames = gradeNames;
        this.gradeNameDictionary = gradeNameDictionary;
    }

    @NonNull
    @Override
    public GradeNameAdapter.GradeNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grade_name_dictionary_layout,
                parent, false);

        return new GradeNameAdapter.GradeNameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeNameAdapter.GradeNameHolder holder, int position) {
        GradeName gradeName = gradeNames.get(position);
        holder.setDetails(gradeName);
    }

    @Override
    public int getItemCount() {
        return gradeNames.size();
    }

    public Context getContext() {
        return context;
    }

    public void setGradeNames(ArrayList<GradeName> gradeNameArrayList) {
        this.gradeNames = gradeNameArrayList;
        notifyDataSetChanged();
    }

    public void deleteGradeName(int id) {
        GradeName gradeName = gradeNames.get(id);
        deleteItem(gradeName.getId());
        gradeNames.remove(id);
        notifyItemRemoved(id);
    }

    private void deleteItem(int id) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "DELETE FROM grade_type WHERE id = " + id;
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

    public void editGradeName(int id) {
        GradeName gradeName = gradeNames.get(id);
        Bundle bundle = new Bundle();
        bundle.putInt("id", gradeName.getId());
        bundle.putString("name", gradeName.getName());
        NewGradeNameHandler handler = new NewGradeNameHandler();
        handler.setArguments(bundle);
        handler.show(gradeNameDictionary.getSupportFragmentManager(), NewGradeNameHandler.TAG);

    }

    class GradeNameHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public GradeNameHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.gradeName);
        }

        public void setDetails(GradeName grade) {
            String gradeName = grade.getName();
            this.name.setText(gradeName);
        }
    }
}
