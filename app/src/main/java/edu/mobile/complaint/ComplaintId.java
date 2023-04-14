package edu.mobile.complaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class ComplaintId extends AppCompatActivity {

    FrameLayout backButton;
    TextView viewComplaintId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_id);

        backButton = findViewById(R.id.onBackHomePress);
        viewComplaintId = findViewById(R.id.textComplaintId);

        LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.playAnimation();

        backButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        viewComplaintId.setText(getIntent().getStringExtra("Complaint_ID"));

    }
}