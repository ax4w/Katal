package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Token
import me.ax4w.katal.Value
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
        println(r.stack.toList().joinToString(" ") { it.toString() })
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
        val arguments = r.fetchNParams(1, false,Value.Function::class, Value.Compound::class)
        if (arguments[0] is Value.Function) {
            r.evaluate(arguments[0].asFunction())
        }else{
            r.evaluate(arguments[0].asCompound())
        }

    }

    fun print(r: Runtime) {
        val arguments = r.fetchNParams(1,false)
        print(arguments[0].toDisplay())
    }

    fun println(r : Runtime) {
        val arguments = r.fetchNParams(1,false)
        println(arguments[0].toDisplay())
    }

    fun repeatN(r : Runtime) {
        val arguments = r.fetchNParams(2,false, Value.Num::class, Value.Compound::class)
        val range = arguments[1]
        val fn = arguments[0]
        var rangeNum : Int

        if (fn !is Value.Compound)
            throw IllegalArgumentException("rangeN needs a compound")

        if (range is Value.Compound) {
            r.evaluate(range.asCompound())
            val nRage = r.fetchNParams(1, false, Value.Num::class)[0]
            rangeNum = nRage.asNum().toInt()
        }else{
            rangeNum = range.asNum().toInt()
        }

        for (i in 0 until rangeNum) {
            r.stack.push(Value.Num(i.toDouble()))
            r.evaluate(fn.asCompound())
        }
    }

    fun load(r: Runtime) {
        val arguments = r.fetchNParams(1,false, Value.Str::class)
        val path = arguments[0]
        val inputStream: InputStream = File(path.asStr()).inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }
        r.evaluate(inputString)
    }

    fun nop(r: Runtime) {}
}