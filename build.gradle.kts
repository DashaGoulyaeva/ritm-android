import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.tasks.testing.Test
import java.io.File

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

val externalBuildRoot = File(
    System.getenv("LOCALAPPDATA") ?: System.getProperty("java.io.tmpdir"),
    "ritm-android-build"
)

subprojects {
    layout.buildDirectory.set(externalBuildRoot.resolve(name))

    pluginManager.withPlugin("org.jetbrains.kotlin.android") {
        pluginManager.apply("io.gitlab.arturbosch.detekt")
        pluginManager.apply("org.jlleitschuh.gradle.ktlint")
    }

    tasks.withType<Test>().configureEach {
        useJUnit()
    }

    tasks.withType<Detekt>().configureEach {
        buildUponDefaultConfig = true
        config.setFrom(rootProject.file("detekt.yml"))
    }
}

tasks.register("qualityCheck") {
    group = "verification"
    description = "Runs lint, ktlint, detekt, and unit tests for all modules."

    dependsOn(subprojects.map { "${it.path}:lint" })
    dependsOn(subprojects.map { "${it.path}:ktlintCheck" })
    dependsOn(subprojects.map { "${it.path}:detekt" })
    dependsOn(subprojects.map { "${it.path}:testDebugUnitTest" })
}
