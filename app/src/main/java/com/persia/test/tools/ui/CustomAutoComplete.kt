package com.persia.test.tools.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.persia.test.R
import timber.log.Timber
import java.util.logging.Filter

class CustomAutoComplete @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = androidx.appcompat.R.attr.autoCompleteTextViewStyle
) :
    AppCompatAutoCompleteTextView(context, attrs, defStyleAttr) {

    override fun performFiltering(text: CharSequence?, keyCode: Int) {
        super.performFiltering(text, keyCode)
        Timber.i("perform filter")
    }

}