package com.example.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<CalculatorUiState> =
        MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    //Changes the result
    fun compute(number: Int) {
        Log.d("CalculatorViewModel", "compute()")
        _uiState.update {
            val oldResultNum = it.resultNum
            it.copy(
                resultNum = when (it.lastOperator) {
                    '+' -> oldResultNum + number
                    '-' -> oldResultNum - number
                    '*' -> oldResultNum * number
                    '/' -> oldResultNum / number
                    else -> number
                }
            )
        }
    }

    fun updateLastOperator(newOperator: Char) {
        _uiState.update { it.copy(lastOperator = newOperator) }
    }

    fun clear() {
        _uiState.update {
            it.copy(resultNum = 0, lastOperator = '=')
        }
        Log.d("CalculatorViewModel", "clear()")
    }
}