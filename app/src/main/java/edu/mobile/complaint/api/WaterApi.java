package edu.mobile.complaint.api;

import java.util.List;

import edu.mobile.complaint.model.Electricity;
import edu.mobile.complaint.model.Water;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WaterApi {

    @POST("/api/water/create")
    Call<Water> createComplaint(@Body Water water);

    @GET("/api/water/id/{complaintId}")
    Call<Water> getComplaintStatusBycId(@Path("complaintId") String id);
}
