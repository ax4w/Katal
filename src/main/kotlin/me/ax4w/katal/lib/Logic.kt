package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Token

object Logic {

    fun and (r: Runtime) {
        val arguments = r.fetchNParams(2, true,Token.BOOLEAN)
        val result = (arguments[0].first.toBoolean() && arguments[1].first.toBoolean()).toString()
        r.stack.push(Pair(result, Token.BOOLEAN))
    }

    fun or (r: Runtime) {
        val arguments = r.fetchNParams(2,true, Token.BOOLEAN)
        val result = (arguments[0].first.toBoolean() || arguments[1].first.toBoolean()).toString()
        r.stack.push(Pair(result, Token.BOOLEAN))
    }

    fun not (r: Runtime) {
        val arguments = r.fetchNParams(1,true, Token.BOOLEAN)
        val result = (!arguments[0].first.toBoolean()).toString()
        r.stack.push(Pair(result, Token.BOOLEAN))
    }

    fun cond(r : Runtime) {
        //if we would evalCompound = true when both true and false sections would eval when compound
        val arguments = r.fetchNParams(3)
        //process condition
        var (cond,tokCond) = arguments[2]
        when (tokCond) {
            Token.COMPOUND -> {
                r.evaluate(cond)
                val result = r.fetchNParams(1,false, Token.BOOLEAN)[0]
                cond = result.first
                tokCond = result.second
            }
            Token.BOOLEAN -> {}
            else -> throw IllegalArgumentException("Condition needs to evaluate to a boolean")
        }
        //println("DEBUG: Cond result $cond")
        if (cond.toBoolean()) {
            r.evaluate(arguments[1].first)
        }else{
            //println("DEBUG: eval else case: ${arguments[0].first}")
            r.evaluate(arguments[0].first)
        }
    }

    fun eq(r: Runtime) {
        val argument = r.fetchNParams(2,true)
        val (valueA, tokA) = argument[1]
        val (valueB, tokB) = argument[0]
        if (tokA != tokB) throw IllegalArgumentException("Arguments must have the same type for eq")
        when(tokA) {
            Token.NUMBER -> {
                r.stack.push(Pair((valueA.toFloat() == valueB.toFloat()).toString(), Token.BOOLEAN))
            }
            else -> r.stack.push(Pair((valueA == valueB).toString(), Token.BOOLEAN))
        }
    }

    fun le(r: Runtime) {
        val argument = r.fetchNParams(2,true, Token.NUMBER)
        val (valueA, tokA) = argument[1]
        val (valueB, tokB) = argument[0]
        if (tokA != tokB) throw IllegalArgumentException("Arguments must have the same type for eq")
        r.stack.push(Pair((valueA.toFloat() < valueB.toFloat()).toString(), Token.BOOLEAN))
    }

    fun ge(r: Runtime) {
        val argument = r.fetchNParams(2,true, Token.NUMBER)
        val (valueA, tokA) = argument[0]
        val (valueB, tokB) = argument[1]
        if (tokA != tokB) throw IllegalArgumentException("Arguments must have the same type for eq")
        r.stack.push(Pair((valueA.toFloat() > valueB.toFloat()).toString(), Token.BOOLEAN))
    }
}