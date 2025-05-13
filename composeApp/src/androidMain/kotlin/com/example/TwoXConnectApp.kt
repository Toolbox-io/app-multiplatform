package com.example

import android.app.Application
import android.content.Context
import com.pr0gramm3r101.utils.UtilsLibrary
import java.lang.ref.WeakReference

class TwoXConnectApp: Application() {
    companion object {
        private lateinit var _context: WeakReference<Context>
        val context get() = _context.get()!!
    }

    override fun onCreate() {
        super.onCreate()
        _context = WeakReference(this)

        UtilsLibrary.init(this)
    }
}