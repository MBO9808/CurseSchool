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

import com.example.curseschool.Dictionaries.UsersSettings;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.NewObjectsHandlers.NewUserHandler;
import com.example.curseschool.Objects.User;
import com.example.curseschool.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserSettingHolder> {

    private Context context;
    private ArrayList<User> users;
    private UsersSettings usersSettings;

    public UsersAdapter(Context context, ArrayList<User> userArrayList, UsersSettings UsersView) {
        this.context = context;
        this.users = userArrayList;
        this.usersSettings = UsersView;
    }

    @NonNull
    @Override
    public UsersAdapter.UserSettingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_layout,
                parent, false);

        return new UsersAdapter.UserSettingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserSettingHolder holder, int position) {
        User user = users.get(position);
        holder.setDetails(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public Context getContext() {
        return context;
    }

    public void setUsers(ArrayList<User> userArrayList) {
        this.users = userArrayList;
        notifyDataSetChanged();
    }

    public void deleteUser(int id) {
        User user = users.get(id);
        archiveItem(user.getId());
        users.remove(id);
        notifyItemRemoved(id);
    }

    private void archiveItem(int id) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "UPDATE users SET archival = 1 WHERE id = " + id;
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

    public void editUser(int id) {
        User user = users.get(id);
        String password = getUserPassword(user.getId());
        Bundle bundle = new Bundle();
        bundle.putInt("id", user.getId());
        bundle.putString("forename", user.getFirstName());
        bundle.putString("surname", user.getLastName());
        bundle.putString("email", user.getEmail());
        bundle.putString("password", password);
        bundle.putString("phoneNumber", user.getPhoneNumber());
        bundle.putString("city", user.getCity());
        bundle.putString("street", user.getStreet());
        bundle.putString("postalCode", user.getPostalCode());
        bundle.putString("type", user.getType());
        NewUserHandler handler = new NewUserHandler();
        handler.setArguments(bundle);
        handler.show(usersSettings.getSupportFragmentManager(), NewUserHandler.TAG);

    }

    class UserSettingHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView idn;
        private TextView email;
        private TextView phoneNumber;
        private TextView userType;

        public UserSettingHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.settingsUserName);
            this.idn = itemView.findViewById(R.id.settingsUserIdn);
            this.email = itemView.findViewById(R.id.settingsUserEmail);
            this.phoneNumber = itemView.findViewById(R.id.settingsUserPhone);
            this.userType = itemView.findViewById(R.id.settingsUserType);
        }

        public void setDetails(User user) {
            String userName = user.getFirstName() + " " + user.getLastName();
            String userIdn = user.getIdn();
            String userEmail = user.getEmail();
            String userPhoneNumber = user.getPhoneNumber();
            String userKind = user.getType();
            this.name.setText(userName);
            this.idn.setText(userIdn);
            this.email.setText(userEmail);
            this.phoneNumber.setText(userPhoneNumber);
            this.userType.setText(userKind);
        }
    }

    private String getUserPassword(int id){
        String ps = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select password from users where id = '" + id + "'";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    ps = resultSet.getString(1);
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return ps;
    }
}