package com.example.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<CalculatorUiState> =
        MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    //Returns result, immutable
    private fun computeToExpression(number: Int): Int {
        return _uiState.value.run {
            when (this.lastOperator) {
                '+' -> this.resultNum + number
                '-' -> this.resultNum - number
                '×' -> this.resultNum * number
                '/' -> this.resultNum / number
                '=' -> this.resultNum
                else -> throw IllegalArgumentException("Operator not found")
            }
        }
    }

    fun onOperatorClicked(newOperator: Char) {
        val isExprFocused = _uiState.value.viewFocused == FocusedView.EXPRESSION_VIEW
        //If expression is printing, change the operator
        if (isExprFocused) {
            _uiState.update {
                it.copy(
                    expression = it.expression + newOperator,
                    lastOperator = newOperator
                )
            }
        }
        //If result is done, change expression as result
        else {
            _uiState.update {
                it.copy(
                    //initial value of resultNum is 0
                    expression = it.resultNum.toString() + newOperator,
                    lastOperator = newOperator
                )
            }
        }
    }

    fun onNumberClicked(newNumber: Int) {
        val isExprFocused = _uiState.value.viewFocused == FocusedView.EXPRESSION_VIEW
        if (isExprFocused) {
            _uiState.update {
                it.copy(
                    expression = it.expression + newNumber,
                    resultNum = computeToExpression(newNumber)
                )
            }
        }
        else {
            _uiState.update {
                it.copy(
                    //initial value of resultNum is 0
                    expression = newNumber.toString(),
                    resultNum = newNumber
                )
            }
        }
    }

    fun clear() {
        _uiState.update {
            CalculatorUiState()
        }
    }

    fun updateFocusView(view: FocusedView) {
        _uiState.update {
            it.copy(viewFocused = view)
        }
    }

}