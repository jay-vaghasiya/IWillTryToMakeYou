package com.jay.iwilltrytomakeyou

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun checkPermission(permission: String,context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

fun requestPermission(permission: String,context: Context) {
    ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), 1)
}
