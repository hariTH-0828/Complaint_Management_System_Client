package edu.mobile.complaint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import edu.mobile.complaint.api.ElectricityApi;
import edu.mobile.complaint.api.ElectricityComplaintApi;
import edu.mobile.complaint.api.ElectricityConsumerApi;
import edu.mobile.complaint.model.Electricity;
import edu.mobile.complaint.model.ElectricityComplaintType;
import edu.mobile.complaint.model.PriorityLevel;
import edu.mobile.complaint.model.Status;
import edu.mobile.complaint.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElectricityDepartment extends AppCompatActivity {

    AutoCompleteTextView electricityComplaintAdapter;
    TextInputEditText consumerNumber, query;
    Button submit;
    int consumerId;
    RetrofitService retrofitService = new RetrofitService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_department);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_back_light_foreground);

        electricityComplaintAdapter = findViewById(R.id.editElectricityComplaintList);
        consumerNumber = findViewById(R.id.editConsumerNumber);
        query = findViewById(R.id.editQuery);
        submit = findViewById(R.id.submitQuery);

        loadComplaintType();

        submit.setOnClickListener(view -> {
            if(!Objects.requireNonNull(electricityComplaintAdapter.getText()).toString().isEmpty() && !Objects.requireNonNull(consumerNumber.getText()).toString().isEmpty()
                    && !Objects.requireNonNull(electricityComplaintAdapter.getText()).toString().equals("Other")) {

                getConsumerId(consumerNumber.getText().toString());

            } else Toast.makeText(getApplicationContext(), "Empty field not allowed", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadComplaintType() {
        ElectricityComplaintApi electricityComplaintApi = retrofitService.getRetrofit().create(ElectricityComplaintApi.class);

        electricityComplaintApi.getAllComplaintTypes().enqueue(new Callback<List<ElectricityComplaintType>>() {
            @Override
            public void onResponse(@NonNull Call<List<ElectricityComplaintType>> call, @NonNull Response<List<ElectricityComplaintType>> response) {
                if(response.isSuccessful()) {
                    List<ElectricityComplaintType> complaintList = response.body();
                    ArrayAdapter<ElectricityComplaintType> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, complaintList);
                    electricityComplaintAdapter.setAdapter(adapter);
                } else Toast.makeText(ElectricityDepartment.this, "Response fetch failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<List<ElectricityComplaintType>> call, @NonNull Throwable t) {
                Toast.makeText(ElectricityDepartment.this, "Server connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createComplaint(int id) {
        ElectricityApi electricityApi = retrofitService.getRetrofit().create(ElectricityApi.class);

        Electricity electricity = new Electricity();
        electricity.setIssue(electricityComplaintAdapter.getText().toString());
        electricity.setDate(getDate());
        electricity.setDescription(Objects.requireNonNull(query.getText()).toString());
        electricity.setPriorityLevel(PriorityLevel.LOW.toString());
        electricity.setStatus(Status.SUBMITTED.toString());
        electricity.setConsumerId(id);

        electricityApi.createConsumerComplaint(electricity).enqueue(new Callback<Electricity>() {
            @Override
            public void onResponse(@NonNull Call<Electricity> call, @NonNull Response<Electricity> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Create complaint successful", Toast.LENGTH_SHORT).show();
                    resetPage();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Electricity> call,@NonNull Throwable t) {
                Toast.makeText(ElectricityDepartment.this, "Complaint failed to create", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPage() {
        consumerNumber.setText("");
        electricityComplaintAdapter.clearListSelection();
        query.setText("");
    }

    private void getConsumerId(String consumer_number) {
        ElectricityConsumerApi electricityConsumerApi = retrofitService.getRetrofit().create(ElectricityConsumerApi.class);
        electricityConsumerApi.getConsumerId(consumer_number).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call,@NonNull Response<Integer> response) {
                if(response.isSuccessful()) {
                    consumerId = Objects.requireNonNull(response.body());
                    createComplaint(consumerId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call,@NonNull Throwable t) {
                Toast.makeText(ElectricityDepartment.this, "Consumer number is invalid", Toast.LENGTH_SHORT).show();
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

    public String getDate() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);

    }
}