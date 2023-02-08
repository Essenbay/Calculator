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

        val buttons: Sequence<Button> =
            binding.keyboard?.children?.filter { it is Button } as Sequence<Button>

        for (button in buttons) {
            val buttonChar = button.text.toString().first()

            when (button.tag) {
                "number" -> {
                    button.setOnClickListener {
                        calcViewModel.compute(buttonChar.digitToInt())
                    }
                }
                "operator" -> {
                    button.setOnClickListener {
                        calcViewModel.updateLastOperator(buttonChar)
                    }
                }
                "clear" -> {
                    button.setOnClickListener {
                        calcViewModel.clear()
                    }
                }
            }
        }

        //Update UI
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                calcViewModel.uiState.collect {
                    binding.result.text = "${it.lastOperator} ${it.resultNum}"
                }
            }
        }
    }
}