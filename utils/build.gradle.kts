plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xexpect-actual-classes")
    }

    androidLibrary {
        namespace = "com.pr0gramm3r101.utils"
        compileSdk = 35
        minSdk = 24
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "utils"
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.constraintlayout.compose.multiplatform)
            implementation(compose.materialIconsExtended)
        }

        androidMain.dependencies {
            implementation(libs.androidx.adaptive.android)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.material)
            implementation(libs.biometric)
            implementation(libs.gson)

            implementation(libs.androidx.adaptive.android)
            implementation(libs.datastore.preferences)
            implementation(libs.datastore.core)
        }

        iosMain.dependencies {
            // nothing
        }

        desktopMain.dependencies {
            // nothing
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "ru.denis0001dev.utils"
    generateResClass = auto
}