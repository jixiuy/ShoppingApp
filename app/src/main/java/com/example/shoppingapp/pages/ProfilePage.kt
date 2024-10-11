import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppingapp.R

@Composable
fun MyProfilePage() {
    // 获取屏幕宽度并计算 1/4 位置
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    var offsetX by remember { mutableStateOf(0.dp) }

    // 使用LaunchedEffect确保计算后的偏移量生效
    LaunchedEffect(screenWidthDp) {
        offsetX = screenWidthDp * 0.25f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .background(
                    color = Color(0xFFEFEEF6),
                    shape = RoundedCornerShape(16.dp)
                ),
        ) {
            ProfileInformation()

            Spacer(Modifier.padding(vertical = 5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp,end=15.dp)
                    .clickable { /* 点击事件处理 */ }
                    .clip(RoundedCornerShape(16.dp))
                    .padding(15.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_history_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "历史订单",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(3f)
                )
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            //Spacer(Modifier.padding(vertical = 5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp,end=15.dp)
                    .clickable { /* 点击事件处理 */ }
                    .clip(RoundedCornerShape(16.dp))
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "完善个人信息",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(3f)
                )
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            // Repeat for the other rows
            //Spacer(Modifier.padding(vertical = 5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp,end=15.dp)
                    .clickable { /* 点击事件处理 */ }
                    .clip(RoundedCornerShape(16.dp))
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_directions_car_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "成为司机",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(3f)
                )
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            //Spacer(Modifier.padding(vertical = 5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp,end=15.dp)
                    .clickable { /* 点击事件处理 */ }
                    .clip(RoundedCornerShape(16.dp))
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_local_shipping_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "成为供货人员",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(3f)
                )
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.padding(vertical = 5.dp))
        }
    }
    Spacer(Modifier.padding(vertical = 5.dp))
    Exit()
}

@Composable
fun ProfileInformation() {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(15.dp)

    ) {
        Row(
            modifier = Modifier
                .clickable { /* 点击事件处理 */ }
                .background(color = Color(0xFFEFEEF6))
        ) {
            Image(
                painter = painterResource(id = R.drawable.wechatjpg),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.padding(horizontal = 12.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "用户名",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.padding(vertical = 6.dp))
                Text(
                    text = "用户等级"
                )
            }
        }
    }

}

@Composable
fun Exit() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(
                elevation = 10.dp, // 设置阴影的高度
                shape = RoundedCornerShape(8.dp), // 阴影的形状
                clip = false // 可选：是否剪裁
            )
            .background(
                color = Color(0xFFEFEEF6),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* 点击事件处理 */ }
                .padding(vertical = 15.dp), // 调整垂直填充以适当增加高度
            contentAlignment = Alignment.Center // 设置内容居中
        ) {
            Text(

                text = "退出"
            )
        }
    }

}
