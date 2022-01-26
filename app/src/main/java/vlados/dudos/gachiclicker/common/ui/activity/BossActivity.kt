package vlados.dudos.gachiclicker.common.ui.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.Case.boss
import vlados.dudos.gachiclicker.common.Case.bossDockPath
import vlados.dudos.gachiclicker.common.Case.updateCPC
import vlados.dudos.gachiclicker.common.Case.updateCPS
import vlados.dudos.gachiclicker.common.ui.models.Boss
import vlados.dudos.gachiclicker.databinding.ActivityBossBinding
import kotlin.random.Random.Default.nextInt


class BossActivity : AppCompatActivity() {

    private lateinit var b: ActivityBossBinding
    private lateinit var timer: CountDownTimer
    private var isWin = -1
    var bossFB = FirebaseFirestore.getInstance().collection("Bosses").document("boss:$bossDockPath")
    private var maxHP = 0
    private lateinit var dialog: AlertDialog
    private lateinit var str: String
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityBossBinding.inflate(layoutInflater)
        setContentView(b.root)
        startBossFight()
        onClick()
        startMusic()
    }

    private fun startBossFight() {
        uploadBoss()
    }

    private fun startMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.you_got_that)
        mediaPlayer.setVolume(1f, 1f)
        mediaPlayer.start()
    }

    private fun uploadBoss() {
        bossFB
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    boss.bossImg = task.result.data?.get("bossImg").toString()
                    boss.bossHP = task.result.data?.get("bossHP").toString().toInt()
                    maxHP = task.result.data?.get("bossHP").toString().toInt()
                    boss.isCpc = task.result.data?.get("isCpc").toString().toBoolean()
                    boss.prizeAmount = task.result.data?.get("prizeAmout").toString().toInt()
                    boss.timeSec = task.result.data?.get("timeSec").toString().toInt()

                    Glide.with(this)
                        .load(boss.bossImg)
                        .into(b.bossImage)
                    str = if (boss.isCpc) "click" else "sec"

                    updateProgress()

                    startTimer()
                }
            }
    }

    private fun onClick() {
        b.bossImage.setOnClickListener {
            when (nextInt(4)) {
                0 -> {}
                else -> {
                    boss.bossHP -= 1
                    b.progressBar.progress -= 1
                }
            }

            b.txtHp.text = "${boss.bossHP} / ${maxHP}"
            if (boss.bossHP == 0) {
                isWin = 1
                timer.cancel()

                dialog = AlertDialog.Builder(this@BossActivity)
                    .setTitle("Let`s celebrate your victory!")
                    .setMessage("Your Cum /$str increased by ${boss.prizeAmount}!")
                    .setPositiveButton("Ok") { dialog, which ->
                        dialog.dismiss()
                        onBackPressed()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(boss.timeSec * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                b.timerText.text = ("Seconds Remaining: " + millisUntilFinished / 1000)
                if (!mediaPlayer.isPlaying)
                    mediaPlayer.start()
                if (!mediaPlayer.isPlaying)
                    mediaPlayer.start()
            }

            override fun onFinish() {
                onBackPressed()
            }
        }
        timer.start()
    }

    override fun onBackPressed() {
        bossResult()
        updateProgress()
        mediaPlayer.stop()
        if (isWin == -1) {
            dialog = AlertDialog.Builder(this@BossActivity)
                .setTitle("Ohh shit... I`m sorry...")
                .setMessage("Your Cum /$str reduced by ${boss.prizeAmount}!")
                .setPositiveButton("FUCK!") { dialog, which ->
                    dialog.dismiss()
                    super.onBackPressed()
                }
                .setCancelable(false)
                .show()
        }
        else super.onBackPressed()
    }

    private fun updateProgress() {
        loadBoss(boss)
        b.progressBar.max = boss.bossHP
        b.progressBar.progress = boss.bossHP
        b.txtHp.text = "${boss.bossHP} / $maxHP"
    }

    private fun loadBoss(bigBoss: Boss) {
        boss.bossHP = bigBoss.bossHP
        boss.prizeAmount = bigBoss.prizeAmount
        boss.isCpc = bigBoss.isCpc
        boss.timeSec = bigBoss.timeSec
    }

    private fun bossResult() {
        if (boss.isCpc)
            updateCPC(isWin * boss.prizeAmount)
        else updateCPS(isWin * boss.prizeAmount)
    }
}