plugins {
    id("common-conventions")
}

description = "Extended library building on library-a functionality"

dependencies {
    // Dependency auf library-a (innerhalb des Monorepos)
    implementation(project(":library-a"))
    
    // Test dependencies
    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
