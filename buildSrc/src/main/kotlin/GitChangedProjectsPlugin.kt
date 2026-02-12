/*
 * Copyright 2024 Example Organization
 * Licensed under the Apache License, Version 2.0
 */

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * Gradle Plugin to detect which subprojects have changed since the last Git tag.
 *
 * <p>This plugin analyzes Git history and provides tasks to:
 *
 * <ul>
 *   <li>List changed subprojects
 *   <li>Show changed files
 * </ul>
 */
class GitChangedProjectsPlugin : Plugin<Project> {

  override fun apply(rootProject: Project) {
    if (rootProject != rootProject.rootProject) {
      throw IllegalStateException("GitChangedProjectsPlugin must be applied to root project")
    }

    // Create extension for configuration
    rootProject.extensions.create("gitChangedProjects", GitChangedProjectsExtension::class.java)

    // Register task to list changed projects
    rootProject.tasks.register("listChangedProjects") {
      group = "versioning"
      description = "Lists all subprojects that have changed since the last release"
      
      notCompatibleWithConfigurationCache("Uses project state at execution time")

      doLast {
        val changedProjects = getChangedProjects(rootProject)
        println("\n=== Changed Projects ===")
        if (changedProjects.isEmpty()) {
          println("No changes detected")
        } else {
          changedProjects.forEach { println("  - ${it.name}") }
        }
        println("========================\n")
      }
    }

    // Register task to list changed projects as JSON (for CI/CD)
    rootProject.tasks.register("listChangedProjectsJson") {
      group = "versioning"
      description = "Lists changed subprojects as JSON array (for CI/CD)"
      
      notCompatibleWithConfigurationCache("Uses project state at execution time")

      doLast {
        val changedProjects = getChangedProjects(rootProject)
        val projectNames = changedProjects.map { it.name }
        
        // Output als JSON-Array für einfaches Parsing in CI
        val json = projectNames.joinToString(",", "[", "]") { "\"$it\"" }
        println(json)
      }
    }

    // Register task to show which files changed
    rootProject.tasks.register("showChangedFiles") {
      group = "versioning"
      description = "Shows which files have changed since the last release"
      
      notCompatibleWithConfigurationCache("Uses project state at execution time")

      doLast {
        val lastTag = getLastGitTag(rootProject.rootDir)
        val changedFiles = getChangedFilesSinceTag(rootProject.rootDir, lastTag)
        val unstagedFiles = getUnstagedFiles(rootProject.rootDir)
        
        val allFiles = (changedFiles + unstagedFiles).distinct()
        
        println("\n=== Changed Files (since $lastTag) ===")
        if (allFiles.isEmpty()) {
          println("No changes detected")
        } else {
          allFiles.forEach { println("  $it") }
        }
        println("========================================\n")
      }
    }

    // CI/CD Helper Task: Build only changed projects
    rootProject.tasks.register("buildChangedProjects") {
      group = "build"
      description = "Builds only changed subprojects (CI/CD optimized)"
      
      notCompatibleWithConfigurationCache("Uses project state at execution time")

      doLast {
        val changedProjects = getChangedProjects(rootProject)
        
        if (changedProjects.isEmpty()) {
          println("No changed projects detected")
        } else {
          println("Changed projects: ${changedProjects.joinToString(", ") { it.name }}")
          changedProjects.forEach { project ->
            println("Building ${project.name}...")
            project.tasks.getByName("build").actions.forEach { action ->
              action.execute(project.tasks.getByName("build"))
            }
          }
        }
      }
    }

    // CI/CD Helper Task: Test only changed projects  
    rootProject.tasks.register("testChangedProjects") {
      group = "verification"
      description = "Tests only changed subprojects (CI/CD optimized)"
      
      notCompatibleWithConfigurationCache("Uses project state at execution time")

      doLast {
        val changedProjects = getChangedProjects(rootProject)
        
        if (changedProjects.isEmpty()) {
          println("No changed projects detected")
        } else {
          println("Testing changed projects: ${changedProjects.joinToString(", ") { it.name }}")
        }
      }
    }

    // GitHub Actions Output Task
    rootProject.tasks.register("ciSetChangedProjects") {
      group = "ci"
      description = "Outputs changed projects for GitHub Actions (sets GITHUB_OUTPUT)"
      
      notCompatibleWithConfigurationCache("Uses project state at execution time")

      doLast {
        val changedProjects = getChangedProjects(rootProject)
        val projectNames = changedProjects.map { it.name }
        val json = projectNames.joinToString(",", "[", "]") { "\"$it\"" }
        
        // GitHub Actions output format
        val githubOutput = System.getenv("GITHUB_OUTPUT")
        if (githubOutput != null) {
          File(githubOutput).appendText("projects=$json\n")
          File(githubOutput).appendText("has-changes=${projectNames.isNotEmpty()}\n")
          println("Set GitHub Actions output: projects=$json")
        } else {
          // Local development fallback
          println("projects=$json")
          println("has-changes=${projectNames.isNotEmpty()}")
        }
      }
    }
  }

