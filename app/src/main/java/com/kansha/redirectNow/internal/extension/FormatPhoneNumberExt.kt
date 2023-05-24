package com.kansha.redirectNow.internal.extension

fun formatPhoneNumber(phoneNumber: String): String {
    val formattedNumber = StringBuilder()

    // Remove todos os caracteres que não são dígitos
    val digitsOnly = phoneNumber.replace("\\D".toRegex(), "")

    // Verifica se o número de telefone tem pelo menos 10 dígitos
    if (digitsOnly.length >= 10) {
        formattedNumber.append("(")
        formattedNumber.append(digitsOnly.substring(0, 2))
        formattedNumber.append(") ")
        formattedNumber.append(digitsOnly.substring(2, 7))
        formattedNumber.append("-")
        formattedNumber.append(digitsOnly.substring(7))
    } else {
        formattedNumber.append(digitsOnly)
    }

    return formattedNumber.toString()
}