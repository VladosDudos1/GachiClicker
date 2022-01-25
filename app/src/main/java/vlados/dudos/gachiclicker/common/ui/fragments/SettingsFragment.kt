package vlados.dudos.gachiclicker.common.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.app.App
import vlados.dudos.gachiclicker.common.Case.shopList
import vlados.dudos.gachiclicker.common.ui.activity.LoginActivity
import vlados.dudos.gachiclicker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var b: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentSettingsBinding.bind(
            inflater.inflate(
                R.layout.fragment_settings,container, false
            )
        )
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSwitchState()
        onSwitch()
        onClick()
    }

    private fun onSwitch(){
        b.biometrySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            App.dm.saveBiometryState(isChecked)
        }
        b.soundSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            App.dm.setSoundState(isChecked)
        }
    }
    private fun setSwitchState(){
        b.biometrySwitch.isChecked = App.dm.getBioState()
        b.soundSwitch.isChecked = App.dm.getSoundState()
    }
    private fun onClick(){
        b.logoutBtn.setOnClickListener {
            App.dm.logout()
            App.dm.saveBiometryState(false)
            shopList.clear()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
    }
}