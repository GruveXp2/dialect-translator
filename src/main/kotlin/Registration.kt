package dev.gruve

// sjekker etter ord som ikke er i ordlista eller er inherita
fun unknownWords(text: String, ordliste: Ordliste, inherited: InheritedWords): List<String> =
    tokenize(text)
        .filter { it.type == TokenType.ORD }
        .map { it.text.lowercase() }
        .distinct()
        .filter { ordliste.lookup(it) == null && it !in inherited }
