package space.cherryband.curator.data

import org.junit.Test
import org.junit.Assert.*

internal class RecursiveDirectoryTest {
    private val root = RecursiveDirectory(null, name = "root")
    private val child1 = RecursiveDirectory(root, name = "child1")
    private val child2 = RecursiveDirectory(root, name = "child2")
    private val child3 = RecursiveDirectory(child1, name = "child3")

    @Test
    fun getPath() {
        assertEquals("root/", root.path)
        assertEquals("root/child2/", child2.path)
        assertEquals("root/child1/child3/", child3.path)
    }

    @Test
    fun add() {
        val child4 = root.add(listOf("root", "child2", "child4"))
        assertNotNull(child4)
        assertEquals("child4", child4?.name)
        assertEquals(child2, child4?.parent)
        assertTrue(child2.children.contains(child4))

        val child10 = root.add(listOf("root", "child2", "child4", "child6", "child8", "child10"))
        assertNotNull(child10)
        assertEquals("child10", child10?.name)

        val child8 = child10?.parent
        assertNotNull(child8)
        assertEquals("child8", child8?.name)

        val child6 = child8?.parent
        assertNotNull(child6)
        assertEquals("child6", child6?.name)

        assertEquals(child4, child6?.parent)
    }

    @Test
    fun find() {
        assertEquals(root.find(listOf("root")), root)
        assertNull(root.find(listOf("child3")))
        assertEquals(root.find(listOf("root", "child2")), child2)
        assertEquals(root.find(listOf("root", "child1", "child3")), child3)
    }

    @Test
    fun getParent() {
        assertNull(root.parent)
        assertEquals(child3.parent, child1)
        assertEquals(child2.parent, root)
        assertEquals(child1.parent, root)
    }

    @Test
    fun getImageCount(){
        child3.localImageCount = 30
        assertEquals(0, child1.localImageCount)
        assertEquals(30, child1.imageCount)

        child1.localImageCount = 3
        assertEquals(3, child1.localImageCount)
        assertEquals(30, child3.imageCount)
        assertEquals(33, child1.imageCount)

        child2.localImageCount = 28
        assertEquals(61, root.imageCount)

        child3.localImageCount = 0
        assertEquals(31, root.imageCount)
    }
}