  /**
   * Detects which subprojects have changed since the last Git tag.
   *
   * @param rootProject the root Gradle project
   * @return list of changed subprojects
   */
  private fun getChangedProjects(rootProject: Project): List<Project> {
    val lastTag = getLastGitTag(rootProject.rootDir)
    val changedFiles = getChangedFilesSinceTag(rootProject.rootDir, lastTag)
    val unstagedFiles = getUnstagedFiles(rootProject.rootDir)
    
    val allChangedFiles = (changedFiles + unstagedFiles).distinct()

    return rootProject.subprojects.filter { subproject ->
      val subprojectPath = subproject.projectDir.relativeTo(rootProject.projectDir).path
      allChangedFiles.any { file -> file.startsWith(subprojectPath) }
    }
  }

  /**
   * Gets the most recent Git tag across all subprojects.
   *
   * @param workingDir the working directory
   * @return the last Git tag, or "HEAD" if no tags exist
   */
  private fun getLastGitTag(workingDir: File): String {
    return try {
      val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0")
        .directory(workingDir)
        .redirectErrorStream(true)
        .start()
      
      val output = process.inputStream.bufferedReader().readText().trim()
      process.waitFor()
      
      if (process.exitValue() == 0 && output.isNotEmpty()) output else "HEAD"
    } catch (e: Exception) {
      "HEAD"
    }
  }

  /**
   * Gets all files that changed since a given Git tag or commit.
   *
   * @param workingDir the working directory
   * @param since the Git reference to compare against
   * @return list of changed file paths
   */
  private fun getChangedFilesSinceTag(workingDir: File, since: String): List<String> {
    return try {
      val compareRef = if (since == "HEAD") "HEAD~1" else since
      
      val process = ProcessBuilder("git", "diff", "--name-only", compareRef, "HEAD")
        .directory(workingDir)
        .redirectErrorStream(true)
        .start()
      
      val output = process.inputStream.bufferedReader().readText().trim()
      process.waitFor()
      
      if (process.exitValue() == 0 && output.isNotEmpty()) {
        output.split("\n").filter { it.isNotBlank() }
      } else {
        emptyList()
      }
    } catch (e: Exception) {
      emptyList()
    }
  }

  /**
   * Gets all unstaged and uncommitted files.
   *
   * @param workingDir the working directory
   * @return list of unstaged file paths
   */
  private fun getUnstagedFiles(workingDir: File): List<String> {
    return try {
      val process = ProcessBuilder("git", "diff", "--name-only")
        .directory(workingDir)
        .redirectErrorStream(true)
        .start()
      
      val output = process.inputStream.bufferedReader().readText().trim()
      process.waitFor()
      
      if (process.exitValue() == 0 && output.isNotEmpty()) {
        output.split("\n").filter { it.isNotBlank() }
      } else {
        emptyList()
      }
    } catch (e: Exception) {
      emptyList()
    }
  }
}

/**
 * Extension for configuring the GitChangedProjects plugin.
 *
 * <p>Currently a placeholder for future configuration options.
 */
open class GitChangedProjectsExtension {
  // Placeholder for future configuration options
  // Example: var baseBranch: String = "main"
}
