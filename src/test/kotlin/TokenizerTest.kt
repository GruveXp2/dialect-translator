package gruvexp

import kotlin.test.Test
import kotlin.test.assertEquals

class TokenizerTest {

    @Test
    fun `sentence is split into words, spaces and symbols`() {
        val tokens = tokenize("Javel, se det!")
        assertEquals(
            listOf(
                Token("Javel", TokenType.ORD),
                Token(",", TokenType.TEGN),
                Token(" ", TokenType.MELLOMROM),
                Token("se", TokenType.ORD),
                Token(" ", TokenType.MELLOMROM),
                Token("det", TokenType.ORD),
                Token("!", TokenType.TEGN),
            ),
            tokens,
        )
    }

    @Test
    fun `hyphenated words stay whole and numbers get their own token`() {
        val tokens = tokenize("e-post kl 12")
        assertEquals(listOf("e-post", " ", "kl", " ", "12"), tokens.map { it.text })
        assertEquals(TokenType.TALL, tokens.last().type)
    }

    @Test
    fun `combining the tokens gives back the exact same text`() {
        val setninger = listOf(
            "Hei, kommer du?",
            "Æ og dæ  ska på   tur.\tI mårra!",
            "Linje én\nLinje to",
            "Kjør kano, i De Høye Hastigheder, det forurenser ikkje! kvikkhets-kano: 33,5 kr :D",
            "",
        )
        for (setning in setninger) {
            assertEquals(setning, combine(tokenize(setning)))
        }
    }
}
