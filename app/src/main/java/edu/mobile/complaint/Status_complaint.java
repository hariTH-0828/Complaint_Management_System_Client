package edu.mobile.complaint;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import edu.mobile.complaint.api.ElectricityApi;
import edu.mobile.complaint.api.GarbageApi;
import edu.mobile.complaint.api.WaterApi;
import edu.mobile.complaint.model.Electricity;
import edu.mobile.complaint.model.Garbage;
import edu.mobile.complaint.model.Water;
import edu.mobile.complaint.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Status_complaint extends AppCompatActivity {

    AutoCompleteTextView departmentAdapter;
    TextInputEditText textComplaintId;
    View popupView;
    PopupWindow popupWindow;
    Button submit;
    String getComplaintId;
    TextView department, complaintId, name, complaint, date, priority, status, okay;

    RetrofitService retrofitService = new RetrofitService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_complaint);

        departmentAdapter = findViewById(R.id.editDepartment);
        textComplaintId = findViewById(R.id.editComplaintId);
        submit = findViewById(R.id.submitQuery);

        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.playAnimation();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int IconResId = R.drawable.ic_launcher_back_light_foreground;
        getSupportActionBar().setHomeAsUpIndicator(IconResId);

        String[] department = new String[]{
                "Select Department", "Garbage", "Electricity", "Water"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, department);
        departmentAdapter.setAdapter(adapter);
        
        submit.setOnClickListener(view -> {
            if(!departmentAdapter.getText().toString().isEmpty() && !textComplaintId.getText().toString().isEmpty()) {
                getComplaintStatus();
            } else Toast.makeText(this, "Empty field not allowed", Toast.LENGTH_SHORT).show();
        });
    }

    private void getComplaintStatus() {
        if(departmentAdapter.getText().toString().equals("Garbage")) {
            getGarbageComplaintStatus();
        } else if(departmentAdapter.getText().toString().equals("Electricity")) {
            getElectricityComplaintStatus();
        } else if(departmentAdapter.getText().toString().equals("Water")){
            getWaterComplaintStatus();
        } else {
            Toast.makeText(this, "select department", Toast.LENGTH_SHORT).show();
        }
    }

    private void getGarbageComplaintStatus() {
        GarbageApi garbageApi = retrofitService.getRetrofit().create(GarbageApi.class);
        garbageApi.getComplaintStatusBycId(textComplaintId.getText().toString()).enqueue(new Callback<Garbage>() {
            @Override
            public void onResponse(@NonNull Call<Garbage> call, @NonNull Response<Garbage> response) {
                if(response.isSuccessful()) {
                    Garbage garbage = response.body();

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.status_layout, null);

                    department = popupView.findViewById(R.id.textDepartment);
                    complaintId = popupView.findViewById(R.id.textComplaintId);
                    name = popupView.findViewById(R.id.textName);
                    complaint = popupView.findViewById(R.id.textComplaint);
                    date = popupView.findViewById(R.id.textDate);
                    priority = popupView.findViewById(R.id.textPriority);
                    status = popupView.findViewById(R.id.textStatus);

                    department.setText(departmentAdapter.getText().toString());
                    complaintId.setText(textComplaintId.getText().toString());
                    name.setText(Objects.requireNonNull(garbage).getName());
                    complaint.setText(garbage.getProblems());
                    date.setText(garbage.getDate());
                    priority.setText(garbage.getPriorityLevel());
                    status.setText(garbage.getStatus());

                    popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);


                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    });

                }
            }

            @Override
            public void onFailure(@NonNull Call<Garbage> call, @NonNull Throwable t) {
                Toast.makeText(Status_complaint.this, "Invalid Complaint Id", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getElectricityComplaintStatus() {
        ElectricityApi electricityApi = retrofitService.getRetrofit().create(ElectricityApi.class);
        electricityApi.getComplaintStatusBycId(getComplaintId).enqueue(new Callback<Electricity>() {
            @Override
            public void onResponse(@NonNull Call<Electricity> call, @NonNull Response<Electricity> response) {
                if(response.isSuccessful()) {
                    Electricity electricity = response.body();

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.status_layout, null);

                    department = popupView.findViewById(R.id.textDepartment);
                    complaintId = popupView.findViewById(R.id.textComplaintId);
                    complaint = popupView.findViewById(R.id.textComplaint);
                    date = popupView.findViewById(R.id.textDate);
                    priority = popupView.findViewById(R.id.textPriority);
                    status = popupView.findViewById(R.id.textStatus);

                    department.setText(departmentAdapter.getText().toString());
                    complaintId.setText(textComplaintId.getText().toString());
                    complaint.setText(electricity.getIssue());
                    date.setText(electricity.getDate());
                    priority.setText(electricity.getPriorityLevel());
                    status.setText(electricity.getStatus());

                    popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);


                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Electricity> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Invalid Complaint Id", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWaterComplaintStatus() {
        WaterApi waterApi = retrofitService.getRetrofit().create(WaterApi.class);
        waterApi.getComplaintStatusBycId(getComplaintId).enqueue(new Callback<Water>() {
            @Override
            public void onResponse(@NonNull Call<Water> call, @NonNull Response<Water> response) {
                if(response.isSuccessful()) {
                    Water water = response.body();

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    popupView = inflater.inflate(R.layout.status_layout, null);

                    department = popupView.findViewById(R.id.textDepartment);
                    complaintId = popupView.findViewById(R.id.textComplaintId);
                    complaint = popupView.findViewById(R.id.textComplaint);
                    name = popupView.findViewById(R.id.textName);
                    date = popupView.findViewById(R.id.textDate);
                    priority = popupView.findViewById(R.id.textPriority);
                    status = popupView.findViewById(R.id.textStatus);

                    department.setText(departmentAdapter.getText().toString());
                    complaintId.setText(textComplaintId.getText().toString());
                    name.setText(water.getName());
                    complaint.setText(water.getProblems());
                    date.setText(water.getDate());
                    priority.setText(water.getPriorityLevel());
                    status.setText(water.getStatus());

                    popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);


                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Water> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Invalid Complaint Id", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}