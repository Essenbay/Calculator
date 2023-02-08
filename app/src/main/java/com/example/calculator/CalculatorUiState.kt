package com.example.calculator

data class CalculatorUiState(
    val firstNum: String = "",
    val secondNum: String = "",
    val operator: Char = ' ',
    val result: Double = 0.0,
    val showResult: Boolean = true
)