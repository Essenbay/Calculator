package com.example.calculator

enum class FocusedView {
    EXPRESSION_VIEW,
    RESULT_VIEW
}

data class CalculatorUiState(
    val expression: String = "",
    val resultNum: Int = 0,
    val lastOperator: Char = '=',
    val viewFocused: FocusedView = FocusedView.RESULT_VIEW
)