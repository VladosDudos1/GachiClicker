package vlados.dudos.gachiclicker.common

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import vlados.dudos.gachiclicker.app.App
import vlados.dudos.gachiclicker.common.ui.models.Boss
import vlados.dudos.gachiclicker.common.ui.models.ShopItem
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

object Case {

    private val letterList = listOf("", "K", "M", "B", "T", "q", "Q")

    lateinit var bossImage: String
    var clicks: Int = 13

    var currentCum: Long = 0
    var cumPerClick: Int = 1
    var cumPerSecond: Long = 0

    var shopList = mutableListOf<ShopItem>()

    const val shopCoef: Double = 1.2

    //Bosses
    var bossRikardo = Boss(1000, 10, true, 300)

    fun updateCPS(num: Int) {
        if (cumPerSecond + num >= 0)
            cumPerSecond += num
        else cumPerSecond = 0
    }

    fun updateCPC(num: Int) {
        if (cumPerClick + num >= 1)
            cumPerClick += num
        else cumPerClick = 1
    }

    fun updateCurrentCum(num: Long) {
        currentCum += num
    }


    fun saveData() {
        updateUserField("cps", cumPerSecond.toString())
        updateUserField("cpc", cumPerClick.toString())
        updateUserField("currentCum", currentCum.toString())
    }

    fun cutNum(num: Long): String {
        val specialIndex = num.toString().length / 3 - if (num.toString().length % 3 == 0) 1 else 0
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.HALF_DOWN
        return df.format(num / 1000.0.pow(specialIndex)) + letterList[specialIndex]
    }

    private fun updateUserField(key: String, value: String) {
        val fb = FirebaseFirestore.getInstance()
        fb.collection("Users").document("user:${App.dm.getUserMail()}")
            .update(key, value)
    }

    fun updateShop() {
        val fb = FirebaseFirestore.getInstance()
        val user = fb.collection("Users").document("user:${App.dm.getUserMail()}")
        var level = 0
        fb.collection("Shop")
            .get()
            .addOnCompleteListener { task ->
                for ((iteration, i) in task.result.documents.withIndex()) {
                    user
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                level = task.result.data?.get("si$iteration").toString().toInt()
                                shopList.add(
                                    ShopItem(
                                        i["img"].toString(),
                                        i["nameItem"].toString(),
                                        i["description"].toString(),
                                        i["cpsBuff"].toString().toInt(),
                                        (i["price"].toString()
                                            .toLong() * shopCoef.pow(level)).toLong(),
                                        level
                                    )
                                )
                            }
                        }
                }
            }
    }
}