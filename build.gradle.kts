// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath (libs.hilt.android.gradle.plugin)
        classpath(libs.org.jetbrains.kotlin.plugin.compose.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" // this version matches your Kotlin version
}