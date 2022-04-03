package com.example.curseschool;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ClassRoomAdapter extends RecyclerView.Adapter<ClassRoomAdapter.ClassRoomHolder> {

    private Context context;
    private ArrayList<ClassRoom> classRooms;
    private ClassRoomDictionary classRoomDictionary;

    public ClassRoomAdapter(Context context, ArrayList<ClassRoom> classRooms, ClassRoomDictionary classRoomDictionary) {
        this.context = context;
        this.classRooms = classRooms;
        this.classRoomDictionary = classRoomDictionary;
    }

    @NonNull
    @Override
    public ClassRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_room_dictionary_layout,
                parent, false);

        return new ClassRoomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassRoomHolder holder, int position) {
        ClassRoom classRoom = classRooms.get(position);
        holder.setDetails(classRoom);
    }

    @Override
    public int getItemCount() {
        return classRooms.size();
    }

    public Context getContext() {
        return context;
    }

    public void setClassRooms(ArrayList<ClassRoom> ClassRoomsArrayList) {
        this.classRooms = ClassRoomsArrayList;
        notifyDataSetChanged();
    }

    public void deleteClassRoom(int id){
        ClassRoom classRoom = classRooms.get(id);
        deleteItem(classRoom.getId());
        classRooms.remove(id);
        notifyItemRemoved(id);
    }

    private void deleteItem(int id){
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

    public void editClassRoom(int id){
        ClassRoom classRoom = classRooms.get(id);
        Bundle bundle = new Bundle();
        bundle.putInt("id", classRoom.getId());
        bundle.putInt("number", classRoom.getClassRoom());
        NewClassRoomHandler handler = new NewClassRoomHandler();
        handler.setArguments(bundle);
        handler.show(classRoomDictionary.getSupportFragmentManager(), NewClassRoomHandler.TAG);

    }

    class ClassRoomHolder extends RecyclerView.ViewHolder {
        private TextView number;

        public ClassRoomHolder(@NonNull View itemView) {
            super(itemView);
            this.number = itemView.findViewById(R.id.classRoomNumber);
        }

        public void setDetails(ClassRoom classRoom) {
            String no = String.valueOf(classRoom.getClassRoom());
            number.setText(no);
        }
    }

}
