pluginManagement {
    repositories {
        google()  // Add Google repository
        mavenCentral()  // Add Maven Central repository
        gradlePluginPortal()  // Gradle Plugin Portal for Gradle plugins
    }
}

dependencyResolutionManagement {
    repositories {
        google()  // Add Google repository
        mavenCentral()  // Add Maven Central repository
    }
}

rootProject.name = "SignIn_SignUp"
include(":app")  // Include the app module
