package com.namvox.weather_app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.android.gms.location.LocationServices

class LocationProvider(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    /**
     * Kiểm tra xem ứng dụng đã được cấp quyền truy cập vị trí chưa
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Lấy vị trí hiện tại của thiết bị
     * Trả về null nếu không thể lấy vị trí
     */
    suspend fun getCurrentLocation(): Location? = withContext(Dispatchers.IO) {
        if (!hasLocationPermission()) {
            return@withContext null
        }

        try {
            // Kiểm tra quyền lần nữa ngay trước khi gọi API
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@withContext null
            }

            val locationTask = fusedLocationClient.lastLocation
            return@withContext Tasks.await(locationTask)
        } catch (e: SecurityException) {
            // Xử lý lỗi bảo mật nếu quyền bị thu hồi
            e.printStackTrace()
            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}