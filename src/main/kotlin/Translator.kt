package dev.gruve

// oversetter bare det som fins i ordlista og som er ord. Beholder forbokstav
fun translate(text: String, from: Ordliste, to: Ordliste): String {
    val tokens = tokenize(text).map { token ->
        if (token.type != TokenType.ORD) return@map token
        val translated = translateWord(token.text.lowercase(), from, to) ?: return@map token
        val capitalized = if (token.text.first().isUpperCase()) translated.replaceFirstChar { it.uppercase() } else translated
        token.copy(text = capitalized)
    }
    return combine(tokens)
}

// ord i ordlista går først, så personlige pronomen via tabellane
fun translateWord(word: String, from: Ordliste, to: Ordliste): String? =
    to.lookup(word) ?: translatePronomen(word, from.pronomen, to.pronomen)
