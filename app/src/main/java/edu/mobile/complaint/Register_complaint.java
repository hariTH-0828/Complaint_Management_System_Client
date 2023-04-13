package edu.mobile.complaint;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Register_complaint extends AppCompatActivity {

    View popupView;
    PopupWindow popupWindow;
    String selectedItem = null;
    AutoCompleteTextView departmentAdapter;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int iconId = R.drawable.ic_launcher_back_light_foreground;
        getSupportActionBar().setHomeAsUpIndicator(iconId);

        departmentAdapter = findViewById(R.id.editDepartment);
        submitBtn = findViewById(R.id.submitDepartment);

        String[] department = new String[]{
                "Select Department", "Garbage", "Electrical", "Water"
        };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, department);
        departmentAdapter.setAdapter(adapter);

        departmentAdapter.setOnItemClickListener((adapterView, view, i, l) -> selectedItem = adapterView.getItemAtPosition(i).toString());

        submitBtn.setOnClickListener(view -> {
            if(selectedItem == null) {
                Toast.makeText(this, "Select Department", Toast.LENGTH_SHORT).show();
                return;
            }

            if(selectedItem.equals(department[1])) {
                Intent switchView = new Intent(getApplicationContext(), GarbageDepartment.class);
                startActivity(switchView);
            } else if(selectedItem.equals(department[2])) {
                startActivity(new Intent(getApplicationContext(), GarbageDepartment.class));
            } else if(selectedItem.equals(department[3])) {
                startActivity(new Intent(getApplicationContext(), GarbageDepartment.class));
            } else {
                Toast.makeText(this, "Select Department", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}