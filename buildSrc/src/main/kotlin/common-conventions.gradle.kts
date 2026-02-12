import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.tasks.testing.logging.TestLogEvent

/**
 * Gemeinsame Konventionen für alle Java-Library-Projekte im Monorepo.
 * Dieses Plugin wird von allen Subprojekten angewendet.
 */
plugins {
    `java-library`
    `maven-publish`
    checkstyle
    id("com.diffplug.spotless")
    id("pl.allegro.tech.build.axion-release")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

// Axion Release Konfiguration für unabhängige Subprojekt-Versionierung
scmVersion {
    // Tag-Format: <projektname>/v<version>
    // Beispiel: library-a/v1.2.3
    tag.apply {
        prefix = "${project.name}/v"
        versionSeparator = ""
    }
    
    // Verwende semantische Versionierung basierend auf Conventional Commits
    versionCreator("simple")
    
    // Zeige untagged Version als SNAPSHOT
    useHighestVersion = true
}

// Setze Projektversion aus Axion
project.version = scmVersion.version

// Testing Konfiguration
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

tasks.test {
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = false
    }
}

// Spotless - Code Formatierung
configure<SpotlessExtension> {
    java {
        googleJavaFormat("1.23.0")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
        
        // Lizenz-Header (optional)
        licenseHeaderFile(rootProject.file("config/spotless/license-header.txt"))
            .onlyIfContentMatches("^(?!\\/\\*\\*)") // Nur wenn kein Javadoc
    }
}

// Checkstyle Konfiguration
checkstyle {
    toolVersion = "10.20.2"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    maxWarnings = 0
}

// Maven Publishing Konfiguration
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            
            pom {
                name.set(project.name)
                description.set(project.description ?: "A library from gradle-mono")
                url.set("https://github.com/yourorg/gradle-mono")
                
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                
                developers {
                    developer {
                        id.set("dev")
                        name.set("Development Team")
                        email.set("dev@example.com")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/yourorg/gradle-mono.git")
                    developerConnection.set("scm:git:ssh://github.com/yourorg/gradle-mono.git")
                    url.set("https://github.com/yourorg/gradle-mono")
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/yourorg/gradle-mono")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

// Hilfreiche Tasks
tasks.register("printVersion") {
    group = "help"
    description = "Zeigt die aktuelle Version des Projekts an"
    doLast {
        println("${project.name}: ${project.version}")
    }
}
