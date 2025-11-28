package me.ax4w.katal

import me.ax4w.katal.exceptions.IllegalStatementException
import java.util.Stack

class Scanner(var input: String) {

    var pos = 0

    private fun isNum() = input[pos].isDigit() || input[pos] == '.'

    init {
        input = input.replace("\n", "")
            .replace("\r", "")
            .replace("\n\r", "")
    }

    private fun fetchNumber(): String {
        val sb = StringBuilder()
        while (pos < input.length && isNum()) {
            sb.append(input[pos])
            pos++
        }
        val str = sb.toString()
        //validate
        if (str.filter { it == '.' }.length > 1) throw IllegalArgumentException("A number can only have one decimal point")
        return str
    }


    private fun isCompoundOpen() = input[pos] == '['

    private fun isArray() = input[pos] == '('

    private fun fetchArray(): String {
        val sb = StringBuilder()
        val openStack = Stack<Boolean>()
        do {
            when (input[pos]) {
                '(' -> {
                    openStack.push(true)
                }

                ')' -> {
                    openStack.pop()
                }
            }
            sb.append(input[pos])
            pos++
        } while (openStack.isNotEmpty())
        return sb.toString()
    }

    private fun fetchCompound(): String {
        val sb = StringBuilder()
        val openStack = Stack<Boolean>()
        do {
            when (input[pos]) {
                '[' -> {
                    if (!openStack.isEmpty()) sb.append(input[pos])
                    openStack.push(true)
                }

                ']' -> {
                    openStack.pop()
                    if (!openStack.isEmpty()) sb.append(input[pos])
                }

                else -> sb.append(input[pos])
            }
            pos++
            if (pos > input.length) throw IllegalStatementException("Compound needs to be closed")
        } while (openStack.isNotEmpty())
        return sb.toString()
    }

    private fun isString() = input[pos] == '"'

    private fun fetchString(): String {
        val sb = StringBuilder()
        pos++
        while (pos < input.length && !isString()) {
            sb.append(input[pos])
            pos++
        }
        pos++
        return sb.toString()
    }

    private fun isLiteral() = input[pos].isLetter()

    private fun fetchLiteral(): String {
        val sb = StringBuilder()
        while (pos < input.length && isLiteral()) {
            sb.append(input[pos])
            pos++
        }
        return sb.toString()
    }

    private fun isFunctionDeclaration() = input[pos] == '$'

    private fun fetchFunctionDeclaration(): String {
        //skip $
        pos++
        var sb = StringBuilder()
        //fetch name

        while (pos < input.length && input[pos] != ':') {
            sb.append(input[pos])
            pos++
        }
        if (sb.isEmpty()) throw IllegalStatementException("Function declaration needs a name")
        val name = sb.toString().trim()
        sb = StringBuilder()
        sb.append("${name}$")
        //skip name body delimiter
        pos++
        //now function body
        skipSpaces()
        while (pos < input.length && !isFunctionDeclaration()) {
            sb.append(input[pos])
            pos++
        }
        //skip last $
        pos++
        if (sb.last() == '$') throw IllegalStateException("Function declarations needs a body")
        return sb.toString()
    }

    private fun skipSpaces() {
        while (pos < input.length && input[pos].isWhitespace()) pos++
    }


    fun tokens() = sequence {
        while (pos < input.length) {

            when {
                isNum() -> {
                    yield(Pair(fetchNumber(), Token.NUMBER))
                }

                isCompoundOpen() -> {
                    yield(Pair(fetchCompound(), Token.COMPOUND))
                }

                isString() -> {
                    yield(Pair(fetchString(), Token.STRING))
                }

                isLiteral() -> {
                    val fetched = fetchLiteral()
                    if (fetched == "true" || fetched == "false") yield(Pair(fetched, Token.BOOLEAN))
                    else yield(Pair(fetched, Token.FUNCTION))
                }

                isFunctionDeclaration() -> {
                    yield(Pair(fetchFunctionDeclaration(), Token.DECLARATION))
                }
            }
            skipSpaces()
        }

    }
}