package com.xlt.chloeting.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.xlt.chloeting.R
import com.xlt.chloeting.util.HTTPError
import retrofit2.HttpException

class ErrorView : FrameLayout {

    private val TAG = "ERROR_VIEW"

    constructor(_context: Context):super(_context, null){
        LayoutInflater.from(_context).inflate(R.layout.widget_error, this)
    }

    constructor(context: Context, attrs: AttributeSet?) :super(context, attrs){
        LayoutInflater.from(context).inflate(R.layout.widget_error, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        rootView.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    fun setError(throwable : Throwable?) {
        val desc = findViewById<TextView>(R.id.textDesc)
        if (throwable != null)
            desc.setText(getErrorInfoFromThrowable(throwable))
        else
            desc.setText(HTTPError.UNKNOWN_ERROR.desc)
        Log.e(TAG, desc.text.toString())
    }

    private fun getErrorInfoFromThrowable(throwable : Throwable):Int {
        if (throwable !is HttpException) return  HTTPError.UNKNOWN_ERROR.desc
        return HTTPError.values().find { it.code == throwable.code() }?.desc ?: HTTPError.UNKNOWN_ERROR.desc
    }

    fun addReturnClickListener(listener: OnClickListener) {
        findViewById<MaterialButton>(R.id.buttonRetry).setOnClickListener(listener)
    }
}