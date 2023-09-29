package com.jay.iwilltrytomakeyou

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun checkNotificationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context,android.Manifest.permission.POST_NOTIFICATIONS)==
            PackageManager.PERMISSION_GRANTED
}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun requestNotificationPermission(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
        1)
}