package com.kansha.redirectNow.internal.extension


import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PhoneNumberTextWatcher(private val editText: EditText) : TextWatcher {
    private var isFormatting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting) {
            return
        }

        isFormatting = true

        // Clear the previous input formatting
        val digits = s?.toString()?.replace("[^\\d]".toRegex(), "") ?: ""
        val formattedPhoneNumber = formatPhoneNumber(digits)

        // Check if the formatted phone number exceeds the maximum limit
        val maxLength = 23
        if (formattedPhoneNumber.length <= maxLength) {
            editText.setText(formattedPhoneNumber)
        } else {
            // Truncate the phone number to the maximum limit
            editText.setText(formattedPhoneNumber.substring(0, maxLength))
        }

        // Move the cursor to the end of the text
        editText.setSelection(editText.text?.length ?: 0)

        isFormatting = false
    }

    private fun formatPhoneNumber(digits: String): String {
        val formatted = StringBuilder()

        // Add '(' if it exists
        if (digits.length > 0) {
            formatted.append("(")
        }

        // Add the first 2 digits
        if (digits.length >= 2) {
            formatted.append(digits.substring(0, 2))
        } else {
            formatted.append(digits)
        }

        // Add ')' if it exists
        if (digits.length > 2) {
            formatted.append(")")
        }

        // Add the next 5 digits
        if (digits.length > 2 && digits.length <= 7) {
            formatted.append(digits.substring(2))
        } else if (digits.length > 7) {
            formatted.append(digits.substring(2, 7))
            formatted.append("-")
            formatted.append(digits.substring(7))
        }

        return formatted.toString()
    }
}