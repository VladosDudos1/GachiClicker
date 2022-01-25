package vlados.dudos.gachiclicker.common.ui.fragments

import android.content.ContentValues.TAG
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
import com.google.firebase.firestore.FirebaseFirestore
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.app.App
import vlados.dudos.gachiclicker.common.ui.activity.LoginActivity
import vlados.dudos.gachiclicker.common.ui.models.User
import vlados.dudos.gachiclicker.databinding.FragmentRegBinding

class RegistrationFragment : Fragment() {

    private lateinit var b: FragmentRegBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var store = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentRegBinding.bind(
            inflater.inflate(R.layout.fragment_reg, container, false)
        )
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    private fun onClick() {
        b.btnToAuth.setOnClickListener {
            (activity as LoginActivity).fragmentTransaction(AuthFragment())
        }
        b.doneBtn.setOnClickListener {
            val nick = b.inputNick.text.toString()
            val mail = b.inputEmail.text.toString()
            val password = b.inputPassword.text.toString()
            val userBase = User(nick, mail, "0", "1", "0")

            if (checkInput()) {
                auth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)

                            store.collection("Users").document("user:$mail")
                                .set(userBase)
                                .addOnCompleteListener { d ->
                                    if (d.isSuccessful) {
                                        (activity as LoginActivity).fragmentTransaction(AuthFragment())
                                    } else makeToast(d.exception!!.message.toString())
                                }
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            makeToast("Authentication failed.")
                            updateUI(null)
                        }
                    }
            }

//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val user = User(nick, mail, password, "0", "1", "0")
//                            FirebaseFirestore.getInstance().collection("Users")
//                                .add(user)
//                                .addOnSuccessListener { documentReference ->
//                                    Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.id)
//                                }
//                                .addOnFailureListener { e ->
//                                    Log.w(TAG, "Error adding document", e)
//                                }
//
//                        } else makeToast(task.exception!!.message.toString())
//                    }
        }
    }

    private fun checkInput(): Boolean {
        when {
            !Patterns.EMAIL_ADDRESS.matcher(b.inputEmail.text)
                .matches() -> makeToast("Input correct email")
            b.inputEmail.text.isNullOrEmpty() -> makeToast("Input your email")
            b.inputNick.text.isNullOrEmpty() -> makeToast("Input your nick")
            b.inputPassword.text.isNullOrEmpty() -> makeToast("Input your password")
            b.inputPassword.text.toString().length < 6 -> makeToast("Password have to be 6 symbols at least")
            b.repeatPassword.text.toString() != b.inputPassword.text.toString() -> makeToast("Incorrect password repeat")
            else -> return true
        }
        return false
    }

    private fun makeToast(m: String) {
        Toast.makeText(activity, m, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(account: FirebaseUser?) {
        if (account != null)
            makeToast("Successfully Registered ")
        else makeToast("You didn't signed up")

    }
}