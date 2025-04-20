package com.jchunga.musicapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jchunga.musicapp.R


val Satoshi = FontFamily(
    Font(R.font.satoshi_regular, FontWeight.Normal),
    Font(R.font.satoshi_bold, FontWeight.Bold),
    Font(R.font.satoshi_black, FontWeight.Black),
    Font(R.font.satoshi_light, FontWeight.Light),
    Font(R.font.satoshi_medium, FontWeight.Medium),
    Font(R.font.satoshi_italic, FontWeight.Thin)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displaySmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = Satoshi,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp,
        color = Color.White
    ),
    displayMedium = TextStyle(
        fontSize = 32.sp,
        fontFamily = Satoshi,
        fontWeight = FontWeight.Bold,
        lineHeight = 48.sp,
        color = Color.White
    ),
    displayLarge = TextStyle(
        fontSize = 40.sp,
        fontFamily = Satoshi,
        fontWeight = FontWeight.Normal,
        lineHeight = 56.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = Satoshi,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = Satoshi,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontSize = 13.sp,
        fontFamily = Satoshi,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.sp,
        color = Color.White
    ),
    labelMedium = TextStyle(
        fontSize = 15.sp,
        fontFamily = Satoshi,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        color = Color.White
    ),
    labelLarge = TextStyle(
        fontSize = 17.sp,
        fontFamily = Satoshi,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = Color.White
    )

)