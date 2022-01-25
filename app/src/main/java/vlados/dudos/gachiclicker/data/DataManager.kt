package vlados.dudos.gachiclicker.data

import android.content.Context
import android.content.SharedPreferences

class DataManager(private val baseContext: Context) {
    private val shared: SharedPreferences

    init {
        shared = baseContext.getSharedPreferences("cum", Context.MODE_PRIVATE)
    }

    //settings
    fun getBioState() : Boolean = shared.getBoolean("biometry", false)
    fun saveBiometryState(b: Boolean) = shared.edit().putBoolean("biometry", b).apply()
    fun getSoundState() : Boolean = shared.getBoolean("wewe", false)
    fun setSoundState(b: Boolean) = shared.edit().putBoolean("wewe", b).apply()

    //authorisation
    fun isLogin(): Boolean = shared.getBoolean("isLogin", false)

    fun endLogin() = shared.edit().putBoolean("isLogin", true).apply()

    fun logout(): Boolean = shared.edit().putBoolean("isLogin", false).commit()

    fun getUserMail() : String = shared.getString("mail", "")!!
    fun setUserMail(str: String) = shared.edit().putString("mail", str).commit()
}
