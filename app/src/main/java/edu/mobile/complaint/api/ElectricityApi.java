package edu.mobile.complaint.api;

import edu.mobile.complaint.model.Electricity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ElectricityApi {

    @POST("/api/electricity/create")
    Call<Electricity> createConsumerComplaint(@Body Electricity electricity);
}
