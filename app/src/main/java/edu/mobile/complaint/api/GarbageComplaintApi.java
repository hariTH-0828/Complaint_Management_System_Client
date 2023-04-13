package edu.mobile.complaint.api;

import java.util.List;

import edu.mobile.complaint.model.GarbageComplaint;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GarbageComplaintApi {

    @GET("/api/garbage/complaint/getAll")
    Call<List<GarbageComplaint>> getAllComplaints();
}
