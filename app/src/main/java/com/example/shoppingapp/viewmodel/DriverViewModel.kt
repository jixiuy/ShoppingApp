import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.MyApp
import com.example.shoppingapp.R
import com.example.shoppingapp.models.CarShoppingModifyResponse
import com.example.shoppingapp.models.DriverBean
import com.example.shoppingapp.network.RetrofitClient
import com.example.shoppingapp.repository.DriverRepository
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverViewModel : ViewModel() {
    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> get() = _registerStatus

    // Retrofit 服务实例
    private val driverRepository = UserRepository()
    fun registerDriver(context: Context, license: String, location: String, token: String) {
        viewModelScope.launch {
            try {
                val response = driverRepository.registerDriver(license, location, token)
                if (response.isSuccessful && response.body()?.code == 200) {
                    // 注册成功
                    _registerStatus.postValue(true)
                    ToastUtil.showCustomToast(context, "成为司机成功", R.drawable.icon)
                } else {
                    ToastUtil.showCustomToast(context, "已经是司机", R.drawable.icon)
                }
            } catch (e: Exception) {
                _registerStatus.postValue(false)
                Toast.makeText(context, "请求异常: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val myDriverRepository = DriverRepository()

    private val _deleteResponse = MutableLiveData<CarShoppingModifyResponse?>(null)
    val deleteResponse : LiveData<CarShoppingModifyResponse?> = _deleteResponse

    suspend fun deleteCarGoodsInfo(productId: Int,token:String){
        viewModelScope.launch {
            val response = myDriverRepository.deleteCarShoppingInfo(productId,token)
            ToastUtil.showCustomToast(MyApp.getContext(),response.body().toString())
            try {
                if(response.isSuccessful&&response.body()?.code==200){
                    _deleteResponse.postValue(response.body())
                    ToastUtil.showCustomToast(MyApp.getContext(),"删除成功",R.drawable.icon)
                }else{
                    ToastUtil.showCustomToast(MyApp.getContext(),"删除失败",R.drawable.icon)
                }
            }catch (e:Exception){
                ToastUtil.showCustomToast(MyApp.getContext(),"网络请求异常",R.drawable.icon)

            }

        }
    }

    // 使用 StateFlow 来管理登录响应
    private val _carGoodsInfo = MutableLiveData<DriverBean?>(null)
    val carGoodsInfo: LiveData<DriverBean?> = _carGoodsInfo

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    fun getCarGoodsInfo(token: String) {
        viewModelScope.launch {
            _isLoading.value = true // 请求开始，设置为加载中
            try {
                val response = myDriverRepository.getCarShoppingInfo(token)

                if (response.isSuccessful && response.body()?.code == 200) {
                    // 注册成功
                    _carGoodsInfo.postValue(response.body())
                    //ToastUtil.showCustomToast(MyApp.getContext(), "获取司机数据成功", R.drawable.icon)
                } else {
                    //ToastUtil.showCustomToast(MyApp.getContext(), "司机没有数据", R.drawable.icon)
                }
            } catch (e: Exception) {
                _carGoodsInfo.postValue(null)
                //Toast.makeText(MyApp.getContext(), "请求异常: ${e.message}", Toast.LENGTH_SHORT).show()
            }finally {
                _isLoading.value = false // 请求结束，设置为加载完成
            }
        }
    }

    private val _responseMessage = MutableLiveData<CarShoppingModifyResponse>()
    val responseMessage: LiveData<CarShoppingModifyResponse> get() = _responseMessage

    fun increaseStock(productId: Int, num: Int, token: String) {
        viewModelScope.launch {
            RetrofitClient.driverApiService.increaseStock(productId, num, token).enqueue(object :
                Callback<CarShoppingModifyResponse> {
                override fun onResponse(call: Call<CarShoppingModifyResponse>, response: Response<CarShoppingModifyResponse>) {
                    if (response.isSuccessful) {
                        _responseMessage.value = response.body()
                        ToastUtil.showCustomToast(MyApp.getContext(),"库存增加成功",R.drawable.icon)

                    } else {
                        //_responseMessage.value = "库存增加失败"
                        ToastUtil.showCustomToast(MyApp.getContext(),"库存增加失败",R.drawable.icon)
                    }
                }

                override fun onFailure(call: Call<CarShoppingModifyResponse>, t: Throwable) {
                    //_responseMessage.value = "网络请求失败: ${t.message}"
                    ToastUtil.showCustomToast(MyApp.getContext(),"网络请求失败",R.drawable.icon)
                }
            })
        }
    }

    fun decreaseStock(productId: Int, num: Int, token: String) {
        viewModelScope.launch {
            RetrofitClient.driverApiService.decreaseStock(productId, num, token).enqueue(object : Callback<CarShoppingModifyResponse> {
                override fun onResponse(call: Call<CarShoppingModifyResponse>, response: Response<CarShoppingModifyResponse>) {
                    if (response.isSuccessful) {
                        _responseMessage.value = response.body()
                        ToastUtil.showCustomToast(MyApp.getContext(),"库存减少成功",R.drawable.icon)

                    } else {
                        //_responseMessage.value = "库存减少失败: ${response.message()}"
                        ToastUtil.showCustomToast(MyApp.getContext(),"库存减少失败",R.drawable.icon)
                    }
                }

                override fun onFailure(call: Call<CarShoppingModifyResponse>, t: Throwable) {
                    //_responseMessage.value = "网络请求失败: ${t.message}"
                    ToastUtil.showCustomToast(MyApp.getContext(),"网络请求失败",R.drawable.icon)
                }
            })
        }
    }

    fun changeStock(productId: Int, num: Int, token: String) {
        viewModelScope.launch {
            RetrofitClient.driverApiService.changeStock(productId, num, token).enqueue(object : Callback<CarShoppingModifyResponse> {
                override fun onResponse(call: Call<CarShoppingModifyResponse>, response: Response<CarShoppingModifyResponse>) {
                    if (response.isSuccessful) {
                        _responseMessage.value = response.body()
                        ToastUtil.showCustomToast(MyApp.getContext(),"库存修改成功",R.drawable.icon)

                    } else {
                        //_responseMessage.value = "库存修改失败: ${response.message()}"
                        ToastUtil.showCustomToast(MyApp.getContext(),"库存修改失败",R.drawable.icon)
                    }
                }

                override fun onFailure(call: Call<CarShoppingModifyResponse>, t: Throwable) {
                    //_responseMessage.value = "网络请求失败: ${t.message}"
                    ToastUtil.showCustomToast(MyApp.getContext(),"网络请求失败",R.drawable.icon)
                }
            })
        }
    }
}
