# Gradle Monorepo mit automatischer semantischer Versionierung

Ein produktionsreifes Gradle-Monorepo-Setup mit unabhängiger semantischer Versionierung für jedes Subprojekt und automatischen Releases via GitHub Actions.

## 🎯 Features

- ✅ **Unabhängige Versionierung**: Jedes Subprojekt wird autark semantisch versioniert
- ✅ **Git-basierte Versionen**: Automatische Versionsnummern basierend auf Conventional Commits
- ✅ **Automatische Releases**: GitHub Actions erstellt Releases nur für geänderte Projekte
- ✅ **Code Quality**: Spotless (Auto-Formatting) + Checkstyle
- ✅ **Testing**: JUnit 5 + AssertJ + Mockito
- ✅ **Dependency Management**: Zentraler Version Catalog
- ✅ **Publishing**: Maven Local/Remote & GitHub Packages

## 📁 Projekt-Struktur

```
gradle-mono/
├── .github/
│   └── workflows/
│       ├── ci.yml              # Build & Test bei jedem Push
│       └── release.yml         # Automatisches Release geänderter Projekte
├── buildSrc/                   # Convention Plugins (shared build logic)
│   └── src/main/kotlin/
│       └── common-conventions.gradle.kts
├── config/
│   ├── checkstyle/            # Checkstyle-Regeln
│   └── spotless/              # Code-Format-Regeln
├── gradle/
│   └── libs.versions.toml     # Zentrale Dependency-Versionen
├── library-a/                 # Beispiel-Library (unabhängig versioniert)
│   ├── build.gradle.kts
│   └── src/...
├── library-b/                 # Beispiel-Library (abhängig von library-a)
│   ├── build.gradle.kts
│   └── src/...
├── build.gradle.kts           # Root build config
├── settings.gradle.kts        # Multi-Projekt Setup
└── gradlew                    # Gradle Wrapper
```

## 🚀 Quick Start

### Projekt bauen

```bash
# Alle Projekte bauen
./gradlew build

# Nur ein Projekt bauen
./gradlew library-a:build

# Tests ausführen
./gradlew test

# Code formatieren
./gradlew spotlessApply
```

### Versionen anzeigen

```bash
# Version eines einzelnen Projekts
./gradlew library-a:currentVersion

# Alle Versionen anzeigen
./gradlew printAllVersions
```

## 📦 Versionierung & Releases

### Wie funktioniert die Versionierung?

Dieses Projekt nutzt **Axion Release Plugin** mit Git-Tags für semantische Versionierung:

- **Tag-Format**: `<projektname>/v<version>` (z.B. `library-a/v1.2.3`)
- **Conventional Commits** bestimmen den Version-Bump:

| Commit-Typ | Beispiel | Version-Änderung |
|-----------|----------|-----------------|
| `fix:` | `fix(library-a): korrigiere NPE` | Patch: `1.0.0` → `1.0.1` |
| `feat:` | `feat(library-a): neue Methode` | Minor: `1.0.0` → `1.1.0` |
| `BREAKING CHANGE:` | `feat!: ändere API` | Major: `1.0.0` → `2.0.0` |

### Manuelles Release erstellen

```bash
# Release für ein Projekt erstellen
./gradlew library-a:release

# Mit spezifischem Version-Type
./gradlew library-a:release -Prelease.versionIncrementer=incrementMinor
```

### Automatisches Release (GitHub Actions)

Bei jedem Push auf `main`:

1. **Änderungserkennung**: Workflow erkennt geänderte Subprojekte
2. **Build & Test**: Alle Tests werden ausgeführt
3. **Version-Tag**: Neuer Git-Tag wird erstellt (z.B. `library-a/v1.2.3`)
4. **GitHub Release**: Release mit JARs wird veröffentlicht
5. **Publish**: Artifacts werden zu GitHub Packages hochgeladen

**Wichtig**: Nutze Conventional Commits für automatische Versionierung!

```bash
# Beispiel-Commits
git commit -m "feat(library-a): add divide method"
git commit -m "fix(library-b): handle null input correctly"
git commit -m "feat(library-a)!: change API signature

BREAKING CHANGE: method now requires two parameters"
```

## 🛠️ Development Workflow

### 1. Neues Feature entwickeln

