package me.deepak.api.retrofit;

import me.deepak.api.search.responses.SearchApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Deepak Mishra
 */

public interface WalmartApiInterface {

    String BASE_URL = "https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/walmartproducts/";
    String API_KEY = "dd9085cd-80dc-44b9-be08-e6f5bae99b14";

    @GET("{apiKey}/{pageNumber}/{pageSize}")
    Call<SearchApiResponse> getProducts(
            @Path("apiKey") String apiKey,
            @Path("pageNumber") int pageNumber,
            @Path("pageSize") int pageSize);
}
