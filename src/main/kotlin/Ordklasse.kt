package gruvexp

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
