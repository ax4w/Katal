package me.ax4w.katal

import io.github.classgraph.ClassGraph
import java.util.Stack
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.valueParameters

class Runtime() {

    val stack = Stack<Pair<String, Token>>()

    private val functions = mutableMapOf<String, (Runtime) -> Unit>()
    private val runtimeFunctions = mutableMapOf<String, String>()

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
                executeRuntimeFunction(rtFn)
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
        runtimeFunctions[parts[0]] = parts[1]
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
                        stack.push(Pair(value, tok))
                    }
                }
            }
        } catch (e: Exception) {
            val realError = e.cause ?: e
            println("Error: ${realError.message}")
        }
    }

    fun fetchNParams(amount: Int, evalCompound : Boolean = false, vararg tokens: Token) : List<Pair<String,Token>> {
        val params = mutableListOf<Pair<String,Token>>()
        for (i in 1..amount) {
            if (stack.isEmpty()) throw IllegalStateException("Not enough arguments")
            val (value, tok) = stack.pop()
            params.add(Pair(value,tok))
        }
        return params.map { (value,tok) ->
            var v = value
            var t = tok
            if (evalCompound && t == Token.COMPOUND) {
                evaluate(v)
                val (nVal, nTok ) = fetchNParams(1, false, *tokens)[0]
                v = nVal
                t = nTok
            }
            if (tokens.isNotEmpty() && !tokens.contains(t))
                throw IllegalArgumentException("Invalid parameter $tok, required ${tokens.joinToString { it.toString() }}")
            Pair(v,t)
        }
    }
}