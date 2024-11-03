package com.example.shoppingapp.tools

import android.app.Activity

object ActivityManager {
    private val activityList = mutableListOf<Activity>()

    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
    }

    fun finishAll() {
        for (activity in activityList) {
            activity.finish()
        }
    }
}
