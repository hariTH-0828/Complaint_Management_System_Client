package edu.mobile.complaint.api;


import java.util.List;

import edu.mobile.complaint.model.ElectricityComplaint;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ElectricityComplaintApi {

    @GET("/api/electricity/complaint/getAll")
    Call<List<ElectricityComplaint>> getAllComplaintTypes();
}
