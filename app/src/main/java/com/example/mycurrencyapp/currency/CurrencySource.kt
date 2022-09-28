package com.example.mycurrencyapp.currency

import com.example.mycurrencyapp.R

fun currencyList() = mutableListOf(
    Currency("USD", R.drawable.icon_america),
    Currency("UZS", R.drawable.icon_uzb),
    Currency("EUR", R.drawable.icon_eur),
    Currency("RUBLE", R.drawable.icon_rus),
    Currency("CAD", R.drawable.icon_cana)
)
