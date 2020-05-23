package com.jemiola.moodtimeline.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.jemiola.moodtimeline.base.BaseApplication
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

object PermissionsUtil {

    fun isStoragePermissionGranted(): Boolean {
        val storageWritePermission =
            BaseApplication.context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val storageReadPermission =
            BaseApplication.context.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        return storageWritePermission == PackageManager.PERMISSION_GRANTED &&
                storageReadPermission == PackageManager.PERMISSION_GRANTED
    }

    fun askForStoragePermission(
        context: Context?,
        onDeniedAction: () -> Unit = {},
        onGrantedAction: () -> Unit = {}
    ) {
        if (context != null) {
            AndPermission
                .with(context)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onDenied { onDeniedAction() }
                .onGranted { onGrantedAction() }
                .start()
        }
    }

}