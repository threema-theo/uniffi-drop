import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "example.com"
    compileSdk = 36

    defaultConfig {
        applicationId = "example.com"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.jna)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

tasks.withType<Test> {
    // Necessary to load the dynamic libthreema library in unit tests
    systemProperty("jna.library.path", "${project.projectDir}/../../rust/target/debug")
}

afterEvaluate {
    val bindingsDirectory = "../kotlin/app/src/main/java/"

    val generateLibrust =
            tasks.register<Exec>("generateLibrust") {
                workingDir("${project.projectDir}/../../rust")
                commandLine("pwd")
                commandLine("cargo", "build", "-p", "librust")
            }
    // Define the task to generate the uniffi bindings for librust
    val uniffiBindings =
            tasks.register("generateUniFFIBindings") {
                dependsOn(generateLibrust)
                doLast {
                    // It seems that the uniffi packagze generates a "*.so" file on linux and a
                    // "*.dylib" on mac
                    // while using the cargo build command from the gradle task above
                    // ("generateLibrust").
                    val uniffiLibraryFilePathPrefix =
                            "${project.projectDir}/../../rust/target/debug/liblibrust"
                    val uniffiLibraryFile =
                            file("$uniffiLibraryFilePathPrefix.so").takeIf { it.exists() }
                                    ?: file("$uniffiLibraryFilePathPrefix.dylib")
                    assert(uniffiLibraryFile.exists()) {
                        "Error: Missing pre-generated uniffy library file in rust/target/*/ directory.\n"
                    }

                    val processBuilder =
                            ProcessBuilder( "./diplomat-build.sh")
                    processBuilder.directory(file("${project.projectDir}/../"))
                    processBuilder.start().waitFor()
                }
                outputs.dir(bindingsDirectory)
            }

    tasks["compileDebugKotlin"].dependsOn(uniffiBindings)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin { compilerOptions { jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11 } }
