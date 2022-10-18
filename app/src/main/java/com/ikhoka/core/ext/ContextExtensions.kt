package com.ikhoka.core.ext

import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi


fun Context.isTv(): Boolean {
    val leanback: Boolean =
        this.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
    val uiModeManager: UiModeManager? =
        this.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager?
    return leanback && Configuration.UI_MODE_TYPE_TELEVISION == uiModeManager?.currentModeType
}


fun Context.action(action: String): String {
    return "${this.applicationContext.packageName}.$action"
}

fun Context.areSystemAnimationsEnabled(): Boolean {
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
    val duration: Float = Settings.Global.getFloat(
        this.contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1F
    )
    val transition: Float = Settings.Global.getFloat(
        this.contentResolver,
        Settings.Global.TRANSITION_ANIMATION_SCALE,
        1F
    )
//    } else {
//        duration = Settings.System.getFloat(
//            this.contentResolver,
//            Settings.System.ANIMATOR_DURATION_SCALE, 1F
//        )
//        transition = Settings.System.getFloat(
//            this.contentResolver,
//            Settings.System.TRANSITION_ANIMATION_SCALE, 1F
//        )
//    }
    return duration != 0f && transition != 0f
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.hasInternet(): Boolean {
    val cm = getSystemService(ConnectivityManager::class.java)
    val network = cm.activeNetwork
    val capabilities = cm.getNetworkCapabilities(network)
    return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

fun Context.isNetworkConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    val connected = activeNetwork != null && activeNetwork.isConnected
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connected && this.hasInternet()
    } else {
        connected
    }
}

fun Context.isTimeAutomatic(): Boolean {
    return Settings.Global.getInt(this.contentResolver, Settings.Global.AUTO_TIME, 0) == 1
}
