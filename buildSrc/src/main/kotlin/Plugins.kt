object Plugins {
    object Versions {
        const val detekt = "1.23.1"
        const val kotlin_serialization = "1.9.0"
    }

    object Paths {
        const val detekt_config = "../../tools/detekt/detekt.yml"
    }

    const val kotlin_jvm = "org.jetbrains.kotlin.jvm"
    const val kotlin_serialization = "plugin.serialization"

    const val testFixtures = "java-test-fixtures"

    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detekt_formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
}
