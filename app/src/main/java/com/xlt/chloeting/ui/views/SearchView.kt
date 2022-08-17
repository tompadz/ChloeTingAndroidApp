package com.xlt.chloeting.ui.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class SearchView: TextInputEditText {

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet : AttributeSet) : super(context, attributeSet)

    private var listener:SearchListener? = null

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {
                //empty
            }

            override fun onTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {
                //empty
            }

            override fun afterTextChanged(query : Editable?) {
                listener?.onQueryChange(query.toString())
            }
        })
    }

    interface SearchListener {
      fun onQueryChange(query:String)
    }

    fun setOnSearchListener(listener:SearchListener) {
        this.listener = listener
    }
}