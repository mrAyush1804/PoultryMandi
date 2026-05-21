package com.ninjafarm.poultrymandi.app.Core.AppUpdate
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdateManager(private val context: Context) {

    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    // Yeh request code aap MainActivity me check karenge
    companion object {
        const val REQ_CODE_VERSION_UPDATE = 5321
    }

    // Update check karne ka function
    fun checkForUpdates(activity: Activity) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Agar update available hai, to system dialog box open karein
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE),
                        REQ_CODE_VERSION_UPDATE
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun resumeUpdate(activity: Activity) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE),
                        REQ_CODE_VERSION_UPDATE
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}