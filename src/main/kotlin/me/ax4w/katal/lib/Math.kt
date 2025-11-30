package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Value

object Math {

    fun add(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Num::class)
        val valueA = arguments[0]
        val valueB = arguments[1]
        r.stack.push(Value.Num(valueA.asNum() + valueB.asNum()))
    }

     fun mul(r: Runtime) {
         val arguments = r.fetchNParams(2, true, Value.Num::class)
         val valueA = arguments[0]
         val valueB = arguments[1]
         r.stack.push(Value.Num(valueA.asNum() * valueB.asNum()))
    }

    fun div(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Num::class)
        val valueA = arguments[0]
        val valueB = arguments[1]

        if(valueB.asNum() == 0.0) throw IllegalArgumentException("Division by zero")
        r.stack.push(Value.Num(valueA.asNum() / valueB.asNum()))
    }

    fun safeDiv(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Num::class)
        val valueA = arguments[0]
        val valueB = arguments[1]
        if (valueB.asNum() == 0.0) {
            r.stack.push(Value.Nothing)
        }else{
            r.stack.push(Value.Something(Value.Num(valueA.asNum() / valueB.asNum())))
        }
    }

    fun sub(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Num::class)
        val valueA = arguments[0]
        val valueB = arguments[1]
        r.stack.push(Value.Num(valueA.asNum() - valueB.asNum()))
    }

    fun mod(r : Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Num::class)
        val valueA = arguments[0]
        val valueB = arguments[1]
        r.stack.push(Value.Num(valueA.asNum() % valueB.asNum()))
    }

    fun abs(r: Runtime) {
        val arguments = r.fetchNParams(1, true, Value.Num::class)
        r.stack.push(Value.Num(kotlin.math.abs(arguments[0].asNum())))
    }
}