/**
 * Hauptkonventions-Plugin für alle Projekte im Monorepo.
 * Wendet automatisch die richtigen Konventionen basierend auf dem Projekttyp an:
 * - Root-Projekt: root-conventions
 * - Subprojekte: java-library-conventions
 */

if (project == rootProject) {
    apply(plugin = "root-conventions")
} else {
    apply(plugin = "java-library-conventions")
}
