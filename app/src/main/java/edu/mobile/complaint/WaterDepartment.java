package edu.mobile.complaint;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import edu.mobile.complaint.api.DistrictApi;
import edu.mobile.complaint.api.StatesApi;
import edu.mobile.complaint.api.WaterApi;
import edu.mobile.complaint.api.WaterComplaintTypeApi;
import edu.mobile.complaint.model.District;
import edu.mobile.complaint.model.PriorityLevel;
import edu.mobile.complaint.model.States;
import edu.mobile.complaint.model.Status;
import edu.mobile.complaint.model.Water;
import edu.mobile.complaint.model.WaterComplaintType;
import edu.mobile.complaint.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterDepartment extends AppCompatActivity {

    AutoCompleteTextView waterComplaintAdapter, stateAdapter, districtAdapter;
    Button submit;
    int stateId, districtId;
    TextInputEditText name, query, phoneNumber, address;
    String complaintId;

    RetrofitService retrofitService = new RetrofitService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_department);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_back_light_foreground);

        name = findViewById(R.id.editName);
        query = findViewById(R.id.editQuery);
        phoneNumber = findViewById(R.id.editPhoneNumber);
        address = findViewById(R.id.editAddress);
        waterComplaintAdapter = findViewById(R.id.editWaterComplaintList);
        stateAdapter = findViewById(R.id.editState);
        districtAdapter = findViewById(R.id.editDistrict);
        submit = findViewById(R.id.submitQuery);

        loadWaterComplaintType();
        loadStates();

        stateAdapter.setOnItemClickListener((adapterView, view, i, l) -> {
            States selectedId = (States) adapterView.getItemAtPosition(i);
            stateId = selectedId.getId();
            loadDistrict(selectedId.getId());
        });
        districtAdapter.setOnItemClickListener((adapterView, view, i, l) -> {
            District selectedId = (District) adapterView.getItemAtPosition(i);
            districtId = selectedId.getId();
        });

        submit.setOnClickListener(view -> {
            if(!Objects.requireNonNull(name.getText()).toString().trim().isEmpty() && !Objects.requireNonNull(phoneNumber.getText()).toString().trim().isEmpty()
                    && !Objects.requireNonNull(address.getText()).toString().trim().isEmpty() && !Objects.requireNonNull(waterComplaintAdapter.getText()).toString().isEmpty()
                    && !Objects.requireNonNull(stateAdapter.getText()).toString().trim().isEmpty() && !Objects.requireNonNull(districtAdapter.getText()).toString().trim().isEmpty()
                    && !Objects.requireNonNull(waterComplaintAdapter.getText()).toString().equals("Other")) {

                if(Objects.requireNonNull(phoneNumber.getText()).toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Invalid Phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                complaintId = GeneratorComplaintId.generateId();
                if(!complaintId.isEmpty()) {
                    createComplaint();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Provide all field - Empty field not allowed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createComplaint() {
        WaterApi waterApi = retrofitService.getRetrofit().create(WaterApi.class);

        Water water = new Water();

        water.setName(Objects.requireNonNull(name.getText()).toString());
        water.setProblems(waterComplaintAdapter.getText().toString());
        water.setQuery(Objects.requireNonNull(query.getText()).toString());
        water.setPhoneNumber(Objects.requireNonNull(phoneNumber.getText()).toString());
        water.setAddress(Objects.requireNonNull(address.getText()).toString());
        water.setStateId(stateId);
        water.setDistrictId(districtId);
        water.setDate(getDate());
        water.setStatus(String.valueOf(Status.SUBMITTED));
        water.setPriorityLevel(String.valueOf(PriorityLevel.LOW));
        water.setComplaintId(complaintId);
        waterApi.createComplaint(water).enqueue(new Callback<Water>() {
            @Override
            public void onResponse(@NonNull Call<Water> call,@NonNull Response<Water> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), ComplaintId.class);
                    intent.putExtra("Complaint_ID", complaintId);
                    startActivity(intent);
                }else Toast.makeText(WaterDepartment.this, "Complaint create failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Water> call,@NonNull Throwable t) {
                Toast.makeText(WaterDepartment.this, "Server connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWaterComplaintType() {
        WaterComplaintTypeApi waterComplaintTypeApi = retrofitService.getRetrofit().create(WaterComplaintTypeApi.class);
        waterComplaintTypeApi.getAllComplaintTypes().enqueue(new Callback<List<WaterComplaintType>>() {
            @Override
            public void onResponse(@NonNull Call<List<WaterComplaintType>> call,@NonNull Response<List<WaterComplaintType>> response) {
                if(response.isSuccessful()) {
                    List<WaterComplaintType> waterComplaintTypeList = response.body();
                    ArrayAdapter<WaterComplaintType> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, waterComplaintTypeList);
                    waterComplaintAdapter.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WaterComplaintType>> call,@NonNull Throwable t) {
                Toast.makeText(WaterDepartment.this, "complaint load failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStates() {
        StatesApi statesApi = retrofitService.getRetrofit().create(StatesApi.class);
        statesApi.getAllStates().enqueue(new Callback<List<States>>() {
            @Override
            public void onResponse(@NonNull Call<List<States>> call,@NonNull Response<List<States>> response) {
                if(response.isSuccessful()) {
                    List<States> statesList = response.body();
                    ArrayAdapter<States> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, statesList);
                    stateAdapter.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<States>> call,@NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Server connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDistrict(int stateId) {
        DistrictApi districtApi = retrofitService.getRetrofit().create(DistrictApi.class);
        districtApi.getDistrictByStateId(stateId).enqueue(new Callback<List<District>>() {
            @Override
            public void onResponse(@NonNull Call<List<District>> call,@NonNull Response<List<District>> response) {
                if(response.isSuccessful()) {
                    List<District> districtList = response.body();
                    ArrayAdapter<District> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, districtList);
                    districtAdapter.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<District>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Server connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDate() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(date);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}