package space.cherryband.curator.data

import org.junit.Test
import org.junit.Assert.*

internal class DataKtTest {

    @Test
    fun dicedPath() {
        assertArrayEquals(arrayOf("a", "b", "c"), dicedPath("a/b/c").toTypedArray())
        assertArrayEquals(arrayOf("","a", "b", "c"), dicedPath("/a/b/c").toTypedArray())
        assertArrayEquals(arrayOf("a", "b", "c"), dicedPath("a/b/c/").toTypedArray())
        assertArrayEquals(arrayOf("a", "d"), dicedPath("a/d").toTypedArray())
    }
}