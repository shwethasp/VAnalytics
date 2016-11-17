package google.shwethasp.com.analytics_google.api_call;


import com.google.gson.JsonElement;

import java.util.Vector;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

/*import google.shwethasp.com.analytics_google.GaFilter;
import google.shwethasp.com.analytics_google.GaGoal;
import google.shwethasp.com.analytics_google.GaList;
import google.shwethasp.com.analytics_google.GaProfile;
import google.shwethasp.com.analytics_google.GaRealtime;
import google.shwethasp.com.analytics_google.GaSegment;
import google.shwethasp.com.analytics_google.GaUnsampleReport;
import google.shwethasp.com.analytics_google.GaWebPropertie;*/

public interface IGoogleAnalyticsApi {
    public static final String SERVER = "https://www.googleapis.com/analytics/v3/";

    @GET("/analytics/v3/management/accounts/~all/webproperties/~all/profiles?fields=items%2Ftimezone")
    Call<JsonElement>  getTimeZome(@Header("Authorization") String Token);


    @Headers({"Content-Type: application/json"})
    @GET("/analytics/v3/management/accountSummaries")
    Call<JsonElement> getAccounts(
            @Header("Authorization") String Token
    );


/*    GaData getData(
            @Header("Authorization") String token,
            @Query("ids") String str,
            @Query("start-date") String str2,
            @Query("end-date") String str3,
            @Query("metrics") String str4,
            @Query("dimensions") String str5,
            @Query("sort") String str6,
            @Query("filters") String str7,
            @Query("segment") String str8,
            @Query("start-index") Integer num,
            @Query("max-results") Integer num2
    );*/


    //   @POST
//    Callback<QueryVars> authenticate(@Body QueryVars queryVars);
    @Headers({"Content-Type: application/json"})
    @GET("/analytics/v3/data/ga")
    Call<JsonElement> getResult(@Header("Authorization") String Token,
                                @Query("ids") String id, @Query("start-date") String std,
                                @Query("end-date") String end, @Query("metrics") Vector<String> metrics,
                                @Query("dimensions") Vector<String> dimensions,
                                @Query("filters") Vector<String> filters
                                //  Call<ArrayList> getResult(@Header("Authorization") String token

    );


    @GET
    Call<JsonElement> getProfile(@Url String url);

    @Headers({"Content-Type: application/json"})
    @GET("/analytics/v3/data/ga")
    Call<JsonElement> getVisitors(@Header("Authorization") String Token,
                                  @Query("ids") String id,
                                  @Query("start-date") String std,
                                  @Query("end-date") String end,
                                  @Query("metrics") Vector<String> metrics,
                                  @Query("dimensions") Vector<String> dimensions
                                  //  Call<ArrayList> getResult(@Header("Authorization") String token

    );





   /* @GET("/management/accounts/{accountId}/filters")
    GaList<GaFilter> getFilters(@Path("accountId") String str);

    @GET("/management/accounts/{accountId}/webproperties/{webPropertyId}/profiles/{profileId}/goals")
    GaList<GaGoal> getGoals(@Path("accountId") String str, @Path("webPropertyId") String str2, @Path("profileId") String str3);

    @GET("/data/realtime")
    GaRealtime getRealtime(@Query("ids") String str, @Query("metrics") String str2, @Query("dimensions") String str3, @Query("sort") String str4, @Query("filters") String str5);

    @GET("/management/accounts/{accountId}/webproperties/{webPropertyId}/profiles/{profileId}/unsampledReports")
    GaList<GaUnsampleReport> getReports(@Path("accountId") String str, @Path("webPropertyId") String str2, @Path("profileId") String str3);

    @GET("/management/segments")
    GaList<GaSegment> getSegments();

    @GET("/management/accounts/{accountId}/webproperties/{webPropertyId}/profiles")
    GaList<GaProfile> getViewsProfiles(@Path("accountId") String str, @Path("webPropertyId") String str2);

    @GET("/management/accounts/{accountId}/webproperties")
    GaList<GaWebPropertie> getWebProperties(@Path("accountId") String str);*/


}
