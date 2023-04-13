package edu.mobile.complaint.api;

import java.util.List;

import edu.mobile.complaint.model.District;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DistrictApi {

    @GET("/api/district/getAll")
    Call<List<District>> getAllDistrict();

    @GET("/api/district/state/{stateId}")
    Call<List<District>> getDistrictByStateId(@Path("stateId") int stateId);
}
