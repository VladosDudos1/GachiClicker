package vlados.dudos.gachiclicker.app

import android.app.Application
import vlados.dudos.gachiclicker.data.DataManager

class App : Application() {
    companion object {
        lateinit var dm: DataManager
    }
    override fun onCreate() {
        super.onCreate()
        dm = DataManager(baseContext)
    }
}