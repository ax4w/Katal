package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Token
import me.ax4w.katal.Value

object Logic {

    fun and (r: Runtime) {
        val arguments = r.fetchNParams(2, true,Value.Bool::class)
        val result = arguments[0].asBoolean()  && arguments[1].asBoolean()
        r.stack.push(Value.Bool(result))
    }

    fun or (r: Runtime) {
        val arguments = r.fetchNParams(2,true, Value.Bool::class)
        val result = arguments[0].asBoolean()|| arguments[1].asBoolean()
        r.stack.push(Value.Bool(result))
    }

    fun not (r: Runtime) {
        val arguments = r.fetchNParams(1,true, Value.Bool::class)
        val result = !arguments[0].asBoolean()
        r.stack.push(Value.Bool(result))
    }

    fun cond(r : Runtime) {
        val arguments = r.fetchNParams(3)
        val condRaw = arguments[2]
        var condResult: Boolean
        when (condRaw) {
            is Value.Compound -> {
                r.evaluate(condRaw.asCompound())
                val result = r.fetchNParams(1, false, Value.Bool::class)[0]
                condResult = result.asBoolean()
            }
            is Value.Bool -> {
                condResult = condRaw.asBoolean()
            }
            else -> throw IllegalArgumentException("Condition needs to evaluate to a boolean")
        }

        if (arguments[1] !is Value.Compound || arguments[2] !is Value.Compound)
            throw IllegalArgumentException("Branches need to be compounds")

        if (condResult) {
            r.evaluate(arguments[1].asCompound())
        }else{
            r.evaluate(arguments[0].asCompound())
        }

    }

    fun eq(r: Runtime) {
        val argument = r.fetchNParams(2,true)
        val valA = argument[1]
        val valB = argument[0]

        if (valA::class != valB::class) throw IllegalArgumentException("Arguments must have the same type for eq")

        r.stack.push(Value.Bool(valA == valB))
    }

    fun le(r: Runtime) {
        val argument = r.fetchNParams(2,true, Value.Num::class)
        val valA = argument[1]
        val valB = argument[0]

        if (valA::class != Value.Num::class || valB::class != Value.Num::class)
            throw IllegalArgumentException("Arguments must have the same type for eq")
        r.stack.push(Value.Bool(valA.asNum() < valB.asNum()))
    }

    fun ge(r: Runtime) {
        val argument = r.fetchNParams(2,true, Value.Num::class)
        val valA = argument[1]
        val valB = argument[0]

        if (valA::class != Value.Num::class || valB::class != Value.Num::class)
            throw IllegalArgumentException("Arguments must have the same type for eq")
        r.stack.push(Value.Bool(valA.asNum() > valB.asNum()))
    }
}