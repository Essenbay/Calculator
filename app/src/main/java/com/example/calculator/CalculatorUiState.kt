package com.example.calculator

data class CalculatorUiState(
    val resultNum: Int = 0,
    val lastOperator: Char = '=',
)