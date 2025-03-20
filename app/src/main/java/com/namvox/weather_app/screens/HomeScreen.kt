package com.namvox.weather_app.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.namvox.weather_app.models.BaseModel
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val locations by viewModel.locations.collectAsState()
    val (city, setCity) = remember { mutableStateOf("") }

    LaunchedEffect(city) {
        delay(500)
        if (city.isNotEmpty()) {
            viewModel.searchLocation(city)
        }
    }

    // Sử dụng Box để nền gradient bao phủ toàn bộ màn hình
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9AC6F3),  // Màu xanh nhạt phía trên
                        Color(0xFF294A91)   // Màu xanh đậm phía dưới
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 64.dp),
        ) {
            Text(
                text = "Thời tiết",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray), // Màu nền của ô nhập
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon tìm kiếm bên trái
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa icon và textfield

                    // Ô nhập văn bản (Dùng BasicTextField để không có line đen)
                    BasicTextField(
                        value = city,
                        onValueChange = { setCity(it) },
                        modifier = Modifier
                            .weight(1f) // Chiếm hết phần trống
                            .background(Color.Transparent),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp
                        ),
                        decorationBox = { innerTextField ->
                            if (city.isEmpty()) {
                                Text(
                                    text = "Nhập tên thành phố",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa textfield và icon micro

                    // Icon micro bên phải
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic Icon",
                        tint = Color.Black
                    )
                }
            }



            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = locations is BaseModel.Success,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Column {
                    Text(text = "Chọn thành phố của bạn", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    when (val data = locations) {
                        is BaseModel.Success -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(data.data.size) { index ->
                                    val location = data.data[index]
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.secondary)
                                            .clickable {
                                                navController.navigate("weather/${location.key}/${location.englishName}/${location.country.englishName}")
                                            }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                location.englishName,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                location.country.englishName,
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            AnimatedVisibility(
                visible = locations is BaseModel.Loading,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

