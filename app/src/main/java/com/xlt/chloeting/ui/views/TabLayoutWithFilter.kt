package com.xlt.chloeting.ui.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout
import com.xlt.chloeting.util.FilterProgramType

class TabLayoutWithFilter : TabLayout {

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet : AttributeSet) : super(context, attributeSet)

    private var listener:FilterChangeListener? = null

    val filters = mutableListOf(
        FilterProgramType.ALL,
        FilterProgramType.ABS,
        FilterProgramType.ARMS,
        FilterProgramType.BOOTY,
        FilterProgramType.CORE,
        FilterProgramType.FULL_BODY,
        FilterProgramType.LEGS,
        FilterProgramType.LOWER_BODY,
    )

    init {
       filters.forEach {
           val tab = newTab()
           tab.setText(it.title)
           addTab(tab)
       }
        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab : Tab?) {
                if (tab == null) return
                listener?.onFilterChange(filters[tab.position])
            }

            override fun onTabUnselected(tab : Tab?) {
               //empty
            }

            override fun onTabReselected(tab : Tab?) {
                //empty
            }
        })
    }

    fun setActiveTab(filter : FilterProgramType) {
        filters.forEachIndexed { index, type ->
            if (type == filter) {
                this.selectTab(getTabAt(index))
            }
        }
    }

    fun addFilterChangeListener(listener:FilterChangeListener){
        this.listener = listener
    }

    interface FilterChangeListener {
        fun onFilterChange(filter:FilterProgramType)
    }
}