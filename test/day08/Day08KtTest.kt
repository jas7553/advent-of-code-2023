package day08

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day08KtTest {
    @Nested
    inner class LeftRightGenerator {
        @Test
        fun `just L then R`() {
            leftRightGenerator("LR")
                .forEachIndexed { i, c ->
                    assertEquals(if (i % 2 == 0) Move.Left else Move.Right, c)
                    if (i > 10) return
                }
        }
    }
}
