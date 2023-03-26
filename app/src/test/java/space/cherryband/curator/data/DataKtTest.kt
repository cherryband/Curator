package space.cherryband.curator.data

import org.junit.Test
import org.junit.Assert.*
import space.cherryband.curator.util.diced
import space.cherryband.curator.util.isParentOf
import space.cherryband.curator.util.walk

internal class DataKtTest {
    @Test
    fun diced() {
        assertArrayEquals(arrayOf("a", "b", "c"), "a/b/c".diced.toTypedArray())
        assertArrayEquals(arrayOf("/","a", "b", "c"), "/a/b/c".diced.toTypedArray())
        assertArrayEquals(arrayOf("a", "b", "c"), "a/b/c/".diced.toTypedArray())
        assertArrayEquals(arrayOf("a", "d"), "a/d".diced.toTypedArray())
    }

    @Test
    fun walk() {
        assertArrayEquals(arrayOf("/", "/a", "/a/b"), "/a/b/c".walk.toTypedArray())
        assertArrayEquals(arrayOf("a", "a/b"), "a/b/c".walk.toTypedArray())
    }
    @Test
    fun isParentOf() {
        assertTrue("/a".isParentOf("/a/b"))
        assertTrue("/a".isParentOf("/a/c"))
        assertFalse("a".isParentOf("/a/b"))
        assertTrue("a".isParentOf("a/b"))
        assertFalse("/a".isParentOf("/a"))
        assertFalse("/a".isParentOf("/b/a/c"))
    }
}