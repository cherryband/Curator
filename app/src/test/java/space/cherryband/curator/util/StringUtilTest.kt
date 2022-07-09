package space.cherryband.curator.util

import org.junit.Test
import org.junit.Assert.*

class StringUtilTest {
    @Test
    fun testRandomShrug() {
        val randomShrugs = List(100) { randomShrug() }
        val distinctRandShrugs = randomShrugs.toSet()
        print("distinct random shrugs: $distinctRandShrugs")
        assertTrue(distinctRandShrugs.size > 3)
    }
}