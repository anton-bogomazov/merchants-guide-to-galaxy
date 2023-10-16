import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forExactly
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe

class SampleTest : StringSpec({
    "sample test" {
        2 + 2 shouldBe 4
        listOf(1, 2, 1).forExactly(3) { it shouldBeIn listOf(1, 2) }
    }
})
