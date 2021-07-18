package com.vn.light

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class LightInterface(ctx: Context, callback: Callback) {

    final var baseUrl: String = "http://192.168.2.171"
    final var lightSwitchPath: String = "/turn"
    final var lightStatePath: String = "/state"

    private var queue: RequestQueue = Volley.newRequestQueue(ctx)
    private var call: Callback = callback


    interface Callback {
        fun lightState(state: String)
        fun onError(err: VolleyError)
    }

    /**
     * 切换灯状态
     */
    fun switchLight() {
        Log.i("MyLightService", "switchLight Request $baseUrl$lightSwitchPath")
        val stringRequest = StringRequest(Request.Method.GET, baseUrl + lightSwitchPath, {
            lightState()
        }, {
            call.onError(it)
        })
        queue.add(stringRequest)
    }

    /**
     * 更新图标状态，与实际灯状态对应
     */
    fun lightState() {
        Log.i("MyLightService", "lightState Request $baseUrl$lightStatePath")
        val stringRequest =
            StringRequest(Request.Method.GET, baseUrl + lightStatePath, { response ->
                call.lightState(response)
            }, {
                call.onError(it)
            })
        queue.add(stringRequest)
    }


    class MyRetry : RetryPolicy {
        private final var defTimeout = 1000
        private var defRetryCount = 3

        override fun getCurrentTimeout(): Int {
            return defTimeout
        }

        override fun getCurrentRetryCount(): Int {
            return defRetryCount
        }

        override fun retry(error: VolleyError?) {
            defRetryCount--
            Log.i("LightInterface", "retry $defRetryCount")
            if (defRetryCount == 0) {
                throw error!!
            }
        }
    }


}