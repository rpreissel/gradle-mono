# Agent Guidelines for gradle-mono

This document provides essential information for AI coding agents working in this Gradle monorepo with independent semantic versioning per subproject.

## Project Overview

- **Type**: Gradle multi-project monorepo (Java libraries)
- **Language**: Java 21
- **Build Tool**: Gradle 9.3.1 with Kotlin DSL
- **Versioning**: Git-based semantic versioning via Axion Release Plugin
- **Testing**: JUnit 5, AssertJ, Mockito
- **Code Quality**: Spotless (Google Java Format), Checkstyle

## Build Commands

```bash
# Build entire project
./gradlew build

# Build specific subproject
./gradlew library-a:build

# Clean build
./gradlew clean build

# Build without tests
./gradlew build -x test
```

## Test Commands

```bash
# Run all tests
./gradlew test

# Run tests for specific project
./gradlew library-a:test

# Run single test class
./gradlew library-a:test --tests "CalculatorTest"

# Run single test method
./gradlew library-a:test --tests "CalculatorTest.testAdd"

# Run tests with pattern matching
./gradlew library-a:test --tests "*Calculator*"

# Continuous testing (re-run on changes)
./gradlew library-a:test --continuous
```

## Linting & Code Quality

```bash
# Check code formatting (fails if violations found)
./gradlew spotlessCheck

# Auto-fix code formatting
./gradlew spotlessApply

# Run Checkstyle
./gradlew checkstyleMain checkstyleTest

# Run all checks (includes spotless, checkstyle, tests)
./gradlew check

# Check for outdated dependencies
./gradlew dependencyUpdates
```

## Code Style Guidelines

### Java Formatting
- **Style**: Google Java Format (enforced via Spotless)
- **Indentation**: 2 spaces (no tabs)
- **Line Length**: 100 characters (Google standard)
- **Always run**: `./gradlew spotlessApply` before committing

### Import Rules
- Static imports first, then regular imports
- No wildcard imports (enforced by Spotless)
- Unused imports automatically removed
- Order: java.*, javax.*, org.*, com.*, static imports

### Naming Conventions
- **Classes**: PascalCase (e.g., `Calculator`, `ArrayUtils`)
- **Methods**: camelCase (e.g., `calculateSum`, `findMaxValue`)
- **Variables**: camelCase (e.g., `totalCount`, `isValid`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_SIZE`, `DEFAULT_VALUE`)
- **Test Methods**: camelCase with descriptive names (e.g., `testAddWithPositiveNumbers`)
- **Packages**: lowercase, no underscores (e.g., `com.example.mono.librarya`)

### File Headers
All Java files MUST start with:
```java
/*
 * Copyright 2024 Example Organization
 * Licensed under the Apache License, Version 2.0
 */
```

### Documentation
- **Public APIs**: MUST have Javadoc with description, @param, @return, @throws
- **Package-private**: Javadoc recommended
- **Private methods**: Optional but encouraged
- Use `<p>` tags for paragraph breaks in Javadoc

Example:
```java
/**
 * Calculates the sum of an array.
 *
 * <p>This method handles null and empty arrays gracefully.
 *
 * @param numbers array of integers to sum
 * @return sum of all elements, or 0 for empty/null array
 * @throws IllegalArgumentException if array contains invalid values
 */
```

### Error Handling
- Use specific exception types (not generic `Exception`)
- Validate input parameters early
- Throw `IllegalArgumentException` for invalid arguments
- Throw `NullPointerException` for null checks (or use `Objects.requireNonNull`)
- Include descriptive error messages

Example:
```java
if (array == null || array.length == 0) {
  throw new IllegalArgumentException("Array must not be null or empty");
}
```

### Testing Standards
- Use JUnit 5 (`@Test`, `@BeforeEach`, `@DisplayName`)
- Use AssertJ for assertions (`assertThat(...).isEqualTo(...)`)
- Use `@DisplayName` for readable test descriptions
- Use `@ParameterizedTest` with `@CsvSource` for multiple inputs
- Test structure: Given-When-Then or Arrange-Act-Assert
- Test both happy path and error cases

Example:
```java
@Test
@DisplayName("Division by zero should throw ArithmeticException")
void testDivideByZero() {
  assertThatThrownBy(() -> calculator.divide(10, 0))
      .isInstanceOf(ArithmeticException.class)
      .hasMessageContaining("Division by zero");
}
```

## Version Control & Commits

### Conventional Commits (REQUIRED)
This project uses **Conventional Commits** for automatic semantic versioning:

```bash
# Format: <type>(<scope>): <description>

# Patch version bump (0.1.0 → 0.1.1)
git commit -m "fix(library-a): correct null pointer in Calculator"

# Minor version bump (0.1.0 → 0.2.0)
git commit -m "feat(library-a): add square root method"

# Major version bump (0.1.0 → 1.0.0)
git commit -m "feat(library-a)!: change API signature

BREAKING CHANGE: divide() now throws exception instead of returning -1"

# No version bump
git commit -m "docs(library-a): update Javadoc"
git commit -m "test(library-a): add more test cases"
git commit -m "refactor(library-a): simplify calculation logic"
```

**Commit Types**:
- `feat`: New feature (minor bump)
- `fix`: Bug fix (patch bump)
- `docs`: Documentation only
- `test`: Adding/updating tests
- `refactor`: Code refactoring
- `chore`: Build/tooling changes
- `!` suffix or `BREAKING CHANGE:` footer for major bump

## Adding New Code

### Adding a New Method
1. Write the method with Javadoc
2. Add unit tests (minimum 80% coverage)
3. Run `./gradlew spotlessApply`
4. Run `./gradlew library-x:test`
5. Commit with appropriate conventional commit message

### Adding a New Class
1. Create class in appropriate package (e.g., `com.example.mono.librarya`)
2. Add license header
3. Write comprehensive Javadoc
4. Create corresponding test class in `src/test/java`
5. Follow all style guidelines above

### Adding a New Subproject
See README.md "Development Workflow" section for detailed steps.

## Project-Specific Rules

1. **Each subproject is independently versioned** - changes to `library-a` don't affect `library-b` version
2. **No cross-project imports** except via Gradle dependencies (declared in `build.gradle.kts`)
3. **All dependencies** should be declared in `gradle/libs.versions.toml` (Version Catalog)
4. **Tests must pass** before pushing - CI will reject failing builds
5. **Code formatting is enforced** - CI will fail on formatting violations

## Quick Checklist Before Commit

- [ ] Code follows Google Java Format (`./gradlew spotlessApply`)
- [ ] All tests pass (`./gradlew test`)
- [ ] Checkstyle violations resolved (`./gradlew check`)
- [ ] Javadoc added for public APIs
- [ ] License header present in new files
- [ ] Conventional commit message format used
- [ ] Tests added/updated for new functionality

## Common Mistakes to Avoid

1. ❌ Using wildcard imports (`import java.util.*`)
2. ❌ Missing Javadoc on public methods
3. ❌ Non-conventional commit messages (breaks versioning)
4. ❌ Skipping `spotlessApply` before commit
5. ❌ Adding dependencies without using Version Catalog
6. ❌ Hardcoding versions in build files
7. ❌ Using `System.out.println` instead of proper logging
8. ❌ Catching generic `Exception` instead of specific types
