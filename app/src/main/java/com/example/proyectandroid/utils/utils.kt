package com.example.proyectandroid.utils

import android.content.Context
import android.widget.Toast

fun showLongMessage(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun showShortMessage(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}