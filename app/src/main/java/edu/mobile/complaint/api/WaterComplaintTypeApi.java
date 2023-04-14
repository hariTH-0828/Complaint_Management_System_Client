package edu.mobile.complaint.api;

import java.util.List;

import edu.mobile.complaint.model.WaterComplaintType;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WaterComplaintTypeApi {

    @GET("/api/water/complaint/getAll")
    Call<List<WaterComplaintType>> getAllComplaintTypes();
}
