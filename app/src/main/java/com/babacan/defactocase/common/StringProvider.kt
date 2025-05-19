package com.babacan.defactocase.common

import android.content.Context
import javax.inject.Inject

interface StringProvider {
    fun getString(resId: Int): String
}

class DefaultStringProvider @Inject constructor(
    private val context: Context
) : StringProvider {
    override fun getString(resId: Int): String = context.getString(resId)
}