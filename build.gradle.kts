// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    kotlin("kapt") version "1.8.20" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath ("com.google.gms:google-services:4.3.15")

    }
}