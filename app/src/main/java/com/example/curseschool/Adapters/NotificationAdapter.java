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
import com.example.curseschool.NewObjectsHandlers.NewCourseLanguageHandler;
import com.example.curseschool.NewObjectsHandlers.NewNotificationHandler;
import com.example.curseschool.Notification.UserNotification;
import com.example.curseschool.Objects.CourseLanguage;
import com.example.curseschool.Objects.Notification;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;
import com.example.curseschool.UserUtils.UserUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private Context context;
    private ArrayList<Notification> notifications;
    private UserNotification userNotification;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationArrayList, UserNotification notification) {
        this.context = context;
        this.notifications = notificationArrayList;
        this.userNotification = notification;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_layout,
                parent, false);

        return new NotificationAdapter.NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.setDetails(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public Context getContext() {
        return context;
    }

    public void setNotifications(ArrayList<Notification> notificationArrayList) {
        this.notifications = notificationArrayList;
        notifyDataSetChanged();
    }

    public void deleteNotification(int id){
        Notification notification = notifications.get(id);
        archiveItem(notification.getId());
        notifications.remove(id);
        notifyItemRemoved(id);
    }

    private void archiveItem(int id){
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "UPDATE notification SET archival = 1 WHERE id = " + id;
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

    public void editNotification(int id){
        Notification notification = notifications.get(id);
        Bundle bundle = new Bundle();
        bundle.putInt("id", notification.getId());
        bundle.putString("description", notification.getDescription());
        NewNotificationHandler handler = new NewNotificationHandler();
        handler.setArguments(bundle);
        handler.show(userNotification.getSupportFragmentManager(), NewNotificationHandler.TAG);

    }

    class NotificationHolder extends RecyclerView.ViewHolder {

        private TextView description;
        private TextView info;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.notificationDescription);
            info = itemView.findViewById(R.id.notificationInfo);
        }

        public void setDetails(Notification notification) {
            description.setText(notification.getDescription());
            int userId = notification.getUserId();
            Date date = notification.getCreationDate();
            User user = UserUtils.getUserById(userId);
            String userName = user.getFirstName() + " " + user.getLastName();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
            String dateStr = dateFormat.format(date);
            info.setText(dateStr + ", " + userName);
        }

    }
}
