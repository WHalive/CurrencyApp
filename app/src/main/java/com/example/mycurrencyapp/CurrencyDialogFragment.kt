package com.example.mycurrencyapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycurrencyapp.currency.Currencies
import com.example.mycurrencyapp.currency.Currency
import com.example.mycurrencyapp.currency.CurrencyViewModel
import com.example.mycurrencyapp.currency.currencyList
import com.example.mycurrencyapp.databinding.FragmentCurrencyDialogBinding
import com.example.mycurrencyapp.databinding.ItemViewBinding

class CurrencyDialogFragment(private var isOrigin: Boolean) : DialogFragment() {

    private lateinit var binding: FragmentCurrencyDialogBinding
    private val viewModel: CurrencyViewModel by activityViewModels()
    private val currencies: Currencies by lazy { currencyList() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyDialogBinding.inflate(inflater, container, false)
        updateUI(currencies)

        return binding.root
    }


    private fun updateUI(currencies: List<Currency>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = CurrencyAdapter(currencies)
    }


    private inner class CurrencyAdapter(
        private val currencies: List<Currency>
    ) :
        RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
            return CurrencyHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.context)))
        }

        override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
            val currency = currencies[position]
            holder.bind(currency)

        }

        override fun getItemCount() = currencies.size

        private inner class CurrencyHolder(private var binding: ItemViewBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(currency: Currency) {
                binding.itemCurrencyFlag.setImageResource(currency.icon)
                binding.currencyName.text = currency.iso.uppercase()
                binding.root.setOnClickListener {
                    viewModel.listItemClick(
                        isOrigin,
                        currency
                    )
                    dismiss()
                }

            }
        }
    }
}

