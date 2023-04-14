package edu.mobile.complaint.api;

import java.util.List;

import edu.mobile.complaint.model.GarbageComplaintType;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GarbageComplaintApi {

    @GET("/api/garbage/complaint/getAll")
    Call<List<GarbageComplaintType>> getAllComplaints();
}
