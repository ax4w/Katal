package me.ax4w.katal

fun main() {
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