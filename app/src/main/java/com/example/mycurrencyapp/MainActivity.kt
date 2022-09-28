package com.example.mycurrencyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.example.mycurrencyapp.databinding.ActivityMainBinding
import com.example.mycurrencyapp.ui.MyKeyboardView
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.example.mycurrencyapp.currency.CurrencyViewModel
import java.math.RoundingMode
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    private val viewModel: CurrencyViewModel by viewModels()

    private var _binding: ActivityMainBinding? = null
    private val viewBinding get() = _binding!!
    private lateinit var editText: EditText
    private lateinit var keyboard: MyKeyboardView
    private lateinit var inputAmount: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        keyboard = viewBinding.keyboard
        editText = viewBinding.converter
        editText.showSoftInputOnFocus = false

        val inputConnection = editText.onCreateInputConnection(EditorInfo())
        keyboard.setUpInputConnection(inputConnection)

        observeUI()
        viewModel.calculate()

        viewBinding.currencyImage.setOnClickListener {
            showCurrencyDialog(true)
        }
        viewBinding.currencyImage2.setOnClickListener {
            showCurrencyDialog(false)
        }
        viewBinding.currency.setOnClickListener {
            showCurrencyDialog(true)
        }

        viewBinding.currency2.setOnClickListener {
            showCurrencyDialog(false)
        }
        viewBinding.currencyRatio.setOnClickListener {
            showCurrencyDialog(true)
        }

        viewBinding.currencyRatio2.setOnClickListener {
            showCurrencyDialog(false)
        }

        viewBinding.swapCurrency.setOnClickListener {
            calculateResultSwap()
            viewModel.swap()
        }

        viewBinding.converter.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateResult()
            }

            override fun afterTextChanged(s: Editable) {
                editText.removeTextChangedListener(this)
                try {
                    var orgString = s.toString().replace(" ", "")

                    val decimalFormat = DecimalFormat("#,##0")
                    decimalFormat.roundingMode = RoundingMode.FLOOR

                    val formattedString = decimalFormat.format(orgString.toDouble())
                    val againFormattedString = formattedString.replace(",", " ")

                    editText.setText(againFormattedString)
                    editText.setSelection(editText.text.length)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    editText.addTextChangedListener(this)
                }
            }

        })
    }


    private fun observeUI() {
        viewModel.originCurrency.observe(this) { originCurrency ->
            viewBinding.currency.text = originCurrency
        }
        viewModel.resultCurrency.observe(this) {
            viewBinding.currency2.text = it
        }
        viewModel.originRate.observe(this) {
            viewBinding.currencyRatio.text =
                "1 ${viewModel.originCurrency.value} - ${it} ${viewModel.resultCurrency.value}"
        }
        viewModel.resultRate.observe(this) {
            viewBinding.currencyRatio2.text =
                "1 ${viewModel.resultCurrency.value} - ${it} ${viewModel.originCurrency.value}"
        }
        viewModel.originImage.observe(this) {
            viewBinding.currencyImage.setImageResource(it)
        }

        viewModel.resultImage.observe(this) {
            viewBinding.currencyImage2.setImageResource(it)
        }
    }

    private fun calculateResult() {
        inputAmount = viewBinding.converter.text.toString().replace(" ", "")
        val decimalFormat = DecimalFormat("#,##0.000")
        decimalFormat.roundingMode = RoundingMode.FLOOR

        if (inputAmount.isEmpty()) {
            viewModel.result.observe(this) {
                viewBinding.currencyResult.text = "0"
            }
        } else {
            val result = "${
                (inputAmount.toDouble() * viewModel.originRate.value.toString().toDouble()).toBigDecimal()
            }".toDouble()

            val formattedResult = decimalFormat.format(result).replace(',', ' ')
            viewModel.result.observe(this) {
                viewBinding.currencyResult.text = formattedResult
//                viewModel.calculate()
            }
        }
    }

    private fun calculateResultSwap() {
        inputAmount = viewBinding.converter.text.toString().replace(" ", "")
        val decimalFormat = DecimalFormat("#,##0.000")
        decimalFormat.roundingMode = RoundingMode.FLOOR
        if (inputAmount.isEmpty()) {
            viewModel.result.observe(this) {
                viewBinding.currencyResult.text = "0"
            }
        } else {
            val result = "${
                (inputAmount.toDouble() * viewModel.resultRate.value.toString().toDouble()).toBigDecimal()
            }".toDouble()

            val formattedResult = decimalFormat.format(result).replace(',', ' ')
            viewModel.result.observe(this) {
                viewBinding.currencyResult.text = formattedResult
            }
        }
    }

    private fun showCurrencyDialog(isOrigin: Boolean) {
        CurrencyDialogFragment(isOrigin).show(supportFragmentManager, "dialog")
    }
}