package com.parkingmanagerapp.view.regUserPanel.reservationSystem

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.Locale

class LicensePlateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val filteredText = text.text.filter { it.isLetterOrDigit() }.uppercase(Locale.getDefault())
        val annotatedString = AnnotatedString(filteredText)

        return TransformedText(
            annotatedString,
            OffsetMapping.Identity
        )
    }

    companion object {
        fun transform(input: String): String {
            return input.filter { it.isLetterOrDigit() }.uppercase(Locale.getDefault())
        }
    }
}