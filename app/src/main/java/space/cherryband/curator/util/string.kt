package space.cherryband.curator.util

private val GENDER = listOf("", "\u200D♀️", "\u200D♂")
private val COLOUR_TONE_MODIFIER =
    listOf("", "\uD83C\uDFFB", "\uD83C\uDFFC", "\uD83C\uDFFD", "\uD83C\uDFFE",
    "\uD83C\uDFFF")

fun randomShrug(): String {
    return "\uD83E\uDD37" + COLOUR_TONE_MODIFIER.random() + GENDER.random()
}