package com.example.mycurrencyapp.currency


typealias Currencies = List<Currency>
data class Currency(
    val iso: String,
    val icon: Int
        )