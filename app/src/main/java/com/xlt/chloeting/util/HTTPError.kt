package com.xlt.chloeting.util

import androidx.annotation.StringRes
import com.xlt.chloeting.R

enum class HTTPError(val code:Int, @StringRes val desc:Int) {
    INTERNAL_ERROR(500, R.string.error_internal_title),
    NOT_IMPL_ERROR(501, R.string.error_not_impl),
    BAD_GATEWAY_ERROR(502, R.string.error_bad_gateway),
    UNAVAILABLE_ERROR(503, R.string.error_unavailable),
    TIMEOUT_ERROR(504, R.string.error_timeout),
    BAD_REQUEST(400, R.string.error_bad_request),
    UNKNOWN_ERROR(code = 1, R.string.error_unknown),
}