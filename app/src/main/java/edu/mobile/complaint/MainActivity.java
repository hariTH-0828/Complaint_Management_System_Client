package edu.mobile.complaint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button createBtn, statusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBtn = findViewById(R.id.btn_createComplaint);
        statusBtn = findViewById(R.id.btn_ComplaintStatus);

        createBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Register_complaint.class));
        });

        statusBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Status_complaint.class));
        });
    }
}