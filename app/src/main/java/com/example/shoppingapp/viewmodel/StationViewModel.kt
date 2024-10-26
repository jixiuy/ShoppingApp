import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.GlobalToken
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.Station
import com.example.shoppingapp.models.StationBean
import com.example.shoppingapp.models.StationInfoResponse
import com.example.shoppingapp.models.UserRequest
import com.example.shoppingapp.network.RetrofitClient
import com.example.shoppingapp.repository.SupplierRepository
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class StationViewModel : ViewModel() {
    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> get() = _registerStatus

    private val userRepository = UserRepository()

    var stationList by mutableStateOf<List<Station>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    suspend fun getStationInfo(token:String){
        viewModelScope.launch{
            try {
                val response = stationRepository.getStationInfo(token)
                if (response.isSuccessful) {
                    stationList = response.body()?.data ?: emptyList()

                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }


    fun registerStation(
        context: Context,
        storeName: String,
        contactInfo: String,
        addressDetails: String,
        stationType: String,
        token: String
    ) {
        viewModelScope.launch {
            try {
                val response = userRepository.registerStation(storeName, contactInfo, addressDetails, stationType, token)
                if (response.isSuccessful && response.body()?.code == 200) {
                    _registerStatus.postValue(true)
                    ToastUtil.showCustomToast(context, "成为供货站工作人员成功", R.drawable.icon)
                } else {

                    ToastUtil.showCustomToast(context, "已经是供货人员", R.drawable.icon)

                }
            } catch (e: Exception) {
                //_registerStatus.postValue(false)
                Toast.makeText(context, "请求异常: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val stationRepository = SupplierRepository()

    // 使用 StateFlow 来管理登录响应
    private val _stationGoodsInfo = MutableLiveData<StationBean?>(null)
    val stationGoodsInfo: LiveData<StationBean?> = _stationGoodsInfo

    fun getStationGoodsInfo(token: String) {
        viewModelScope.launch {
            try {
                val response = stationRepository.getStationShoppingInfo(token)

                if (response.isSuccessful && response.body()?.code == 200) {
                    // 注册成功
                    _stationGoodsInfo.postValue(response.body())
                    //ToastUtil.showCustomToast(MyApp.getContext(), "获取司机数据成功", R.drawable.icon)
                } else {
                    //ToastUtil.showCustomToast(MyApp.getContext(), "司机没有数据", R.drawable.icon)
                }
            } catch (e: Exception) {
                _stationGoodsInfo.postValue(null)
                //Toast.makeText(MyApp.getContext(), "请求异常: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val _requestList = MutableLiveData<List<UserRequest>>()
    val requestList: LiveData<List<UserRequest>> = _requestList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage2: LiveData<String?> = _errorMessage

    fun getRequests(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.supplierApiService.getRequests(token)
                if (response.isSuccessful) {
                    _requestList.value = response.body()?.data ?: emptyList()
                    //ToastUtil.showCustomToast(MyApp.getContext(),"数据请求成功",R.drawable.icon)
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                    //ToastUtil.showCustomToast(MyApp.getContext(),"数据请求失败",R.drawable.icon)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                //ToastUtil.showCustomToast(MyApp.getContext(),"数据请求异常",R.drawable.icon)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
