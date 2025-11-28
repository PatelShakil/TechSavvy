package com.shakilpatel.techsavvy.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shakilpatel.techsavvy.data.repository.VersionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirestoreDB() = FirebaseFirestore.getInstance()

    @Provides
    fun provideRealtimeDB() = FirebaseDatabase.getInstance()

    @Provides
    fun provideStorage() = FirebaseStorage.getInstance()

    @Provides
    fun provideAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("student_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @Provides
    @Singleton
    fun provideVersionRepository(firestore: FirebaseFirestore): VersionRepository {
        return VersionRepository(firestore)
    }
}