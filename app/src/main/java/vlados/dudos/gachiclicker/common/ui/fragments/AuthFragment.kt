package vlados.dudos.gachiclicker.common.ui.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.app.App
import vlados.dudos.gachiclicker.common.ui.activity.GameActivity
import vlados.dudos.gachiclicker.common.ui.activity.LoginActivity
import vlados.dudos.gachiclicker.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    private lateinit var b: FragmentAuthBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentAuthBinding.bind(
            inflater.inflate(
                R.layout.fragment_auth, container, false
            )
        )
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    private fun onClick() {
        b.btnToReg.setOnClickListener {
            (activity as LoginActivity).fragmentTransaction(RegistrationFragment())
        }
        b.doneBtn.setOnClickListener {
            val mail = b.inputEmail.text.toString()
            val password = b.inputPassword.text.toString()
            if (checkInput()) {
                auth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                            App.dm.endLogin()
                            App.dm.setUserMail(user?.email.toString())
                            startActivity(Intent(requireActivity(), GameActivity::class.java))
                        } else {
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            makeToast("Authentication failed.")
                            updateUI(null)
                        }
                    }
            }
        }
        b.forgotPasswordTxt.setOnClickListener {

        }
    }

    private fun checkInput(): Boolean {
        when {
            !Patterns.EMAIL_ADDRESS.matcher(b.inputEmail.text)
                .matches() -> makeToast("Input correct email")
            b.inputEmail.text.isNullOrEmpty() -> makeToast("Input your email")
            b.inputPassword.text.isNullOrEmpty() -> makeToast("Input your password")
            else -> return true
        }
        return false
    }

    private fun makeToast(m: String) {
        Toast.makeText(activity, m, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            makeToast("You Signed In successfully")
        } else {
            makeToast("You didn't signed in")
        }
    }
}