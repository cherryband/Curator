package space.cherryband.curator.data

import org.junit.Assert
import org.junit.Test

internal class RootDirectoryTest{
    val root = RootDirectory("rootDir")
    private val child1 = RecursiveDirectory(null, name = "child1")
    private val child2 = RecursiveDirectory(null, name = "child2")
    private val child3 = RecursiveDirectory(child1, name = "child3")
    init {
        root.children.addAll(arrayOf(child1, child2))
    }

    @Test
    fun add() {
        val child4 = root.add(listOf("child2", "child4"))
        Assert.assertNotNull(child4)
        Assert.assertEquals(child2, child4!!.parent)
        Assert.assertEquals("child4", child4.name)

        val child5 = root.add(listOf("child5"))
        Assert.assertNotNull(child5)
        Assert.assertEquals(null, child5!!.parent)
        Assert.assertEquals("child5", child5.name)
        Assert.assertTrue(root.children.contains(child5))
    }


    @Test
    fun find() {
        Assert.assertNull(root.find(listOf("rootDir")))
        Assert.assertNull(root.find(listOf("child3")))
        Assert.assertEquals(child2, root.find(listOf("child2")))
        Assert.assertEquals(child3, root.find(listOf("child1", "child3")))
    }
}