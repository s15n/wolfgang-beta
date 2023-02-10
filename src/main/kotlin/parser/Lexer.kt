package parser

fun lex(text: String): List<Token> {
    val tokens = mutableListOf<Token>()

    if (text.isEmpty())
        return tokens

    var i = -1
    while (++i < text.length) {
        val c = text[i]
        //console.log("Check index ", i, " found ", c.toString())

        if (c.isLetter()) {
            tokens.add(Token(TokenType.VarName, c.toString()))
            continue
        }

        val token = when (c) {
            '(' -> Token_LB
            ')' -> Token_RB
            '+' -> Token_Plus
            '-' -> Token_Minus
            '*' -> Token_Times
            '/' -> Token_Div
            '^' -> Token_Power
            else -> {
                if (c.isDigit()) {
                    val endIndex = lexNumber(text, i, c)
                    Token(TokenType.Number, text[(i)..endIndex]).also {
                        i = endIndex
                    }
                } else {
                    continue
                }
            }
        }

        tokens.add(token)
    }

    return  tokens
}

fun lexNumber(text: String, index: Int, c1: Char): Int {
    var i = index
    var c = c1

    var dot = false

    val firstChar = text[i + 1]
    var lastChar: Char

    do {
        if (c == '.') {
            if (!dot) {
                dot = true
            } else throw SyntaxError()
        }
        lastChar = c
        c = text[i++]
    } while (c.isDigit() || c == '.')

    if (lastChar == '.') throw SyntaxError()
    if (firstChar == '.') throw SyntaxError()

    i -= 2
    return i
}

operator fun String.get(range: IntRange): String {
    return substring(range)
}

class SyntaxError : Throwable()

/*class Lexer(private val text: String) {
    private var pos = 0
    private val current = StringBuilder()

    private val tokens = mutableListOf<Token>()

    fun lex(): List<Token> {
        while (pos < text.length) {
            current.append(text[pos++])

            val cs = current.toString()

            for (type in TokenType.values()) {
                if (type.regex.test(cs)) {
                    if (pos < text.length && type.regex.test(cs + text[pos]))
                        break
                    tokens.add(Token(type, cs, pos))
                    current.clear()
                    break
                }
            }
        }
        return tokens
    }
}*/

data class Token(val type: TokenType, val value: String)

val Token_LB = Token(TokenType.LB, "(")
val Token_RB = Token(TokenType.RB, ")")
val Token_Plus = Token(TokenType.Plus, "+")
val Token_Minus = Token(TokenType.Minus, "-")
val Token_Times = Token(TokenType.Star, "*")
val Token_Div = Token(TokenType.Slash, "/")
val Token_Power = Token(TokenType.Hat, "^")

enum class TokenType {
    LB, RB, Plus, Minus, Star, Slash, Hat, Number, VarName
}

/*enum class TokenType(val regex: RegExp) {
    Number(RegExp("^[0-9]+$")),
    VarName(RegExp("^[a-zA-Z]$")),
    OP(RegExp("^[\\+\\-\\*\\^]$")),
    LB(RegExp("^\\($")),
    RB(RegExp("^\\)$")),
}*/