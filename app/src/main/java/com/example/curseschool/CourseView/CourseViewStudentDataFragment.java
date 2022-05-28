package com.example.curseschool.CourseView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.MainView.MainSite;
import com.example.curseschool.Objects.Student;
import com.example.curseschool.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CourseViewStudentDataFragment extends Fragment {

    private View view;
    private int courseId;
    private int studentId;
    private Toolbar toolbar;
    private TextView studentName;
    private TextView studentEmail;
    private TextView studentPhoneNumber;
    private TextView studentPaymentStatus;
    private ExtendedFloatingActionButton fabPayment, fabDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_view_student_data, container, false);
        setToolbar();
        initFields();
        courseId = getActivity().getIntent().getIntExtra("courseId",0);
        studentId = getActivity().getIntent().getIntExtra("studentId",0);
        setFieldsValues();
        setFabListeners();
        return view;
    }

    private void setFabListeners(){
        fabPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvePayment();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteStudentFromCurse();
            }
        });
    }

    private void approvePayment(){
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "UPDATE course_students SET payed = 1 WHERE course_id = " + courseId + " and student_id = " + studentId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                connect.close();
            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        studentPaymentStatus.setText("Opłacono");
        fabPayment.setEnabled(false);
        fabPayment.setBackgroundColor(Color.GRAY);
    }

    private void deleteStudentFromCurse(){
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "DELETE course_students WHERE student_id = " + studentId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                connect.close();
            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
        Intent intent = new Intent(getContext(), CourseView.class);
        startActivity(intent);
    }

    private void initFields(){
        studentName = view.findViewById(R.id.studentDataName);
        studentEmail = view.findViewById(R.id.studentDataEmail);
        studentPhoneNumber = view.findViewById(R.id.studentDataPhone);
        studentPaymentStatus = view.findViewById(R.id.studentDataPaymentStatus);
        fabPayment = view.findViewById(R.id.fab_approve_payment);
        fabDelete = view.findViewById(R.id.fab_delete_user);
    }

    private void setToolbar() {
        toolbar = view.findViewById(R.id.include);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Dane studenta");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CourseView.class);
                startActivity(intent);
            }
        });
    }

    private void setFieldsValues(){
        Map<String, String> studentData = getStudentValues();
        String name = studentData.get("name");
        String email = studentData.get("email");
        String phone = studentData.get("phone");
        String payed = studentData.get("payed");
        studentName.setText(name);
        studentEmail.setText(email);
        studentPhoneNumber.setText(phone);
        studentPaymentStatus.setText(payed);
        if(payed.equals("Opłacono")) {
            fabPayment.setEnabled(false);
            fabPayment.setBackgroundColor(Color.GRAY);
        } else {
            fabPayment.setEnabled(true);
        }
    }

    private Map<String, String> getStudentValues(){
        Map<String, String> studentData = new HashMap<>();
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select us.forename, us.surname, us.email, us.phone_number, st.payed from course_students st " +
                        "join users us on us.id = st.student_id where st.course_id =  " + courseId + " and st.student_id = " + studentId;
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String forename = resultSet.getString(1);
                    String surname = resultSet.getString(2);
                    String email = resultSet.getString(3);
                    String phone = resultSet.getString(4);
                    boolean payed = resultSet.getBoolean(5);
                    String name = forename + " " + surname;
                    studentData.put("name", name);
                    studentData.put("email", email);
                    studentData.put("phone", phone);
                    if(payed)
                        studentData.put("payed", "Opłacono");
                    else
                        studentData.put("payed", "Do zapłaty");
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return studentData;
    }
}