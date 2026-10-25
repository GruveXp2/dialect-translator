package dev.gruve

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File

const val INHERITED_FORMAT_VERSION = 1

// ord som er sjekka og ikke har egen form på dette nivået, men arver.
// feks hest i krsand, er ikke forskjellig fra bokmål og da æddekke vits å prompte unknown word, eller lagre reduntant translation som er lik
@Serializable
data class InheritedWords(
    val version: Int,
    val words: Map<Ordklasse, Map<String, String>>,
) {
    operator fun contains(word: String) = words.values.any { word.lowercase() in it }

    // could use MutableMap but not needed, since primary use will be translating, updating happens "rarely" (not like thousands/sec)
    // so it can translate really fast, large amounts, but adding new words is slow, the human is the bottleneck
    fun withWord(ordklasse: Ordklasse, word: String, registered: String) =
        copy(words = words.plusWord(ordklasse, word, registered))

    fun withoutWord(ordklasse: Ordklasse, word: String) = copy(words = words.minusWord(ordklasse, word))

    fun sorted() = copy(words = words.sorted())
}

fun parseInheritedWords(json: String): InheritedWords {
    val inherited = Json.decodeFromString<InheritedWords>(json)
    require(inherited.version == INHERITED_FORMAT_VERSION) {
        "Inherited-lista har versjon ${inherited.version}, men programmet støtter bare versjon $INHERITED_FORMAT_VERSION"
    }
    return inherited
}

fun loadInheritedWords(file: File): InheritedWords = parseInheritedWords(file.readText())

fun saveInheritedWords(inherited: InheritedWords, file: File) =
    writeJsonFile(Json.encodeToJsonElement(inherited.sorted()), file)
