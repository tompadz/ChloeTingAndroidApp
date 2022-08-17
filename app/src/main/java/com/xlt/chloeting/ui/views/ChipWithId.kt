package com.xlt.chloeting.ui.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip

class ChipWithId:Chip{

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet : AttributeSet) : super(context, attributeSet)

    var id:String? = null
}