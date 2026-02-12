/*
 * Root Build-Konfiguration für Gradle Monorepo
 * Definiert gemeinsame Eigenschaften für alle Subprojekte
 */

plugins {
    id("com.github.ben-manes.versions")
}

// Gemeinsame Eigenschaften für alle Subprojekte
allprojects {
    group = "com.example.mono"
}

// Konfiguration für alle Subprojekte
subprojects {
    // Zusätzliche gemeinsame Konfiguration kann hier hinzugefügt werden
}

// Task zum Anzeigen aller Projekt-Versionen
tasks.register("printAllVersions") {
    group = "help"
    description = "Zeigt die Versionen aller Subprojekte an"
    
    doLast {
        println("\n=== Projekt-Versionen ===")
        subprojects.forEach { subproject ->
            println("${subproject.name}: ${subproject.version}")
        }
        println("========================\n")
    }
}

// Task zum Prüfen auf veraltete Dependencies
tasks.named("dependencyUpdates") {
    group = "help"
    description = "Zeigt veraltete Dependencies an"
}


