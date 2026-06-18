package com.example.guitarritas

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(context: Context) {
    val requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)

    companion object {
        @Volatile
        private var instancia: VolleySingleton? = null

        fun getInstance(context: Context): VolleySingleton {
            return instancia ?: synchronized(this) {
                instancia ?: VolleySingleton(context).also { instancia = it }
            }
        }
    }
}
