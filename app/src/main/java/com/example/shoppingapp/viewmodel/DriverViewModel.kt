import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.R
import com.example.shoppingapp.models.DriverBean
import com.example.shoppingapp.repository.DriverRepository
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.launch

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

    // 使用 StateFlow 来管理登录响应
    private val _carGoodsInfo = MutableLiveData<DriverBean?>(null)
    val carGoodsInfo: LiveData<DriverBean?> = _carGoodsInfo

    fun getCarGoodsInfo(token: String) {
        viewModelScope.launch {
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
            }
        }
    }
}
