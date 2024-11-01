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
import com.example.shoppingapp.models.StationShoppingResponse
import com.example.shoppingapp.models.UserRequest
import com.example.shoppingapp.network.RetrofitClient
import com.example.shoppingapp.repository.SupplierRepository
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class StationViewModel : ViewModel() {
    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> get() = _registerStatus

    private val userRepository = UserRepository()

    private val _stationList = MutableLiveData<List<Station>>()

    val stationList :LiveData<List<Station>> = _stationList

    suspend fun getStationInfo(token:String){
        viewModelScope.launch{
            try {
                val response = stationRepository.getStationInfo(token)
                if (response.isSuccessful) {
                    _stationList.postValue( response.body()?.data ?: emptyList())
                }
            } catch (e: Exception) {

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

    private val _productList = MutableStateFlow<List<com.example.shoppingapp.models.StationProductResponse.Product>>(emptyList())
    val productList: StateFlow<List<com.example.shoppingapp.models.StationProductResponse.Product>> get() = _productList

    private val _errorMessage3 = MutableStateFlow<String?>(null)
    val errorMessage3: StateFlow<String?> get() = _errorMessage3

    fun loadProducts(stationId: Int, token: String) {
        viewModelScope.launch {
            val result = stationRepository.fetchProducts(stationId, token)
            result.onSuccess { products ->
                _productList.value = products
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
        }
    }

    private val _response = MutableLiveData<StationShoppingResponse>()
    val response: LiveData<StationShoppingResponse> get() = _response

    fun incrementProduct(productId: Int, num: Int, token: String) {
        viewModelScope.launch {
            val result = stationRepository.incrementProduct(productId, num, token)
            if(result.isSuccessful&&result.body()?.code==200){
                ToastUtil.showCustomToast(MyApp.getContext(),"添加成功")
                    _response.postValue(result.body())
            }else{
                ToastUtil.showCustomToast(MyApp.getContext(),"添加失败")
            }
        }
    }

    fun decrementProduct(productId: Int, num: Int, token: String) {
        viewModelScope.launch {
            val result = stationRepository.decrementProduct(productId, num, token)
            if(result.isSuccessful&&result.body()?.code==200){
                ToastUtil.showCustomToast(MyApp.getContext(),"减少成功")
                _response.postValue(result.body())
            }else{
                ToastUtil.showCustomToast(MyApp.getContext(),"减少失败")
            }
        }
    }

    fun modifyProduct(productId: Int, num: Int, token: String) {
        viewModelScope.launch {
            val result = stationRepository.storeProduct(productId, num, token)
            if(result.isSuccessful&&result.body()?.code==200){
                ToastUtil.showCustomToast(MyApp.getContext(),"修改成功")
                _response.postValue(result.body())
            }else{
                ToastUtil.showCustomToast(MyApp.getContext(),"修改失败")
            }
        }
    }

    fun deleteProductInfo(productId: Int, token: String) {
        viewModelScope.launch {
            val result = stationRepository.deleteProductInfo(productId,token)
            if(result.isSuccessful&&result.body()?.code==200){
                ToastUtil.showCustomToast(MyApp.getContext(),"删除成功")
                _response.postValue(result.body())
            }else{
                ToastUtil.showCustomToast(MyApp.getContext(),"删除失败")
            }
        }
    }

    private val _stationGoodsInfo = MutableLiveData<StationBean?>(null)
    val stationGoodsInfo: LiveData<StationBean?> = _stationGoodsInfo

    fun getStationGoodsInfo(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
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
            }finally {
                _isLoading.value = false
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
