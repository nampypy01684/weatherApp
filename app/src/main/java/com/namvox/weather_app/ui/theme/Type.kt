package com.namvox.weather_app.ui.theme

import androidx.compose.material3.Typography
import com.namvox.weather_app.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ubuntuFont = FontFamily(
    Font(R.font.ubuntu_regular)
)

val russoFont = FontFamily(
    Font(R.font.russo_one_regular)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = ubuntuFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

)