```bash
# Feature-Branch erstellen
git checkout -b feature/add-square-root

# Code ändern in library-a
# ...

# Committen mit Conventional Commits
git commit -m "feat(library-a): add square root calculation"

# Push & Pull Request
git push origin feature/add-square-root
```

### 2. Nach Merge in `main`

GitHub Actions:
- Erkennt Änderung in `library-a`
- Baut `library-a` (und Tests laufen)
- Erstellt automatisch neuen Tag `library-a/v0.2.0` (Minor-Bump wegen `feat:`)
- Veröffentlicht Release auf GitHub
- Publiziert zu GitHub Packages

### 3. Neue Library hinzufügen

```bash
# 1. Verzeichnis erstellen
mkdir library-c
mkdir -p library-c/src/{main,test}/java/com/example/mono/libraryc

# 2. build.gradle.kts erstellen
cat > library-c/build.gradle.kts << 'EOF'
plugins {
    id("common-conventions")
}

description = "New library description"

dependencies {
    // Optional: Dependency auf andere Library
    // implementation(project(":library-a"))
    
    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
EOF

# 3. In settings.gradle.kts registrieren
echo 'include("library-c")' >> settings.gradle.kts

# 4. Code hinzufügen und bauen
./gradlew library-c:build
```

## 📚 Nützliche Gradle Tasks

```bash
# Code-Qualität
./gradlew spotlessCheck          # Code-Format prüfen
./gradlew spotlessApply          # Code formatieren
./gradlew checkstyleMain         # Checkstyle ausführen

# Dependencies
./gradlew dependencyUpdates      # Veraltete Dependencies finden
./gradlew dependencies          # Dependency-Tree anzeigen

# Publishing
./gradlew publishToMavenLocal    # Lokal publishen zum Testen
./gradlew publish                # Zu Remote-Repository publishen

# Versionierung
./gradlew currentVersion         # Aktuelle Version
./gradlew verifyRelease         # Release-Vorbereitung prüfen
./gradlew release               # Release erstellen
```

## 🔧 Konfiguration anpassen

### GitHub Packages Publishing

Um nach GitHub Packages zu publishen, passe `common-conventions.gradle.kts` an:

```kotlin
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/DEIN-USERNAME/gradle-mono")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

### Java-Version ändern

In `buildSrc/src/main/kotlin/common-conventions.gradle.kts`:

```kotlin
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Oder andere Version
    }
}
```

### Dependencies aktualisieren

Zentrale Verwaltung in `gradle/libs.versions.toml`:

```toml
[versions]
junit-jupiter = "5.11.3"  # Neue Version hier eintragen

[libraries]
junit-jupiter-api = { module = "org.junit.jupiter:junit-jupiter-api", version.ref = "junit-jupiter" }
```

## 📖 Best Practices

### Commit-Messages

Nutze **Conventional Commits** für automatische Versionierung:

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types:**
- `feat:` - Neues Feature (Minor-Bump)
- `fix:` - Bugfix (Patch-Bump)
- `docs:` - Dokumentation (kein Bump)
- `refactor:` - Code-Refactoring (kein Bump)
- `test:` - Tests hinzufügen (kein Bump)
- `chore:` - Build/Tools-Änderungen (kein Bump)

**Breaking Changes:**
```
feat(library-a)!: change calculator API

BREAKING CHANGE: divide() now throws exception instead of returning -1
```

### Testing

- Schreibe Tests für alle öffentlichen APIs
- Nutze `@DisplayName` für lesbare Test-Namen
- Parametrisierte Tests mit `@ParameterizedTest`

### Dependencies

- Vermeide zyklische Dependencies zwischen Subprojekten
- Nutze `implementation` statt `api` wo möglich
- Teste neue Dependencies lokal mit `publishToMavenLocal`

## 🤝 Contributing

1. Fork das Repository
2. Erstelle einen Feature-Branch
3. Committe mit Conventional Commits
4. Öffne einen Pull Request

## 📄 Lizenz

Apache License 2.0

## 🎓 Weitere Informationen

- [Gradle Multi-Project Builds](https://docs.gradle.org/current/userguide/multi_project_builds.html)
- [Axion Release Plugin](https://github.com/allegro/axion-release-plugin)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)
