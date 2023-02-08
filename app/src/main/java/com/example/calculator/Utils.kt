package com.example.calculator

fun formatResult(result: Double): String {
    return if(result == result.toInt().toDouble()) result.toInt().toString()
    else result.toString()
}