package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Token
import me.ax4w.katal.exceptions.IllegalFunctionCallException

object Math {

    fun add(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Token.NUMBER)
        val (valueA, _) = arguments[1]
        val (valueB, _) = arguments[0]
        val result = (valueA.toDouble() + valueB.toDouble()).toString()
        r.stack.push(Pair(result, Token.NUMBER))
    }

     fun mul(r: Runtime) {
         val arguments = r.fetchNParams(2, true, Token.NUMBER)
         val (valueA, _) = arguments[1]
         val (valueB, _) = arguments[0]
         val result = (valueA.toDouble() * valueB.toDouble()).toString()
        r.stack.push(Pair(result, Token.NUMBER))
    }

    fun div(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Token.NUMBER)
        val (valueA, _) = arguments[1]
        val (valueB, _) = arguments[0]
        if( valueB.toDouble() == 0.0) throw IllegalArgumentException("Division by zero")
        val result = (valueA.toDouble() / valueB.toDouble()).toString()
        r.stack.push(Pair(result, Token.NUMBER))
    }

    fun sub(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Token.NUMBER)
        val (valueA, _) = arguments[1]
        val (valueB, _) = arguments[0]
        val result = (valueA.toDouble() - valueB.toDouble()).toString()
        r.stack.push(Pair(result, Token.NUMBER))
    }

    fun mod(r : Runtime) {
        val arguments = r.fetchNParams(2, true, Token.NUMBER)
        val (valueA, _) = arguments[1]
        val (valueB, _) = arguments[0]
        val result = (valueA.toDouble() % valueB.toDouble()).toString()
        r.stack.push(Pair(result, Token.NUMBER))
    }
}