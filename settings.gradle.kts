/*
 * Multi-Projekt Build für Gradle Monorepo
 * Jedes Subprojekt wird unabhängig versioniert und kann separat released werden.
 */

rootProject.name = "gradle-mono"

// Plugin Management - alle Plugins zentral verfügbar machen
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

// Dependency Resolution Management
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

// Subprojekte einbinden
include("library-a")
include("library-b")
