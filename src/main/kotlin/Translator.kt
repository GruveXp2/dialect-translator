package dev.gruve

// oversetter bare det som fins i ordlista og som er ord
fun translate(text: String, ordliste: Ordliste): String {
    val tokens = tokenize(text).map { token ->
        if (token.type != TokenType.ORD) return@map token
        val translated = ordliste.lookup(token.text) ?: return@map token
        token.copy(text = translated)
    }
    return combine(tokens)
}
