package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Value

object Optional {

    fun something(r: Runtime) {
        val arguments = r.fetchNParams(1, true)
        r.stack.push(Value.Something(arguments[0]))
    }

    fun nothing(r : Runtime) {
        r.stack.push(Value.Nothing)
    }

    fun unwrap(r : Runtime) {
        val arguments = r.fetchNParams(1, true,Value.Something::class, Value.Nothing::class)
        if (arguments[0] is Value.Nothing)
            throw IllegalArgumentException("Unwrap can not be unwrapped")
        r.stack.push(arguments[0].asSomething())
    }

    fun unwrapOr(r: Runtime) {
        val arguments = r.fetchNParams(2, true)
        if (arguments[0] is Value.Nothing)
            r.stack.push(arguments[1])
        else if(arguments[0] is Value.Something)
            r.stack.push(arguments[0].asSomething())
        else
            throw IllegalArgumentException("UnwrapOr needs to be called on Something or Nothing")
    }

}