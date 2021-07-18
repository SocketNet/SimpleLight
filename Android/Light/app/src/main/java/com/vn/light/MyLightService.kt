package com.vn.light

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.VolleyError
import org.apache.commons.lang3.exception.ExceptionUtils


@RequiresApi(Build.VERSION_CODES.N)
class MyLightService : TileService() {

    private lateinit var lightInterface: LightInterface

    override fun onCreate() {
        super.onCreate()
        lightInterface = LightInterface(this, object: LightInterface.Callback {

            override fun lightState(state: String) {
                if (state == "on") {
                    qsTile.state = Tile.STATE_ACTIVE
                    qsTile.label = "on"
                } else {
                    qsTile.state = Tile.STATE_INACTIVE
                    qsTile.label = "off"
                }
                qsTile.updateTile()
            }

            override fun onError(err: VolleyError) {
                qsTile.state = Tile.STATE_UNAVAILABLE
                qsTile.label = "invalid"
                qsTile.updateTile()
                Log.i("MyLightService On Error", "request fail" + ExceptionUtils.getStackTrace(err))
            }
        })
    }

    override fun onClick() {
        super.onClick()
        lightInterface.switchLight()
    }


    override fun onStartListening() {
        lightInterface.lightState()
    }

    override fun onStopListening() {
        super.onStopListening()
        Log.i("MyLightService", "onStopListening")
    }

}