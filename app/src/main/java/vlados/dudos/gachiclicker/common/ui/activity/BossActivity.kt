package vlados.dudos.gachiclicker.common.ui.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.Case.bossImage
import vlados.dudos.gachiclicker.common.Case.bossRikardo
import vlados.dudos.gachiclicker.common.Case.updateCPC
import vlados.dudos.gachiclicker.common.Case.updateCPS
import vlados.dudos.gachiclicker.common.ui.models.Boss
import vlados.dudos.gachiclicker.databinding.ActivityBossBinding
import kotlin.random.Random.Default.nextInt


class BossActivity : AppCompatActivity() {

    private lateinit var b: ActivityBossBinding
    private lateinit var timer: CountDownTimer
    private var isWin = -1
    private var boss: Boss = Boss(100000000, -1,false,-1)
    private lateinit var dialog: AlertDialog
    private lateinit var str: String
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityBossBinding.inflate(layoutInflater)
        setContentView(b.root)
        startBossFight()
        onClick()
        startTimer()
        startMusic()
    }

    private fun startBossFight() {
        updateProgress()
        uploadBoss()
    }

    private fun startMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.you_got_that)
        mediaPlayer.setVolume(1f, 1f)
        mediaPlayer.start()
    }

    private fun uploadBoss() {
        Glide.with(this)
            .load(bossImage)
            .into(b.bossImage)
        str = if (boss.isCpc) "click" else "sec"
    }

    private fun onClick() {
        b.bossImage.setOnClickListener {
            when (nextInt(4)) {
                0 -> {}
                else -> {
                    boss.bossHP -= 1
                    b.progressBar.progress-=1
                }
            }

            b.txtHp.text = "${boss.bossHP} / ${bossRikardo.bossHP}"
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
                dialog = AlertDialog.Builder(this@BossActivity)
                    .setTitle("Ohh shit... I`m sorry...")
                    .setMessage("Your Cum /$str reduced by ${boss.prizeAmount}!")
                    .setPositiveButton("FUCK!") { dialog, which ->
                        dialog.dismiss()
                        onBackPressed()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
        timer.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        bossResult()
        updateProgress()
        mediaPlayer.stop()
    }
    private fun updateProgress(){
        loadBoss(bossRikardo)
        b.progressBar.max = boss.bossHP
        b.progressBar.progress = boss.bossHP
        b.txtHp.text = "${boss.bossHP} / ${bossRikardo.bossHP}"
    }
    private fun loadBoss(bigBoss: Boss){
        boss.bossHP = bigBoss.bossHP
        boss.prizeAmount = bigBoss.prizeAmount
        boss.isCpc = bigBoss.isCpc
        boss.timeSec = bigBoss.timeSec
    }
    private fun bossResult(){
        if (boss.isCpc)
            updateCPC(isWin * boss.prizeAmount)
        else updateCPS(isWin * boss.prizeAmount)
    }
}