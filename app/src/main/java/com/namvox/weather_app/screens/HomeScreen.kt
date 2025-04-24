package com.namvox.weather_app.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.namvox.weather_app.models.BaseModel
import com.namvox.weather_app.models.CurrentWeather
import com.namvox.weather_app.models.Location
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val locations by viewModel.locations.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val currentWeather by viewModel.currentWeather.collectAsState()
    val locationPermissionGranted by viewModel.locationPermissionGranted.collectAsState()
    val (city, setCity) = remember { mutableStateOf("") }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateLocationPermissionState(isGranted)
    }

    LaunchedEffect(Unit) {
        if (locationPermissionGranted) {
            viewModel.fetchCurrentLocationWeather()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(city) {
        delay(500)
        if (city.isNotEmpty()) {
            viewModel.searchLocation(city)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9AC6F3),
                        Color(0xFF294A91)
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

            // Hiển thị thời tiết vị trí hiện tại
            CurrentWeatherSection(
                currentLocation = currentLocation,
                currentWeather = currentWeather,
                onRequestPermission = {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                onRefresh = {
                    viewModel.fetchCurrentLocationWeather()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ô tìm kiếm
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = city,
                        onValueChange = { setCity(it) },
                        modifier = Modifier
                            .weight(1f)
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

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic Icon",
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Hiển thị kết quả tìm kiếm
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
                                columns = GridCells.Fixed(1),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(data.data.size) { index ->
                                    val location = data.data[index]
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)
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

@Composable
fun CurrentWeatherSection(
    currentLocation: BaseModel<com.namvox.weather_app.models.Location>?,
    currentWeather: BaseModel<List<com.namvox.weather_app.models.CurrentWeather>>?,
    onRequestPermission: () -> Unit,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80FFFFFF)
        )
    ) {
        when {
            currentLocation is BaseModel.Loading || currentWeather is BaseModel.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            currentLocation is BaseModel.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Không thể lấy vị trí hiện tại",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentLocation.error,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRequestPermission) {
                        Text("Cấp quyền truy cập vị trí")
                    }
                }
            }

            currentLocation is BaseModel.Success && currentWeather is BaseModel.Success -> {
                val location = currentLocation.data
                val weather = currentWeather.data.firstOrNull()

                if (weather != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${location.englishName}, ${location.country.englishName}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "${weather.Temperature.Metric.Value.toInt()}°C",
                            color = Color.White,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = weather.WeatherText,
                            color = Color.White,
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Cảm giác như: ${weather.RealFeelTemperature.Metric.Value.toInt()}°C",
                            color = Color.White,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            WeatherInfoItem(
                                label = "Độ ẩm",
                                value = "${weather.RelativeHumidity}%"
                            )

                            WeatherInfoItem(
                                label = "Gió",
                                value = "${weather.Wind.Speed.Metric.Value.toInt()} ${weather.Wind.Speed.Metric.Unit}"
                            )

                            WeatherInfoItem(
                                label = "UV",
                                value = weather.UVIndexText
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val dateFormat = SimpleDateFormat("HH:mm, dd/MM", Locale.getDefault())
                        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                            .parse(weather.LocalObservationDateTime)

                        Text(
                            text = "Cập nhật lúc: ${dateFormat.format(date ?: Date())}",
                            color = Color.White,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = onRefresh) {
                            Text("Cập nhật", color = Color.White)
                        }
                    }
                } else {
                    Text(
                        text = "Không có dữ liệu thời tiết",
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = onRequestPermission) {
                        Text("Lấy thời tiết vị trí hiện tại")
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherInfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}