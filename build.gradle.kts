buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.3")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.57")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.2.0-2.0.2")
    }
}

plugins {
    id("com.android.application") version "8.12.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("com.google.gms.google-services") version "4.4.3" apply false
    id("com.google.dagger.hilt.android") version "2.57" apply false
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
    id("de.mannodermaus.android-junit5") version "1.13.1.0" apply false
    alias(libs.plugins.compose.compiler) apply false
}