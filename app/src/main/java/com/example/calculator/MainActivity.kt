package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calculator.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val calcViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("UNCHECKED_CAST")
        val buttons: Sequence<Button> =
            binding.keyboard.children.filter { it is Button } as Sequence<Button>

        for (button in buttons) {
            val buttonChar = button.text.toString().first()

            //Set onClickListener to Calculator buttons
            when (button.tag) {
                "number" -> {
                    button.setOnClickListener {
                        calcViewModel.onNumberClicked(buttonChar.digitToInt())
                        calcViewModel.updateFocusView(FocusedView.EXPRESSION_VIEW)
                    }
                }
                "operator" -> {
                    button.setOnClickListener {
                        calcViewModel.onOperatorClicked(buttonChar)
                        calcViewModel.updateFocusView(FocusedView.EXPRESSION_VIEW)
                    }
                }
                "clear" -> {
                    button.setOnClickListener {
                        calcViewModel.clear()
                        calcViewModel.updateFocusView(FocusedView.RESULT_VIEW)
                    }
                }
                "equals" -> {
                    button.setOnClickListener {
                        calcViewModel.updateFocusView(FocusedView.RESULT_VIEW)
                    }
                }
            }
        }

        //Update UI
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                calcViewModel.uiState.collect {
                    val expressionStr = it.expression
                    binding.expressionView.text = expressionStr
                    binding.expressionView.contentDescription = expressionStr

                    val resultStr = getString(R.string.result, it.resultNum.toString())
                    binding.resultView.text = resultStr
                    binding.resultView.contentDescription = resultStr


                    when (it.viewFocused) {
                        FocusedView.EXPRESSION_VIEW -> {
                            binding.expressionView.setTextAppearance(R.style.heading_focused)
                            binding.resultView.setTextAppearance(R.style.heading_unfocused)
                        }
                        FocusedView.RESULT_VIEW -> {
                            binding.resultView.setTextAppearance(R.style.heading_focused)
                            binding.expressionView.setTextAppearance(R.style.heading_unfocused)
                        }
                    }
                }
            }
        }
    }
}