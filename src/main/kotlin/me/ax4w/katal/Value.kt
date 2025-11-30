package me.ax4w.katal

import kotlin.reflect.KClass

sealed class Value {

    data class Num(val value: Double) : Value()

    data class Str(val value : String) : Value()

    data class Bool(val value: Boolean) : Value()

    data class Compound(val value : String) : Value()

    data class Function(val value : String) : Value()

    data class Something(val value : Value) : Value()

    object Nothing : Value()

    data class Array(val value : MutableList<Value>, val type : KClass<out Value>) : Value() {
        fun toStr() : String {
           return value.map { it.asRune() }.joinToString("")
        }
    }

    data class Rune(val value: Char) : Value()

    fun asBoolean(): Boolean {
        return (this as? Bool)?.value ?: throw IllegalStateException("Value is not a Boolean: $this")
    }

    fun asNum(): Double {
        return (this as? Num)?.value ?: throw IllegalStateException("Value is not a Num: $this")
    }

    fun asStr(): String {
        return (this as? Str)?.value ?: throw IllegalStateException("Value is not a Str: $this")
    }

    fun asCompound(): String {
        return (this as? Compound)?.value ?: throw IllegalStateException("Value is not a Compound: $this")
    }

    fun asFunction(): String {
        return (this as? Function)?.value ?: throw IllegalStateException("Value is not a Compound: $this")
    }

    fun asSomething(): Value {
        return (this as? Something)?.value ?: throw IllegalStateException("Value is not a Something: $this")
    }

    fun asArray(): MutableList<Value> {
        return (this as? Array)?.value ?: throw IllegalStateException("Value is not a Array: $this")
    }

    fun asRune(): Char {
        return (this as? Rune)?.value ?: throw IllegalStateException("Value is not a Something: $this")
    }

    fun toDisplay(): String = when (this) {
        is Num -> value.toString()
        is Str -> "\"$value\""
        is Bool -> value.toString()
        is Compound -> "[$value]"
        is Function -> value
        is Something -> "Something($value)"
        is Nothing -> "Nothing"
        is Rune -> "'($value)'"
        is Array -> {
            when(this.type) {
                Rune::class -> "\"${(this.toStr())}\""
                else -> "(${value.joinToString(", ")})"
            }
        }
    }

}