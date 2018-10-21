import org.jetbrains.kotlin.gradle.frontend.Bundler
import org.jetbrains.kotlin.gradle.frontend.FrontendPlugin
import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.config.BundleConfig
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackBundler
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

buildscript {
    val kotlinVersion = "1.2.71"
    val serializationVersion = "0.6.2"

    repositories {
        jcenter()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:$serializationVersion")
        classpath("org.jetbrains.kotlin:kotlin-frontend-plugin:0.0.37")
    }
}

val releaseVersion : String by project

group = "eu.rigeldev.uirig"
version = releaseVersion

plugins {
    java
}

apply {
    plugin("kotlin2js")
    plugin("kotlinx-serialization")
}
plugins.apply(FrontendPlugin::class.java)

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "https://dl.bintray.com/teyckmans/rigeldev-oss-maven")
}

val kotlinVersion = "1.2.71"
val serializationVersion = "0.6.2"

dependencies {
    compile("eu.rigeldev.uirig:ui-rig-core:0.0.4")
    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion")
}

tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            metaInfo = true
            outputFile = "${project.buildDir.path}/js/${project.name}.js"
            sourceMap = true
            sourceMapEmbedSources = "always"
            moduleKind = "commonjs"
            main = "call"
        }
    }
}

configure<KotlinFrontendExtension> {
    downloadNodeJsVersion = "10.10.0"

    sourceMaps = true

    bundle("webpack", delegateClosureOf<WebPackExtension>{
        bundleName = "main"

        contentPath = file(File(project.projectDir, "src/main/web"))
    })
}

configure<NpmExtension> {

    dependency("ui-rig-core", "0.0.4")
    dependency("snabbdom", "0.7.2")
    dependency("style-loader")

    devDependency("karma")
}