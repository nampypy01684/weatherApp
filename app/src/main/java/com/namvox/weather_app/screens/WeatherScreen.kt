package com.namvox.weather_app.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.ArrowDownward
import androidx.compose.material.icons.sharp.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.namvox.weather_app.models.BaseModel
import com.namvox.weather_app.ui.theme.russoFont
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeatherScreen(
    navController: NavController,
    locationKey: String,
    locationName: String,
    country: String,
    viewModel: WeatherViewModel = viewModel()
) {
    val dailyForecasts = viewModel.dailyForecast.collectAsState().value
    val hourlyForecasts = viewModel.hourlyForecast.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getDailyForecast(locationKey)
        viewModel.getHourlyForecast(locationKey)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF87CEFA), Color(0xFF00008B))
                )
            )
            .padding(horizontal = 16.dp)
    ) {
        Column {
            Row(modifier = Modifier.padding(vertical = 32.dp)) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navController.popBackStack() },
                    tint = Color.White,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = locationName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(text = country, color = Color.Gray)
                }
            }

            AnimatedVisibility(visible = hourlyForecasts is BaseModel.Success) {
                val data = hourlyForecasts as BaseModel.Success
                val temp = data.data.first().temperature
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "${temp.value}°", fontWeight = FontWeight.Bold, fontSize = 80.sp, color = Color.White, fontFamily = russoFont)
                }
            }
            AnimatedVisibility(visible = hourlyForecasts is BaseModel.Loading) { Loading() }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Thời tiết hàng giờ:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedVisibility(visible = hourlyForecasts is BaseModel.Success) {
                val data = hourlyForecasts as BaseModel.Success
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(data.data) { forecast ->
                        Column(
                            modifier = Modifier
                                .size(100.dp, 140.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.secondary),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = SimpleDateFormat("H a").format(Date(forecast.epochDateTime * 1000)), color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            AsyncImage(
                                modifier = Modifier.size(70.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://developer.accuweather.com/sites/default/files/${forecast.weatherIcon.fixIcon()}-s.png")
                                    .build(),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "${forecast.temperature.value}°", color = Color.White)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Thời tiết hàng ngày:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedVisibility(visible = dailyForecasts is BaseModel.Success) {
                val data = dailyForecasts as BaseModel.Success
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(data.data.dailyForecasts) { forecast ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.secondary)
                                .padding(start = 16.dp, end = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val day = SimpleDateFormat("d", Locale.getDefault()).apply {
                                timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
                            }.format(Date(forecast.epochDate * 1000)).toInt()
                            Text(text = "$day", color = Color.White)
                            Row {
                                Icon(Icons.Sharp.ArrowDownward, tint = Color(0xffff5353), contentDescription = null)
                                Text(text = "${forecast.temperature.min.value}°", color = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Sharp.ArrowUpward, tint = Color(0xff2eff8c), contentDescription = null)
                                Text(text = "${forecast.temperature.max.value}°", color = Color.White)
                            }
                            AsyncImage(
                                modifier = Modifier.size(70.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://developer.accuweather.com/sites/default/files/${forecast.day.icon.fixIcon()}-s.png")
                                    .build(),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

fun Int.fixIcon(): String = if (this > 9) toString() else "0$this"

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color.White)
    }
}
