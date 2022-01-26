package vlados.dudos.gachiclicker.common.ui.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.app.App
import vlados.dudos.gachiclicker.common.Case
import vlados.dudos.gachiclicker.common.Case.clicks
import vlados.dudos.gachiclicker.common.Case.cumPerClick
import vlados.dudos.gachiclicker.common.Case.cumPerSecond
import vlados.dudos.gachiclicker.common.Case.currentCum
import vlados.dudos.gachiclicker.common.Case.cutNum
import vlados.dudos.gachiclicker.common.Case.saveData
import vlados.dudos.gachiclicker.common.Case.shopMaxLevel
import vlados.dudos.gachiclicker.common.Case.updateCurrentCum
import vlados.dudos.gachiclicker.common.Case.updateShop
import vlados.dudos.gachiclicker.common.ui.fragments.EventsFragment
import vlados.dudos.gachiclicker.common.ui.fragments.GameFragment
import vlados.dudos.gachiclicker.common.ui.fragments.SettingsFragment
import vlados.dudos.gachiclicker.common.ui.fragments.ShopFragment
import vlados.dudos.gachiclicker.common.ui.models.Event
import vlados.dudos.gachiclicker.databinding.ActivityGameBinding
import java.lang.Exception
import java.util.concurrent.TimeUnit

class GameActivity : AppCompatActivity() {

    private lateinit var b: ActivityGameBinding
    private lateinit var thread: Disposable
    private lateinit var mediaPlayer: MediaPlayer
    private val fireBaseFS = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityGameBinding.inflate(layoutInflater)
        setContentView(b.root)

        getUserCum(App.dm.getUserMail())
        updateShop()

        fragmentTransaction(GameFragment())

        b.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.click -> {
                    fragmentTransaction(GameFragment())
                    changeInfoVisibility(View.VISIBLE)
                }

                R.id.shop -> {
                    fragmentTransaction(ShopFragment())
                    changeInfoVisibility(View.VISIBLE)
                }
                R.id.events -> {
                    updateEvents()
                    fragmentTransaction(EventsFragment())
                    changeInfoVisibility(View.GONE)
                }
                R.id.settings -> {
                    fragmentTransaction(SettingsFragment())
                    changeInfoVisibility(View.GONE)
                }

            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer = MediaPlayer.create(this, R.raw.minecraft_rv)
        mediaPlayer.setVolume(0.07f, 0.07f)
        updateDate()
        cpsThread()
    }

    override fun onStop() {
        super.onStop()
        saveData()
        mediaPlayer.pause()
        thread.dispose()
    }

    private fun fragmentTransaction(fmt: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, fmt).addToBackStack(null)
            .commit()
    }
    fun updateDate(){
        b.yourCum.text = getString(R.string.current_cum) + cutNum(currentCum)
        b.textCps.text = getString(R.string.cps) + cumPerSecond
        b.yourCPC.text = getString(R.string.current_cpc) + cumPerClick
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (b.bottomNavigation.selectedItemId != R.id.click){
            b.bottomNavigation.selectedItemId = R.id.click
            fragmentTransaction(GameFragment())
        }
        else finishAffinity()
    }
    private fun cpsThread(){
        thread = Observable
            .interval(1, TimeUnit.SECONDS)
            .doOnNext {
                cpsSaving()
                musicPlaying()
            }
            .subscribe()
    }
    private fun cpsSaving(){
        clicks = 13
        updateCurrentCum(cumPerSecond)
        updateDate()
    }
    private fun changeInfoVisibility(int: Int)
    {
        b.constraintInfo.visibility = int
    }
    private fun musicPlaying(){
        if (!mediaPlayer.isPlaying){
            mediaPlayer.start()
        }
    }
    private fun getUserCum(mail: String){
        fireBaseFS.collection("Users").document("user:$mail")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val userData = task.result?.data
                    cumPerClick = userData?.get("cpc").toString().toInt()
                    cumPerSecond = userData?.get("cps").toString().toLong()
                    currentCum = userData?.get("currentCum").toString().toLong()
                    shopMaxLevel = try {
                        userData?.get("maxShopLevel").toString().toInt()
                    } catch (e: Exception){
                        5
                    }
                } else Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }
    private fun updateEvents() {
        FirebaseFirestore.getInstance()
            .collection("Events")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (i in task.result.documents) {
                        Case.listEvents.add(
                            Event(
                                i.data?.get("id").toString().toInt(),
                                i.data?.get("nameEvent").toString(),
                                i.data?.get("description").toString(),
                                i.data?.get("img").toString(),
                                checkString(i.data?.get("prizeName").toString())
                            )
                        )
                    }
                }
            }
    }

    private fun checkString(str: String): String {
        return str.replace("\\n", "\n")
    }
}


