package parser

import node.*
import num.parseNum

const val log = false

fun parseExp(tokens: MutableList<Token>): Node {
    try {
        return parseExp(tokens, 0)
    } catch (e: Exception) {
        throw SyntaxError()
    }
}

fun parseExp(tokens: MutableList<Token>, prec: Int): Node {
    if (log) console.log("Parse Exp", prec, ": ", tokens.toString())
    var left = parseBasicExp(tokens) ?: throw SyntaxError()
    if (log) console.log("Left: ", left.toString())
    while (tokens.isNotEmpty()) {
        val token2 = tokens[0]
        val op = when (token2.type) {
            TokenType.Plus -> Operator.Plus
            TokenType.Minus -> Operator.Minus
            TokenType.Star -> Operator.Times
            TokenType.Slash -> Operator.Div
            TokenType.Hat -> Operator.Power
            else -> return left
        }

        if (op.leftAssoc) {
            if (op.prec < prec) return left
            left = parseOpExp(tokens, op, left)
            continue
        } else {
            tokens.removeAt(0)
            return op.creator(listOf(left, parseExp(tokens, prec)))
        }
    }
    return left
}

fun parseBasicExp(tokens: MutableList<Token>): Node? {
    if (log) console.log("Parse Basic Exp: ", tokens.toString())
    val token = tokens.removeAt(0)
    return when (token.type) {
        TokenType.LB -> {
            val e = parseExp(tokens)
            if (takeExact(tokens, Token_RB) == null) throw SyntaxError()
            e
        }
        TokenType.VarName -> Var(token.value[0])
        TokenType.Number -> parseNum(token.value)
        else -> null
    }
}

fun parseOpExp(tokens: MutableList<Token>, op: Operator, left: Node): Node {
    if (log) console.log("Parse Op Exp: ", tokens.toString())
    tokens.removeAt(0)
    val operands = mutableListOf(left)
    /*when (op) {
        else -> ...
    }*/
    operands.add(parseExp(tokens, op.prec + 1))
    return op.creator(operands)
}

fun takeExact(tokens: MutableList<Token>, token: Token): Token? {
    if (log) console.log("Take exact: ", token.toString(), " from: ", tokens.toString())
    return if (token === tokens[0]) tokens.removeAt(0) else null
}


enum class Operator(val creator: (List<Node>) -> Node, val leftAssoc: Boolean = true, val prec: Int) {
    Power({ Power(it[0], it[1]) }, prec = 15),

    Times({ Prod(it[0], it[1]) }, prec = 14),
    Div({ Div(it[0], it[1]) }, prec = 14),

    Plus({ Sum(it[0], it[1]) }, prec = 13),
    Minus({ Diff(it[0], it[1]) }, prec = 13),
}

/*class Parser(private val tokens: List<Token>) {
    fun parse(): Node? {
        return parseExp(tokens.toMutableList())
    }
}

// x^2+2x+1
// Add(Pow(x,2),Add(Prod(2,x),1))

private fun parseExp(tokens: MutableList<Token>): Node? {
    return parsePd1(tokens) ?: parsePd2(tokens) ?: parsePd0(tokens)
}

private fun parsePd1(tokens: MutableList<Token>): Node? {
    if (tokens.isEmpty())
        return null

    val tokensCopy = tokens.toMutableList()
    val left = parsePd2(tokensCopy) ?: parsePd3(tokensCopy) ?: parsePd0(tokensCopy) ?: return null
    //console.log("Pd1", left.toString())

    if (tokensCopy.isEmpty())
        return null

    val op = tokensCopy[0]
    val opConstructor: (Node, Node) -> Node

    if (op.type == TokenType.Plus || op.type == TokenType.Minus) {
        if (op.value == "+") {
            tokensCopy.remove(op)
            opConstructor = ::Sum
        } else if (op.value == "-") {
            tokensCopy.remove(op)
            opConstructor = ::Diff
        } else {
            return null
        }
    } else {
        return null
    }

    val right = parsePd1(tokensCopy) ?: parsePd2(tokensCopy) ?: parsePd3(tokensCopy) ?: parsePd0(tokensCopy) ?: return null

    tokens.clear()
    tokens.addAll(tokensCopy)

    return opConstructor(left, right)
}

private fun parsePd2(tokens: MutableList<Token>): Node? {
    if (tokens.isEmpty())
        return null

    val tokensCopy = tokens.toMutableList()
    val left = parsePd3(tokensCopy) ?: parsePd0(tokensCopy) ?: return null
    //console.log("Pd2l", left.toString())

    if (tokensCopy.isEmpty())
        return null

    val op = tokensCopy[0]
    //console.log("Pd2o", op)
    val opConstructor: (Node, Node) -> Node

    if (op.type == TokenType.Star || op.type == TokenType.Slash) {
        if (op.value == "*") {
            tokensCopy.remove(op)
            opConstructor = ::Prod
        } else if (op.value == "/") {
            tokensCopy.remove(op)
            opConstructor = ::Div
        } else {
            return null
        }
    } else {
        return null
    }

    val right = parsePd2(tokensCopy) ?: parsePd3(tokensCopy) ?: parsePd0(tokensCopy) ?: return null
    //console.log("Pd2r", right.toString())

    tokens.clear()
    tokens.addAll(tokensCopy)

    //console.log("Pd2R", opConstructor(left, right))
    return opConstructor(left, right)
}

private fun parsePd3(tokens: MutableList<Token>): Node? {
    if (tokens.isEmpty())
        return null

    val tokensCopy = tokens.toMutableList()
    val left = parsePd0(tokensCopy) ?: return null
    console.log("Pd3l", left.toString())

    if (tokensCopy.isEmpty())
        return null

    val op = tokensCopy[0]
    //console.log("Pd2o", op)
    val opConstructor: (Node, Node) -> Node

    if (op.type == TokenType.Hat) {
        if (op.value == "^") {
            tokensCopy.remove(op)
            opConstructor = ::Power
        } else {
            return null
        }
    } else {
        return null
    }

    val right = parsePd3(tokensCopy) ?: parsePd0(tokensCopy) ?: return null
    //console.log("Pd2r", right.toString())

    tokens.clear()
    tokens.addAll(tokensCopy)

    //console.log("Pd2R", opConstructor(left, right))
    return opConstructor(left, right)
}

private fun parsePd0(tokens: MutableList<Token>): Node? {
    if (tokens.isEmpty())
        return null

    val first = tokens[0]
    if (first.type == TokenType.VarName) {
        tokens.remove(first)
        return Var(first.value[0])
    }
    if (first.type == TokenType.Number) {
        tokens.remove(first)
        return parseNum(first.value)
    }

    if (first.type == TokenType.LB) {
        tokens.remove(first)
        val tokensCopy = tokens.toMutableList()
        val inside = parseExp(tokensCopy)
        val after = tokensCopy[0]
        if (after.type == TokenType.RB) {
            tokensCopy.remove(after)
            tokens.clear()
            tokens.addAll(tokensCopy)
            console.log(tokens)
            return inside
        }
    }

    return null
}*/