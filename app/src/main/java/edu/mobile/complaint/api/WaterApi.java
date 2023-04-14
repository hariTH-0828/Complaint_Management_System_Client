package edu.mobile.complaint.api;

import java.util.List;

import edu.mobile.complaint.model.Water;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WaterApi {

    @POST("/api/water/create")
    Call<Water> createComplaint(@Body Water water);
}
