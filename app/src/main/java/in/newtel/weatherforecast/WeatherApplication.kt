package `in`.newtel.weatherforecast

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class WeatherApplication : Application(){

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}