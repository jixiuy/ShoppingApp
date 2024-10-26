import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.example.shoppingapp.R

object ToastUtil {

    fun showCustomToast(
        context: Context,
        message: String,
        @DrawableRes iconResId: Int? = R.drawable.icon
    ) {
        // 加载自定义布局
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast_layout, null)

        // 设置图标
        val icon = layout.findViewById<ImageView>(R.id.toast_icon)
        if (iconResId != null) {
            icon.setImageResource(iconResId)
            icon.visibility = ImageView.VISIBLE
        } else {
            icon.visibility = ImageView.GONE
        }

        // 设置消息内容
        val text = layout.findViewById<TextView>(R.id.toast_message)
        text.text = message

        // 显示Toast
        Toast(context).apply {
            this.view = layout
            this.duration = Toast.LENGTH_SHORT
            // 设置弹出位置
            val offsetY = context.resources.displayMetrics.heightPixels*3/4  // 从底部上移1/4高度
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, offsetY) // 0是水平偏移，100是垂直偏移
            show()
        }
    }
}
