package dev.gruve

enum class TokenType { ORD, TALL, MELLOMROM, TEGN }

data class Token(val text: String, val type: TokenType)

// splitting words into tokens, like "hoi", " ", "!". some words also have - in them, like e-post etc. prob not important most of the time but let it stand since y not ig
//                                                           0..
//                                              1..     (     1..      ) 0..  0..   1
//                                         (unicodeæøå)  -(unicodeæøå)  |num|space|dot
private val tokenRegex = Regex("""[\p{L}\p{M}]+(-[\p{L}\p{M}]+)*|\d+|\s+|.""")

fun tokenize(text: String): List<Token> =
    tokenRegex.findAll(text).map { Token(it.value, typeOf(it.value)) }.toList()

fun combine(tokens: List<Token>): String = tokens.joinToString("") { it.text }

private fun typeOf(text: String): TokenType {
    val first = text.first()
    return when {
        first.isLetter() -> TokenType.ORD
        first.isDigit() -> TokenType.TALL
        first.isWhitespace() -> TokenType.MELLOMROM
        else -> TokenType.TEGN
    }
}
