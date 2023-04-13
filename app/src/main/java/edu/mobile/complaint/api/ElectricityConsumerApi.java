package edu.mobile.complaint.api;

import edu.mobile.complaint.model.ElectricityConsumer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ElectricityConsumerApi {

    @GET("/api/electricity/consumer/{consumerNumber}")
    Call<ElectricityConsumer> getConsumerId(@Path("consumerNumber") String consumerNumber);

}
