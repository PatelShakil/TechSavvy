package com.shakilpatel.techsavvy.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object VersionManager {

    /**
     * Get current app version name
     */
    fun getCurrentVersion(context: Context): String {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            packageInfo.versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    /**
     * Compare two version strings
     * Returns:
     * - Negative if version1 < version2
     * - Zero if version1 == version2
     * - Positive if version1 > version2
     */
    fun compareVersions(version1: String, version2: String): Int {
        val v1Parts = version1.split(".").map { it.toIntOrNull() ?: 0 }
        val v2Parts = version2.split(".").map { it.toIntOrNull() ?: 0 }

        val maxLength = maxOf(v1Parts.size, v2Parts.size)

        for (i in 0 until maxLength) {
            val v1Part = v1Parts.getOrNull(i) ?: 0
            val v2Part = v2Parts.getOrNull(i) ?: 0

            if (v1Part != v2Part) {
                return v1Part.compareTo(v2Part)
            }
        }

        return 0
    }

    /**
     * Check if current version is less than required version
     */
    fun isUpdateRequired(currentVersion: String, minRequiredVersion: String): Boolean {
        return compareVersions(currentVersion, minRequiredVersion) < 0
    }

    /**
     * Check if update is available
     */
    fun isUpdateAvailable(currentVersion: String, latestVersion: String): Boolean {
        return compareVersions(currentVersion, latestVersion) < 0
    }
}
