package com.example.mycurrencyapp.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mycurrencyapp.R
import com.example.mycurrencyapp.databinding.KeyboardBinding

class MyKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var inputConnection: InputConnection
    init {
        val binding = KeyboardBinding.inflate(LayoutInflater.from(context), this, true)

        binding.number1.setOnClickListener(::onClick)
        binding.number2.setOnClickListener(::onClick)
        binding.number3.setOnClickListener(::onClick)
        binding.number4.setOnClickListener(::onClick)
        binding.number5.setOnClickListener(::onClick)
        binding.number6.setOnClickListener(::onClick)
        binding.number7.setOnClickListener(::onClick)
        binding.number8.setOnClickListener(::onClick)
        binding.number9.setOnClickListener(::onClick)
        binding.number0.setOnClickListener(::onClick)
        binding.dot.setOnClickListener(::onClick)
        binding.backspace.setOnClickListener(::onClick)
    }


    private fun onClick(view: View?) {
        when (view?.id) {
            R.id.backspace -> inputConnection.delete()
            else -> {
                val value = (view as? TextView)?.text
                inputConnection.commitText(value, 1)
            }
        }
    }

    private fun InputConnection.delete() {
        if (getSelectedText(0).isNullOrBlank()) {
            deleteSurroundingText(1, 0)
        } else {
            commitText("", 1)
        }
    }

    fun setUpInputConnection(ic: InputConnection) {
        this.inputConnection = ic
    }

}