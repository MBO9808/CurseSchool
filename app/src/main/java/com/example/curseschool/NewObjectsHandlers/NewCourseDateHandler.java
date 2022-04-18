package com.example.curseschool.NewObjectsHandlers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.curseschool.DialogHandlers.CourseDateDialogCloseHandler;
import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewCourseDateHandler extends BottomSheetDialogFragment {

    public static final String TAG = "NewCourseDateDialog";
    private TextView courseDate;
    private TextView courseTimeFrom;
    private TextView courseTimeTo;
    private Button saveButton;
    private String connectionResult = "";
    private DatePickerDialog.OnDateSetListener setListenerOnDate;
    private int courseTimeFromHour, courseTimeFromMinute, courseTimeToHour, courseTimeToMinute;

    public static NewCourseDateHandler newInstance() {
        return new NewCourseDateHandler();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.NewLanguageDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_course_date, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseDate = requireView().findViewById(R.id.textAddNewCourseDate);
        createDatePickers();
        courseTimeFrom = requireView().findViewById(R.id.textAddNewTimeStart);
        courseTimeTo = requireView().findViewById(R.id.textAddNewTimeEnd);
        createTimePickers();
        saveButton = requireView().findViewById(R.id.saveNewCourseDate);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            String date = bundle.getString("courseDate");
            String timeFrom = bundle.getString("startTime");
            String timeTo = bundle.getString("endTime");
            courseDate.setText(date);
            courseTimeFrom.setText(timeFrom);
            courseTimeTo.setText(timeTo);
            if (date != null && timeFrom != null && timeTo != null && !date.equals("") && !timeFrom.equals("") && !timeTo.equals(""))
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
        }

        courseDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        courseTimeFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        courseTimeTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean updated = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = courseDate.getText().toString();
                String timeFrom = courseTimeFrom.getText().toString();
                String timeTo = courseTimeTo.getText().toString();
                if (date != null && timeFrom != null && timeTo != null && !date.equals("") && !timeFrom.equals("") && !timeTo.equals("")) {
                    if (updated) {
                        int id = bundle.getInt("id");
                        updateCourseDate(date, timeFrom, timeTo, id);
                        dismiss();
                    } else {
                        int id = findMaxId();
                        addNewCourseDate(date, timeFrom, timeTo, id);
                        dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "Proszę uzupełnić wszystkie pola", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void createDatePickers() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        createDatePicker(year, month, day);
    }

    private void createDatePicker(int year, int month, int day) {
        courseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, setListenerOnDate, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListenerOnDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month += 1;
                String monthTxt = String.valueOf(month);
                if (month < 10) {
                    monthTxt = "0" + month;
                }

                String dayOfMonthTxt = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    dayOfMonthTxt = "0" + dayOfMonthTxt;
                }

                String date = year + "-" + monthTxt + "-" + dayOfMonthTxt;
                courseDate.setText(date);
            }
        };
    }

    private void createTimePickers() {
        courseTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        courseTimeFromHour = hour;
                        courseTimeFromMinute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, courseTimeFromHour, courseTimeFromMinute);
                        courseTimeFrom.setText(DateFormat.format("HH:mm", calendar));
                    }
                }, 12, 0, true
                );
                timePickerDialog.updateTime(courseTimeFromHour, courseTimeFromMinute);
                timePickerDialog.show();
            }
        });

        courseTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        courseTimeToHour = hour;
                        courseTimeToMinute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, courseTimeToHour, courseTimeToMinute);
                        courseTimeTo.setText(DateFormat.format("HH:mm", calendar));
                    }
                }, 12, 0, true
                );
                timePickerDialog.updateTime(courseTimeToHour, courseTimeToMinute);
                timePickerDialog.show();
            }
        });
    }

    private int findMaxId() {
        Integer id = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select MAX(id) from course_dates";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        if (id == null)
            return 1;
        else
            return id + 1;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof CourseDateDialogCloseHandler)
            ((CourseDateDialogCloseHandler) activity).handleDialogClose(dialog);
    }

    private void updateCourseDate(String date, String timeFrom, String timeTo, int id) {
        String query = "UPDATE course_dates SET course_date = '" + date + "', course_time_start = '" + timeFrom + "', course_time_end = '" + timeTo + "' WHERE id = " + id;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                Statement statement = connect.createStatement();
                statement.executeUpdate(query);
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }

    private void addNewCourseDate(String date, String timeFrom, String timeTo, int id) {
        Date courseDate = Date.valueOf(date);
        int courseId = getActivity().getIntent().getIntExtra("courseId", 0);
        String query = "INSERT INTO course_dates (id, course_id, course_date, course_time_start, course_time_end) "
                + " VALUES(?,?,?,?,?)";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, courseId);
                preparedStatement.setDate(3, courseDate);
                preparedStatement.setString(4, timeFrom);
                preparedStatement.setString(5, timeTo);
                preparedStatement.execute();
                connect.close();

            } else {
                connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }
    }
}
