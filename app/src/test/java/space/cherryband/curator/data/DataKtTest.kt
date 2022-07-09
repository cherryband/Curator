package space.cherryband.curator.data

import org.junit.Test
import org.junit.Assert.*
import space.cherryband.curator.util.diced

internal class DataKtTest {
    @Test
    fun diced() {
        assertArrayEquals(arrayOf("a", "b", "c"), "a/b/c".diced.toTypedArray())
        assertArrayEquals(arrayOf("/","a", "b", "c"), "/a/b/c".diced.toTypedArray())
        assertArrayEquals(arrayOf("a", "b", "c"), "a/b/c/".diced.toTypedArray())
        assertArrayEquals(arrayOf("a", "d"), "a/d".diced.toTypedArray())
    }
}