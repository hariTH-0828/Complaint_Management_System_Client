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

import edu.mobile.complaint.api.DistrictApi;
import edu.mobile.complaint.api.GarbageApi;
import edu.mobile.complaint.api.GarbageComplaintApi;
import edu.mobile.complaint.api.StatesApi;
import edu.mobile.complaint.model.District;
import edu.mobile.complaint.model.Garbage;
import edu.mobile.complaint.model.GarbageComplaint;
import edu.mobile.complaint.model.PriorityLevel;
import edu.mobile.complaint.model.States;
import edu.mobile.complaint.model.Status;
import edu.mobile.complaint.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GarbageDepartment extends AppCompatActivity {

    AutoCompleteTextView garbageComplaintAdapter, stateAdapter, districtAdapter;
    RetrofitService retrofitService = new RetrofitService();
    Button submit;
    int stateId, districtId;
    TextInputEditText name, query, phoneNumber, address, pinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_department);

        name = findViewById(R.id.editName);
        query = findViewById(R.id.editQuery);
        phoneNumber = findViewById(R.id.editPhoneNumber);
        address = findViewById(R.id.editAddress);
        pinCode = findViewById(R.id.editPinCode);
        garbageComplaintAdapter = findViewById(R.id.editGarbageComplaintList);
        stateAdapter = findViewById(R.id.editState);
        districtAdapter = findViewById(R.id.editDistrict);
        submit = findViewById(R.id.submitQuery);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int IconResId = R.drawable.ic_launcher_back_light_foreground;
        getSupportActionBar().setHomeAsUpIndicator(IconResId);

        loadGarbageComplaint();
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
            && !Objects.requireNonNull(address.getText()).toString().trim().isEmpty() && !Objects.requireNonNull(pinCode.getText()).toString().trim().isEmpty()
            && !Objects.requireNonNull(garbageComplaintAdapter.getText()).toString().isEmpty() && !Objects.requireNonNull(stateAdapter.getText()).toString().trim().isEmpty()
            && !Objects.requireNonNull(districtAdapter.getText()).toString().trim().isEmpty()) {
                
                if(Objects.requireNonNull(phoneNumber.getText()).toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Invalid Phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                createComplaint();
            } else {
                Toast.makeText(getApplicationContext(), "Provide all field - Empty field not allowed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createComplaint() {
        GarbageApi garbageApi = retrofitService.getRetrofit().create(GarbageApi.class);

        Garbage garbage = new Garbage();

        garbage.setName(Objects.requireNonNull(name.getText()).toString());
        garbage.setProblems(garbageComplaintAdapter.getText().toString());
        garbage.setQuery(Objects.requireNonNull(query.getText()).toString());
        garbage.setPhoneNumber(Objects.requireNonNull(phoneNumber.getText()).toString());
        garbage.setAddress(Objects.requireNonNull(address.getText()).toString());
        garbage.setPincode(Objects.requireNonNull(pinCode.getText()).toString());
        garbage.setStateId(stateId);
        garbage.setDistrictId(districtId);
        garbage.setDate(getDate());
        garbage.setStatus(String.valueOf(Status.SUBMITTED));
        garbage.setPriorityLevel(String.valueOf(PriorityLevel.LOW));

        Toast.makeText(this, "Creating Complaint....", Toast.LENGTH_SHORT).show();
        garbageApi.save(garbage).enqueue(new Callback<Garbage>() {
            @Override
            public void onResponse(@NonNull Call<Garbage> call,@NonNull Response<Garbage> response) {
                Garbage responseGarbage = response.body();
                if(responseGarbage != null) {
                    Toast.makeText(GarbageDepartment.this, "Created complaint success", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(GarbageDepartment.this, "Creation Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Garbage> call,@NonNull Throwable t) {
                Toast.makeText(GarbageDepartment.this, "Server connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDate() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(date);
    }
    private void loadGarbageComplaint() {
        GarbageComplaintApi garbageComplaintApi = retrofitService.getRetrofit().create(GarbageComplaintApi.class);
        garbageComplaintApi.getAllComplaints().enqueue(new Callback<List<GarbageComplaint>>() {
            @Override
            public void onResponse(@NonNull Call<List<GarbageComplaint>> call,@NonNull Response<List<GarbageComplaint>> response) {
                if(response.isSuccessful()) {
                    List<GarbageComplaint> garbageComplaints = response.body();
                    ArrayAdapter<GarbageComplaint> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, garbageComplaints);
                    garbageComplaintAdapter.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GarbageComplaint>> call,@NonNull Throwable t) {
                Toast.makeText(GarbageDepartment.this, "Server connection failed", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GarbageDepartment.this, "Server connection failed", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GarbageDepartment.this, "Server connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}