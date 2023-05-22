package com.kansha.redirectNow.internal.extension

import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kansha.redirectNow.R

fun Snackbar.setErrorStyle(): Snackbar = apply {
    val errorBgColor = ContextCompat.getColor(context, R.color.red)
    setBackgroundTint(errorBgColor)
}