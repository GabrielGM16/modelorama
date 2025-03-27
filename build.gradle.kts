// Top-level build file
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        // Removed Glide dependencies - they don't belong in buildscript
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}