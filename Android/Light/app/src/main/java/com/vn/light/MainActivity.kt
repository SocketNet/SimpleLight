package com.vn.light

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.android.volley.VolleyError
import org.apache.commons.lang3.exception.ExceptionUtils

class MainActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var textView: TextView
    lateinit var lightInterface: LightInterface


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        lightInterface.lightState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lightInterface = LightInterface(this, object : LightInterface.Callback {
            override fun lightState(state: String) {
                textView.text = state
            }

            override fun onError(err: VolleyError) {
                Log.e("MainActivity", ExceptionUtils.getStackTrace(err))
                textView.text = "invalid"
            }
        })

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        textView = findViewById(R.id.textView);

        button.setOnClickListener {
            lightInterface.switchLight()
        }
    }

}