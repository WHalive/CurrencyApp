package com.example.mycurrencyapp.currency

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycurrencyapp.R
import com.example.mycurrencyapp.network.CurrencyApi
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CurrencyViewModel : ViewModel() {

    private var _originCurrency = MutableLiveData<String>("cad")
    val originCurrency: LiveData<String> get() = _originCurrency
    private var _resultCurrency = MutableLiveData<String>("eur")
    val resultCurrency: LiveData<String> get() = _resultCurrency
    private var _originRate = MutableLiveData<BigDecimal>()
    val originRate: LiveData<BigDecimal> get() = _originRate
    private var _resultRate = MutableLiveData<BigDecimal>()
    val resultRate: LiveData<BigDecimal> get() = _resultRate
    private var _result = MutableLiveData(BigDecimal.ZERO)
    val result: LiveData<BigDecimal> get() = _result
    private val _originImage = MutableLiveData(R.drawable.icon_cana)
    val originImage: LiveData<Int> get() = _originImage
    private val _resultImage = MutableLiveData(R.drawable.icon_eur)
    val resultImage: LiveData<Int> get() = _resultImage


    fun listItemClick(isOrigin: Boolean, item: Currency) {
        if (isOrigin) {
            _originCurrency.value = item.iso
            _originImage.value = item.icon
            calculate()
        } else {
            _resultCurrency.value = item.iso
            _resultImage.value = item.icon
            calculate()
        }
        calculate()
    }

    fun calculate() {
        viewModelScope.launch {
            try {
                val originRateResponse =
                    CurrencyApi.retrofitService.convertCurrency(_originCurrency.value)
                val currencyRates =
                    originRateResponse[_originCurrency.value] as? Map<String, Double>
                _originRate.value =
                    currencyRates?.get(_resultCurrency.value)?.toString()?.toBigDecimal()

                val resultRateResponse =
                    CurrencyApi.retrofitService.convertCurrency(_resultCurrency.value)
                val currencyRates2 =
                    resultRateResponse[_resultCurrency.value] as? Map<String, Double>
                _resultRate.value =
                    currencyRates2?.get(_originCurrency.value)?.toString()?.toBigDecimal()

            } catch (e: Exception) {
                Log.d("TAG", "$e")
            }
        }
    }

    fun swap() {
        val amount = _originCurrency.value
        _originCurrency.value = _resultCurrency.value
        _resultCurrency.value = amount!!

        val amountRate = _originRate.value
        _originRate.value = _resultRate.value
        _resultRate.value = amountRate!!

        val amountImage = _originImage.value
        _originImage.value = _resultImage.value
        _resultImage.value = amountImage!!
    }
}