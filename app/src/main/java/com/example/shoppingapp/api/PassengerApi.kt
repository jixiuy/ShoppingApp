import com.example.shoppingapp.models.DriverResult
import retrofit2.Call
import retrofit2.http.*
import com.example.shoppingapp.models.Result
import com.example.shoppingapp.models.StationResponse
import retrofit2.Response

interface PassengerApi {

    @GET("/user/login")
    fun login(@Query("code") code: String): Call<Result>

    @POST("/user/passenger/verifyRole")
    fun verifyRole(
        @Query("verify") verify: String,
        @Query("stationId") stationId: String?,
        @Query("role") role: String?,
        @Query("vehicleId") vehicleId: String?
    ): Call<Result>

    @GET("/user/passenger/coupons")
    fun getUserCoupons(): Call<Result>

    @POST("/user/passenger/claimCoupon/{couponId}")
    fun claimCoupon(@Path("couponId") couponId: String): Call<Result>

    @POST("/user/passenger/useCoupon/{couponId}")
    fun useCoupon(@Path("couponId") couponId: String): Call<Result>

    @GET("/user/passenger/couponDetail/{couponId}")
    fun getCouponDetail(@Path("couponId") couponId: String): Call<Result>

    @GET("/user/passenger/allCoupons")
    fun getAllCoupons(): Call<Result>

    @GET("/user/passenger/profile")
    fun getUserProfile(): Call<Result>

    @GET("/user/passenger/orderHistory/{userId}")
    fun getUserOrderHistory(@Path("userId") userId: String): Call<Result>

    @GET("/user/passenger/currentOrder/{userId}")
    fun getUserCurrentOrder(@Path("userId") userId: String): Call<Result>

    @POST("user/driver/register")
    suspend fun registerDriver(
        @Query("license") license: String,
        @Query("location") location: String,
        @Header("token") token: String
    ): Response<DriverResult>

    @POST("user/station/register")
    suspend fun registerStation(
        @Query("store_name") storeName: String,
        @Query("contact_info") contactInfo: String,
        @Query("address_detils") addressDetails: String,
        @Query("station_type") stationType: String,
        @Header("token") token: String
    ): Response<StationResponse>

}
