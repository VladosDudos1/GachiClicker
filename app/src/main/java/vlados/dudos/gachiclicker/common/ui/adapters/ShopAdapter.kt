package vlados.dudos.gachiclicker.common.ui.adapters

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.Case.currentCum
import vlados.dudos.gachiclicker.common.Case.cutNum
import vlados.dudos.gachiclicker.common.Case.shopMaxLevel
import vlados.dudos.gachiclicker.common.Case.updateCPC
import vlados.dudos.gachiclicker.common.Case.updateCPS
import vlados.dudos.gachiclicker.common.Case.updateCurrentCum
import vlados.dudos.gachiclicker.common.ui.models.ShopItem
import vlados.dudos.gachiclicker.databinding.ShopItemBinding

class ShopAdapter(
    private val context: Context,
    private val shopList: List<ShopItem>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    lateinit var b: ShopItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.shop_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        b = ShopItemBinding.bind(holder.itemView)

        if (position == 2)
            b.imgShopItem.setColorFilter(R.color.transparent)

        Glide.with(b.imgShopItem)
            .load(shopList[position].img)
            .error(R.drawable.block)
            .into(b.imgShopItem)

        b.txtNameShopItem.text = shopList[position].nameItem
        b.txtDescShopItem.text = shopList[position].description
        b.priceTxt.text = "${cutNum(shopList[position].price)} cum"
        b.txtLevel.text = "${shopList[position].level} куплено"

        b.buyTxt.setOnClickListener {
            if (shopMaxLevel <= shopList[position].level && position != 0) {
                Toast.makeText(
                    context,
                    "Убейте Billy, чтобы увеличить максимальный уровень магазина",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (currentCum >= shopList[position].price) {
                onClickListener.click(position, shopList[position].level)
                when (position) {
                    0 -> {
                        updateCPC(1)
                    }
                    else -> updateCPS(shopList[position].cpsBuff)
                }
                updateCurrentCum(-shopList[position].price)
            } else Toast.makeText(context, "У Fucking Slave не хватает cum", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun getItemCount(): Int = shopList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnClickListener {
        fun click(data: Int, level: Int)
    }
}