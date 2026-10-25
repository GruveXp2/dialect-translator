package dev.gruve

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.Collator
import java.util.Locale

// file format version
const val ORDLISTE_FORMAT_VERSION = 1

@Serializable
data class WordEntry(
    val form: String, // hva det blir oversatt til
    val registered: String, // dato
)

@Serializable
data class Ordliste(
    val version: Int,
    val words: Map<Ordklasse, Map<String, WordEntry>>, // ordklasse -> (input ord -> oversatt ord)
) {
    // Beholder forbokstav
    fun lookup(word: String): String? {
        val lowercase = word.lowercase()
        // basically words[lowercase], but for using nested maps
        val translated = words.values.firstNotNullOfOrNull { it[lowercase] }?.form ?: return null
        return if (word.first().isUpperCase()) translated.replaceFirstChar { it.uppercase() } else translated
    }

    fun sorted(): Ordliste {
        val norwegian = Collator.getInstance(Locale.of("nb"))
        return copy(words = words.toSortedMap().mapValues { (_, ordklasseWords) -> ordklasseWords.toSortedMap(norwegian) })
    }

}

fun parseOrdliste(json: String): Ordliste {
    val ordliste = Json.decodeFromString<Ordliste>(json)
    require(ordliste.version == ORDLISTE_FORMAT_VERSION) { // migration will prob be added in the future when needed
        "Ordlista har versjon ${ordliste.version}, men programmet støtter bare versjon $ORDLISTE_FORMAT_VERSION"
    }
    return ordliste
}

fun loadOrdliste(file: File): Ordliste = parseOrdliste(file.readText())

fun saveOrdliste(ordliste: Ordliste, file: File) {
    val text = formatJson(Json.encodeToJsonElement(ordliste.sorted())) + "\n"
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
