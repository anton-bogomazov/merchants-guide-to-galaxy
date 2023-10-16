object Plugins {
    object Versions {
        const val detekt = "1.23.1"
    }

    object Paths {
        const val detekt_config = "../tools/detekt/detekt.yml"
    }

    const val kotlin_jvm = "org.jetbrains.kotlin.jvm"

    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detekt_formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
}
