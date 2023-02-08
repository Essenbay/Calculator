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
    private fun getCalculation(): Double {
        return _uiState.value.run {
            val firstNum = this.firstNum.toDouble()
            val secondNum = this.secondNum.toDouble()
            when (this.operator) {
                '+' -> firstNum.plus(secondNum)
                '-' -> firstNum.minus(secondNum)
                '×' -> firstNum.times(secondNum)
                '/' -> firstNum.div(secondNum)
                else -> throw IllegalArgumentException("Operator not found")
            }
        }
    }

    fun equals() {
        if (_uiState.value.operator != '=') {
            _uiState.update { oldUi ->
                oldUi.copy(
                    firstNum = "",
                    secondNum = "",
                    result = getCalculation(),
                    operator = ' ',
                    showResult = true
                )
            }
        }
    }

    fun showExpression() {
        _uiState.update {
            it.copy(
                showResult = false
            )
        }
    }

    fun onOperatorClicked(newOperator: Char) {
        _uiState.update { oldState ->
            if (oldState.showResult) {
                oldState.copy(
                    firstNum = formatResult(oldState.result),
                    operator = newOperator,
                    showResult = false
                )
            } else {
                oldState.copy(
                    operator = newOperator,
                )
            }
        }
    }

    fun onNumberClicked(newNumber: Int) {
        _uiState.update { oldState ->
            if (oldState.showResult) {
                oldState.copy(
                    firstNum = newNumber.toString(),
                    operator = ' ',
                    showResult = false
                )
            }
            else if (newNumber == 0 && (oldState.firstNum.isEmpty() && oldState.secondNum.isEmpty())) {
                oldState
            }
            //Change first number
            else if (oldState.operator == ' ') {
                val firstNum = oldState.firstNum
                //If empty or 0
                if (firstNum.isEmpty() || firstNum == "0") {
                    oldState.copy(
                        firstNum = newNumber.toString(),
                        operator = ' '
                    )
                }
                //Else append new number
                else {
                    oldState.copy(
                        firstNum = oldState.firstNum.plus(newNumber),
                        operator = ' '
                    )
                }
            }
            //Change second number
            else {
                val secondNum = oldState.secondNum
                //If empty or 0
                if (secondNum.isEmpty() || secondNum == "0") {
                    oldState.copy(
                        secondNum = newNumber.toString(),
                    )
                }
                //Else append new number
                else {
                    oldState.copy(
                        secondNum = oldState.secondNum.plus(newNumber),
                    )
                }
            }

        }
    }

    fun clear() {
        _uiState.update {
            CalculatorUiState()
        }
    }
}