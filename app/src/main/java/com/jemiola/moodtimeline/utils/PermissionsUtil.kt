package com.jemiola.moodtimeline.utils

import android.Manifest
import android.content.pm.PackageManager
import com.jemiola.moodtimeline.base.BaseApplication
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission

object PermissionsUtil {

    fun isStoragePermissionGranted(): Boolean {
        val storageWritePermission =
            BaseApplication.context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val storageReadPermission =
            BaseApplication.context.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        return storageWritePermission == PackageManager.PERMISSION_GRANTED &&
                storageReadPermission == PackageManager.PERMISSION_GRANTED
    }

    fun askForStoragePermission() {
        AndPermission
            .with(BaseApplication.context)
            .requestCode(0)
            .permission(Permission.STORAGE)
            .start()
    }

}