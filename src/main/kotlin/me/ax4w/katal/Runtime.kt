package me.ax4w.katal

import io.github.classgraph.ClassGraph
import me.ax4w.katal.exceptions.IllegalStatementException
import java.util.Stack
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.valueParameters

class Runtime() {

    val stack = Stack<Value>()

    private val functions = mutableMapOf<String, (Runtime) -> Unit>()
    private val runtimeFunctions = mutableMapOf<String, Pair<Int,String>>()

    init {
        loadLibrariesFromPackage("me.ax4w.katal.lib")
    }

    fun loadLibrariesFromPackage(p: String) {
        val scan = ClassGraph().enableClassInfo().acceptPackages(p).scan()
        scan.allClasses.forEach { classInfo ->
            try {
                val clazz = classInfo.loadClass().kotlin
                val clazzInstance = clazz.objectInstance
                if (clazzInstance != null) {
                    registerModule(clazzInstance)
                }
            } catch (e: Exception) {
                println("Exception while loading $classInfo : ${e.message}")
            }
        }
    }

    fun registerModule(m: Any) {
        (m::class).declaredMemberFunctions.forEach { function ->
            //check if it is a compatible type
            if (function.valueParameters.size == 1 && function.valueParameters[0].type.classifier == Runtime::class) {
                functions[function.name] = { rt: Runtime -> function.call(m, rt) }
            }
        }
    }

    private fun callFunction(value: String) {
        val fn = functions[value]
        if (fn == null) {
            val rtFn = runtimeFunctions[value]
            if (rtFn == null) {
                println("No function found for $value")
            }else{
                if (stack.size < rtFn.first) throw IllegalStatementException("Function $value needs ${rtFn.first} parameters on stack")
                executeRuntimeFunction(rtFn.second)
            }
        } else {
            fn.invoke(this)
        }
    }

    private fun executeRuntimeFunction(rtFn: String) {
        evaluate(rtFn)
    }

    private fun storeDeclaration(value: String) {
        val parts = value.split("$")
        runtimeFunctions[parts[0]] = Pair(parts[1].toInt(), parts[2])
    }

    fun evaluate(input: String) {
        val s = Scanner(input)
        val tokenIterator = s.tokens().iterator()
        try {
            while (tokenIterator.hasNext()) {
                val (value, tok) = tokenIterator.next()
                when (tok) {
                    Token.FUNCTION -> callFunction(value)
                    Token.DECLARATION -> storeDeclaration(value)
                    else -> {
                        when (tok) {
                            Token.NUMBER -> stack.push(Value.Num(value.toDouble()))
                            Token.STRING -> stack.push(Value.Str(value))
                            Token.BOOLEAN -> stack.push(Value.Bool(value.toBoolean()))
                            Token.COMPOUND -> stack.push(Value.Compound(value))
                            else -> {/*unreachable*/}
                        }
                    }
                }
            }
        } catch (e: Exception) {
            val realError = e.cause ?: e
            println("Error: ${realError.message}")
        }
    }

    fun fetchNParams(amount: Int, evalCompound : Boolean = false, vararg types: KClass<out Value>) : List<Value> {
        val params = mutableListOf<Value>()
        for (i in 1..amount) {
            if (stack.isEmpty()) throw IllegalStateException("Not enough arguments")
            val v = stack.pop()
            params.add(v)
        }
        return params.reversed().map { v ->
            var value = v
            if (v is Value.Compound && evalCompound) {
                evaluate(v.value)
                value = fetchNParams(1, false, *types)[0]
            }
            if (types.isNotEmpty() && !types.any { clazz -> clazz.isInstance(value) })
                throw IllegalArgumentException("Invalid parameter $v, required ${types.joinToString { it.toString() }}")
            value
        }
    }
}