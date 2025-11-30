package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Value

object Str {

    private fun stringToRuneArray(str: String) : Value.Array {
        return Value.Array(str.map { Value.Rune(it) }.toMutableList(), Value.Rune::class)
    }

    private fun runeArrayToStr(value : Value.Array) : String {
        if(value.type != Value.Rune::class)
            throw IllegalArgumentException("concat only works on two strings")
        return value.toStr()
    }

    fun concat(r : Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Array::class)
        val valA = runeArrayToStr((arguments[0] as Value.Array))
        val valB = runeArrayToStr((arguments[1] as Value.Array))
        r.stack.push(stringToRuneArray(valA + valB))
    }

    fun contains(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Array::class)
        val valA = runeArrayToStr((arguments[0] as Value.Array))
        val valB = runeArrayToStr((arguments[1] as Value.Array))
        r.stack.push(Value.Bool(valA.contains(valB)))
    }
}