package dev.gruve

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = OrdklasseSerializer::class)
enum class Ordklasse(val abbreviation: String) {
    SUBSTANTIV("subst"),
    VERB("verb"),
    ADJEKTIV("adj"),
    ADVERB("adv"),
    PREPOSISJON("prep"),
    PRONOMEN("pron"),
    DETERMINATIV("determ"),
    KONJUNKSJON("konj"),
    SUBJUNKSJON("subj"),
    INTERJEKSJON("interj"),
}

fun ordklasseFromAbbreviation(text: String): Ordklasse? {
    val abbreviation = text.trim().lowercase()
    return Ordklasse.entries.find { it.abbreviation == abbreviation }
}

object OrdklasseSerializer : KSerializer<Ordklasse> {
    override val descriptor = PrimitiveSerialDescriptor("dev.gruve.Ordklasse", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Ordklasse) = encoder.encodeString(value.name.lowercase())

    override fun deserialize(decoder: Decoder): Ordklasse {
        val text = decoder.decodeString()
        return Ordklasse.entries.find { it.name.lowercase() == text } ?: throw SerializationException("Ukjent ordklasse '$text'")
    }
}
