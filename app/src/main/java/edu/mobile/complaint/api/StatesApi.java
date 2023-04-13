package edu.mobile.complaint.api;

import java.util.List;

import edu.mobile.complaint.model.States;
import retrofit2.Call;
import retrofit2.http.GET;

public interface StatesApi {

    @GET("/api/states/getAll")
    Call<List<States>> getAllStates();
}
