package com.diasandfahri.picbundles.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

object ExtendedFunctions {

    fun Context?.getLifeCycleOwner(): AppCompatActivity? = when (this) {
        is ContextWrapper ->
            if (this is AppCompatActivity) this
            else this.baseContext.getLifeCycleOwner()
        else -> null
    }
}