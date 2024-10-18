import com.example.shoppingapp.models.CarInformationResponse
import com.example.shoppingapp.models.DriverResult
import com.example.shoppingapp.models.HistoryAccountResponse
import com.example.shoppingapp.models.PurchaseResponse
import retrofit2.Call
import retrofit2.http.*
import com.example.shoppingapp.models.Result
import com.example.shoppingapp.models.StationResponse
import retrofit2.Response

interface PassengerApi {

    @GET("product/scan")
    suspend fun getCarInformation(
        @Query("license") license: Int,
        @Header("token") token: String
    ): CarInformationResponse


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

    @GET("user/orders/history")
    suspend fun historyAccount(
        @Header("token") token: String
    ): HistoryAccountResponse

    @POST("product/purchase")
    suspend fun purchaseProduct(
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int,
        @Query("license") license: Int,
        @Header("token") token: String
    ): Response<PurchaseResponse>

}
