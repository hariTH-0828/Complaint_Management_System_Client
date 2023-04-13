package edu.mobile.complaint.api;

import java.util.List;

import edu.mobile.complaint.model.Garbage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GarbageApi {

    @GET("/api/garbage/getAll")
    Call<List<GarbageApi>> getAllGarbage();

    @POST("/api/garbage/create")
    Call<Garbage> save(@Body Garbage garbage);
}
