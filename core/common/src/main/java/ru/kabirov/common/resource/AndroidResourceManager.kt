package ru.kabirov.common.resource

import android.content.Context

class AndroidResourceManager(private val context: Context) : ResourceManager {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}