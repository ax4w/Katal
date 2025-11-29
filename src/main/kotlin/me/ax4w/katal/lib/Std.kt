package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Value
import me.ax4w.katal.exceptions.IllegalStatementException
import java.io.File
import java.io.InputStream

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
        r.stack.push(arguments[1])
        r.stack.push(arguments[0])
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
        val range = arguments[0]
        val fn = arguments[1]
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

    fun arrayOf(r : Runtime) {
        //top of the stack needs to be the size
        val arguments = r.fetchNParams(1,false, Value.Num::class)
        //arrays don't need to be homogenous, but fixed size
        val elements = r.fetchNParams(arguments[0].asNum().toInt(),false)
        r.stack.push(Value.Array(elements.toMutableList()))
    }

    fun get(r : Runtime) {
        val arguments = r.fetchNParams(2,true, Value.Num::class, Value.Array::class)
        if (arguments[1] !is Value.Num)
            throw IllegalArgumentException("get needs a number as index")
        if (arguments[0] !is Value.Array)
            throw IllegalArgumentException("get needs a array to index in")
        val index = arguments[1].asNum().toInt()
        val array = arguments[0].asArray()
        if (index >= array.size)
            throw IllegalArgumentException("index $index out of bounds for array length ${array.size}")
        //put array back
        r.stack.push(arguments[0])
        //put element at index on top
        r.stack.push(array[index])
    }

    fun set(r : Runtime) {
        val arguments = r.fetchNParams(3,true, )
        if (arguments[1] !is Value.Num)
            throw IllegalArgumentException("set needs a number as index")
        if (arguments[0] !is Value.Array)
            throw IllegalArgumentException("set needs a array to index in")
        val index = arguments[1].asNum().toInt()
        val array = arguments[0].asArray()
        if (index >= array.size)
            throw IllegalArgumentException("index $index out of bounds for array length ${array.size}")
        array[index] = arguments[2]
        r.stack.push(Value.Array(array))
    }

    fun map(r : Runtime) {
        val arguments = r.fetchNParams(2,false, Value.Array::class, Value.Compound::class)
        if (arguments[0] !is Value.Array)
            throw IllegalArgumentException("map needs a array to map over")
        if (arguments[1] !is Value.Compound)
            throw IllegalArgumentException("map needs a compound to map over with")
        val array = arguments[0].asArray()
        val fn = arguments[1].asCompound()
        val res = array.map {
            r.stack.push(it)
            r.evaluate(fn)
            val v = r.fetchNParams(1, false)[0]
            v
        }
        r.stack.push(Value.Array(res.toMutableList()))
    }

    fun forEach(r : Runtime) {
        val arguments = r.fetchNParams(2,false, Value.Array::class, Value.Compound::class)
        if (arguments[0] !is Value.Array)
            throw IllegalArgumentException("map needs a array to map over")
        if (arguments[1] !is Value.Compound)
            throw IllegalArgumentException("map needs a compound to map over with")
        val array = arguments[0].asArray()
        val fn = arguments[1].asCompound()
        array.forEach {
            r.stack.push(it)
            r.evaluate(fn)
        }
    }


}