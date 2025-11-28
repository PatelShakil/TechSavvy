package com.shakilpatel.techsavvy.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class VersionConfig(
    val minRequiredVersion: String = "1.0.0",
    val latestVersion: String = "1.0.0",
    val forceUpdate: Boolean = false,
    val updateMessage: String = "A new version is available!",
    val criticalUpdateMessage: String = "This version is no longer supported. Please update to continue.",
    val playStoreUrl: String = "https://play.google.com/store/apps/details?id=com.shakilpatel.techsavvy"
)

@Singleton
class VersionRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getVersionConfig(): Result<VersionConfig> {
        return try {
            val document = firestore.collection("appConfig")
                .document("versionControl")
                .get()
                .await()

            val config = document.toObject(VersionConfig::class.java)
                ?: VersionConfig()

            Result.success(config)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
