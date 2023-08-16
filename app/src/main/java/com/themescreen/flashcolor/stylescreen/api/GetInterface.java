package com.themescreen.flashcolor.stylescreen.api;



import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetInterface {
    @POST("com.callerscreen.colorflash.colorphone_MmIxXV.php?")
    Call<Example> getStatus(@Query("package_name") String str , @Query("status") int app_status, @Query(value = "api_key", encoded = true) String key);

    @POST("com.callerscreen.colorflash.colorphone_MmIxXV.php?")
    Call<Example> getDebugStatus(@Query("package_name") String str , @Query("status") int app_status, @Query(value = "api_key", encoded = true) String key, @Query("debug") boolean IsDebug);
}
