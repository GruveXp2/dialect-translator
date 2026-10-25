package dev.gruve

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.Collator
import java.util.Locale

// både Ordliste og InheritedWords er ordklasse -> (input ord -> oversatt)
fun <T> Map<Ordklasse, Map<String, T>>.plusWord(ordklasse: Ordklasse, word: String, value: T): Map<Ordklasse, Map<String, T>> {
    val ordklasseWords = getOrElse(ordklasse) { emptyMap() } + (word to value)
    return this + (ordklasse to ordklasseWords)
}

// samme som over bare man fjerner et ord istedet
fun <T> Map<Ordklasse, Map<String, T>>.minusWord(ordklasse: Ordklasse, word: String): Map<Ordklasse, Map<String, T>> {
    val ordklasseWords = getOrElse(ordklasse) { emptyMap() } - word
    return if (ordklasseWords.isEmpty()) this - ordklasse else this + (ordklasse to ordklasseWords) // this[ordklasse] = ordklassewords
}

fun <T> Map<Ordklasse, Map<String, T>>.sorted(): Map<Ordklasse, Map<String, T>> {
    val norwegian = Collator.getInstance(Locale.of("nb"))
    return toSortedMap().mapValues { (_, ordklasseWords) -> ordklasseWords.toSortedMap(norwegian) }
}

fun writeJsonFile(element: JsonElement, file: File) {
    val text = formatJson(element) + "\n"
    // crash protection, write to temp file first and move atomically after so i dont get unlucky one day
    val temp = file.resolveSibling(file.name + ".tmp")
    temp.writeText(text)
    Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
}

// makes sure that the words entries are written as oneliner in the json. this makes it alot more readable
private const val INLINE_DEPTH = 3

private const val INDENT = "    "

private fun formatJson(element: JsonElement, depth: Int = 0): String {
    if (element !is JsonObject || element.isEmpty()) return element.toString()
    val fields = element.map { (key, value) -> "${JsonPrimitive(key)}: ${formatJson(value, depth + 1)}" }
    if (depth >= INLINE_DEPTH) return fields.joinToString(", ", "{ ", " }")
    val indent = INDENT.repeat(depth + 1)
    return fields.joinToString(",\n", "{\n", "\n" + INDENT.repeat(depth) + "}") { indent + it }
}
