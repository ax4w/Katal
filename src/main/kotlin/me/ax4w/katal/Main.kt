package me.ax4w.katal

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.option
import java.io.File
import java.io.InputStream


class Katal : CliktCommand() {
    val file: String? by option(help="path to a file to just run")
    override fun run() {
        if (file != null) {
            val rt = Runtime()
            val inputStream: InputStream = File(file).inputStream()
            val inputString = inputStream.bufferedReader().use { it.readText() }
            rt.evaluate(inputString)
        }else{
            repl()
        }
    }
}

fun repl() {
    val rt = Runtime()
    val inputBuffer = StringBuilder()

    var openBrackets = 0
    var insideFunctionDef = false

    while (true) {
        if (openBrackets == 0 && !insideFunctionDef) print("> ") else print("... ")

        val line = readlnOrNull() ?: break

        if (line.isBlank()) {
            if (openBrackets == 0 && !insideFunctionDef) continue
        }

        inputBuffer.append(line).append(" ")

        openBrackets += line.count { it == '[' } - line.count { it == ']' }

        val dollarCount = line.count { it == '$' }

        if (dollarCount % 2 != 0) {
            insideFunctionDef = !insideFunctionDef
        }

        if (openBrackets <= 0 && !insideFunctionDef) {
            try {
                val finalCode = inputBuffer.toString().trim()
                if (finalCode.isNotEmpty()) {
                    rt.evaluate(finalCode)
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }

            inputBuffer.clear()
            openBrackets = 0
            insideFunctionDef = false
        }
    }
}


fun main(args: Array<String>) = Katal().main(args)