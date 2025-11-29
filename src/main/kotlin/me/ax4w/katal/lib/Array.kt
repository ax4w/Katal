package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Value

object Array {

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

    fun has(r : Runtime) {
        val arguments = r.fetchNParams(2,false)
        if (arguments[0] !is Value.Array)
            throw IllegalArgumentException("has needs an array to check in")
        r.stack.push(Value.Bool(arguments[0].asArray().contains(arguments[1])))
    }

    fun filter(r : Runtime) {
        val arguments = r.fetchNParams(2,false, Value.Array::class, Value.Compound::class)
        if (arguments[0] !is Value.Array)
            throw IllegalArgumentException("filter needs a array to filter over")
        if (arguments[1] !is Value.Compound)
            throw IllegalArgumentException("filter needs a compound to filter over with")
        val array = arguments[0].asArray()
        val res = array.filter { v ->
            r.stack.push(v)
            r.evaluate(arguments[1].asCompound())
            val ret = r.fetchNParams(1, false, Value.Bool::class)
            ret[0].asBoolean()
        }
        r.stack.push(Value.Array(res.toMutableList()))
    }

}