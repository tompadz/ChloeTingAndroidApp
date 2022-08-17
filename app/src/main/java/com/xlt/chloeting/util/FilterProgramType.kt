package com.xlt.chloeting.util

import androidx.annotation.StringRes
import com.xlt.chloeting.R

enum class FilterProgramType(val type:String, @StringRes val title:Int) {
    ALL(type = "", title = R.string.tab_program_all),
    ABS(type = "Abs", title = R.string.tab_program_abs),
    ARMS(type = "Arms", title = R.string.tab_program_arms),
    BOOTY(type = "Booty", title = R.string.tab_program_booty),
    CORE(type = "Core", title = R.string.tab_program_core),
    FULL_BODY(type = "Full Body", title = R.string.tab_program_full_body),
    LEGS(type = "Legs", title = R.string.tab_program_legs),
    LOWER_BODY(type = "Lower Body", title = R.string.tab_program_lower_body)
}