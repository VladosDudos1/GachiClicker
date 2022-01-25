package vlados.dudos.gachiclicker.common.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.ui.fragments.AuthFragment
import vlados.dudos.gachiclicker.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        fragmentTransaction(AuthFragment())
    }


    fun fragmentTransaction(fmt: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, fmt).addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}