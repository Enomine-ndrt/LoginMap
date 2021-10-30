package com.example.loginmap

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton constructor(context: Context){
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context:Context)=
            INSTANCE ?: synchronized (this){
                INSTANCE ?: VolleySingleton(context).also{
                    INSTANCE = it
                }
            }
    }
    private val requestQueque: RequestQueue by lazy {
        //aplicationContext is key it keeps you from leaking the
        //Activity or broadcastReceiver if someone passes one in
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>){
        requestQueque.add(req)
    }
}