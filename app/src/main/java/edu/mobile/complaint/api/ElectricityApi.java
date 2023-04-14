package edu.mobile.complaint.api;

import edu.mobile.complaint.model.Electricity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ElectricityApi {

    @POST("/api/electricity/create")
    Call<Electricity> createConsumerComplaint(@Body Electricity electricity);

    @GET("/api/electricity/id/{complaintId}")
    Call<Electricity> getComplaintStatusBycId(@Path("complaintId") String id);
}
