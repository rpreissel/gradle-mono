plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Plugin-Dependencies mit expliziten Versionen
    implementation("pl.allegro.tech.build:axion-release-plugin:${libs.versions.axion.release.get()}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${libs.versions.spotless.get()}")
    implementation("com.github.ben-manes:gradle-versions-plugin:${libs.versions.versions.plugin.get()}")
}
