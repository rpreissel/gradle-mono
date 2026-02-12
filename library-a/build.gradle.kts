plugins {
    id("common-conventions")
}

description = "Core library providing mathematical utilities"

dependencies {
    // Test dependencies werden über den Version Catalog geladen
    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
