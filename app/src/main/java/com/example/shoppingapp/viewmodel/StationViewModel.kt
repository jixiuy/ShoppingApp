import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.R
import com.example.shoppingapp.models.StationBean
import com.example.shoppingapp.repository.SupplierRepository
import com.example.shoppingapp.repository.UserRepository
import kotlinx.coroutines.launch

class StationViewModel : ViewModel() {
    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> get() = _registerStatus

    private val userRepository = UserRepository()

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
}
