package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Token
import me.ax4w.katal.exceptions.IllegalStatementException
import java.io.File
import java.io.InputStream
import java.util.EmptyStackException

object Std {

    fun dup(r : Runtime) {
        if(r.stack.isEmpty()) throw IllegalStatementException("Cannot dup to empty stack")
        r.stack.push(r.stack.peek())
    }

    fun showStack(r : Runtime) {
        println(r.stack.toList().map { it.first }.reversed().joinToString(","))
    }

    fun clearStack(r : Runtime) {
        r.stack.clear()
    }

    fun swap(r: Runtime) {
        val arguments = r.fetchNParams(2,false)
        r.stack.push(arguments[0])
        r.stack.push(arguments[1])
    }

    fun drop(r: Runtime) {
        if(r.stack.isEmpty()) throw IllegalStatementException("Can not drop from empty stack")
        r.stack.pop()
    }

    fun call (r: Runtime) {
        if (r.stack.isEmpty()) throw IllegalStatementException("Cannot call from empty stack")
        val arguments = r.fetchNParams(1, false,Token.FUNCTION, Token.COMPOUND)
        r.evaluate(arguments[0].first)
    }

    fun print(r: Runtime) {
        val arguments = r.fetchNParams(1,false)
        print(arguments[0].first)
    }

    fun println(r : Runtime) {
        val arguments = r.fetchNParams(1,false)
        println(arguments[0].first)
    }

    fun repeatN(r : Runtime) {
        val arguments = r.fetchNParams(2,false, Token.NUMBER, Token.COMPOUND)
        var (range, tokR) = arguments[1]
        val (fn, tokFn) = arguments[0]
        if (tokR == Token.COMPOUND) {
            r.evaluate(range)
            val (nRange, nTokR) = r.fetchNParams(1, false, Token.NUMBER)[0]
            range = nRange
            tokR = nTokR

        }
        for (i in 0 until range.toInt()) {
            r.stack.push(Pair(i.toString(), Token.NUMBER))
            r.evaluate(fn)
        }
    }

    fun load(r: Runtime) {
        val arguments = r.fetchNParams(1,false, Token.STRING)
        val (path,_) = arguments[0]
        val inputStream: InputStream = File(path).inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }
        r.evaluate(inputString)
    }

    fun nop(r: Runtime) {}
}