package me.ax4w.katal.lib

import me.ax4w.katal.Runtime
import me.ax4w.katal.Value

object Str {

    fun concat(r : Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Str::class)
        r.stack.push(Value.Str(arguments[0].asStr() + arguments[1].asStr()))
    }

    fun contains(r: Runtime) {
        val arguments = r.fetchNParams(2, true, Value.Str::class)
        r.stack.push(Value.Bool(arguments[0].asStr().contains(arguments[1].asStr())))
    }
}