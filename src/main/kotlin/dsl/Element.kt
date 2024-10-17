package dsl

import java.io.File

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

class TextElement(private val content: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$content\n")
    }
}

@DslMarker
annotation class ReadmeDsl

@ReadmeDsl
abstract class Section(val title: String) : Element {
    private val children = mutableListOf<Element>()

    protected fun <T : Element> initSection(section: T, init: T.() -> Unit): T {
        section.init()
        children.add(section)
        return section
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent### $title\n\n")
        children.forEach { it.render(builder, indent) }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

abstract class SectionWithText(title: String) : Section(title) {
    operator fun String.unaryPlus() {
        initSection(TextElement(this)) {}
    }
}

class Readme : SectionWithText("README") {
    fun introduction(init: Introduction.() -> Unit) = initSection(Introduction(), init)
    fun installation(init: Installation.() -> Unit) = initSection(Installation(), init)
    fun usage(init: Usage.() -> Unit) = initSection(Usage(), init)
    fun license(init: License.() -> Unit) = initSection(License(), init)

    fun saveToFile(filePath: String) {
        val file = File(filePath)
        file.writeText(this.toString())
    }
}

class Introduction : SectionWithText("Introduction")
class Installation : SectionWithText("Installation")
class Usage : SectionWithText("Usage")
class License : SectionWithText("License")

fun readme(init: Readme.() -> Unit): Readme {
    return Readme().apply(init)
}