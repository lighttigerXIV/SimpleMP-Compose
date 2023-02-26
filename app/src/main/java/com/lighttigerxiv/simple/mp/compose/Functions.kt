package com.lighttigerxiv.simple.mp.compose

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.SettingsVM
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId


fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(
        500,
        500, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap
}

fun getAppString(context: Context, id: Int): String{

    return context.getString(id)
}

fun getString(stringID: Int): String{
    return Resources.getSystem().getString(stringID)
}


@Composable
fun getSurfaceColor(settingsVM: SettingsVM): Color{

    val themeMode = settingsVM.themeModeSetting.collectAsState().value
    val themeAccent = settingsVM.themeAccentSetting.collectAsState().value
    val darkMode = settingsVM.darkModeSetting.collectAsState().value

    return if (themeMode == "Dark" && darkMode == "Oled") {
        Color.Black
    } else if (themeMode == "Light" && themeAccent == "Blue") {
        Color(0xFFFEFBFF)
    } else if (themeMode == "Light" && themeAccent == "Red") {
        Color(0xFFFFFBFF)
    } else if (themeMode == "Light" && themeAccent == "Purple") {
        Color(0xFFFFFBFF)
    } else if (themeMode == "Light" && themeAccent == "Yellow") {
        Color(0xFFFFFBFF)
    } else if (themeMode == "Light" && themeAccent == "Orange") {
        Color(0xFFFFFBFF)
    } else if (themeMode == "Light" && themeAccent == "Green") {
        Color(0xFFFDFDF5)
    } else if (themeMode == "Light" && themeAccent == "Pink") {
        Color(0xFFFFFBFF)
    } else if (darkMode == "Oled" && themeMode == "Light" && isSystemInDarkTheme()) {
        Color(0xFFFFFBFF)
    } else if (darkMode == "Oled" && themeAccent.startsWith("Macchiato")) {
        Color.Black
    } else if (darkMode == "Oled" && themeAccent.startsWith("Frappe")) {
        Color.Black
    } else if (darkMode == "Oled" && themeAccent.startsWith("Mocha")) {
        Color.Black
    } else if (darkMode == "Oled" && isSystemInDarkTheme()) {
        Color.Black
    } else {
        MaterialTheme.colorScheme.surface
    }
}

fun Long.toMongoHex(): String{

    return BsonObjectId(this).toHexString()
}