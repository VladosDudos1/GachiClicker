package vlados.dudos.gachiclicker.common.ui.activity

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import vlados.dudos.gachiclicker.app.App
import vlados.dudos.gachiclicker.databinding.ActivityLogBinding

class LogActivity : AppCompatActivity() {

    private var cancellationSignal: CancellationSignal? = null
    private val bioState = App.dm.getBioState()
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)

                    notifyUser("Welcome to the club buddy!")
                    successfulEnter()
                }
            }
    private lateinit var b: ActivityLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLogBinding.inflate(layoutInflater)
        setContentView(b.root)

        checkBiometricSupport()
        onClick()
        Handler().postDelayed({
            if (bioState){
                openBiometry()
            }
            else successfulEnter()
      }, 1000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
    private fun successfulEnter() {
        if (!App.dm.isLogin())
            startActivity(Intent(this, LoginActivity::class.java))
        else startActivity(Intent(this, GameActivity::class.java))
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by slave")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint authentication has`t been enabled in settings")
            App.dm.saveBiometryState(false)
            return false
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint permission is not enabled")
            return false
        }

        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }
    private fun openBiometry(){
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Use your DickFinger")
            .setSubtitle("Authentication is required")
            .setDescription("It`s gym rules")
            .setNegativeButton("Cancel", this.mainExecutor, { dialog, i ->
                notifyUser("Authentication cancelled")
            }).build()
        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }
    private fun onClick(){
        b.imgBilly.setOnClickListener {
            if (bioState)
                openBiometry()
        }
    }
}