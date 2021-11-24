package com.example.androidclubapp.utils

import android.text.Editable
import android.text.TextUtils
import android.widget.TextView

object CommonMethods {

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    fun makeTextViewHorizontalScroll(textView: TextView){
        textView.isSelected = true
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.setHorizontallyScrolling(true)
        textView.isSingleLine = true
        textView.setLines(1)
    }
}