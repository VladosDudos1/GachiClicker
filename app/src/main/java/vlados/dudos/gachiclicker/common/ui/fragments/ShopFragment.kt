package vlados.dudos.gachiclicker.common.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import vlados.dudos.gachiclicker.common.ui.activity.GameActivity
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.app.App
import vlados.dudos.gachiclicker.common.Case.shopCoef
import vlados.dudos.gachiclicker.common.Case.shopList
import vlados.dudos.gachiclicker.common.ui.adapters.ShopAdapter
import vlados.dudos.gachiclicker.databinding.FragmentShopBinding

class ShopFragment : Fragment(), ShopAdapter.OnClickListener {

    override fun click(data: Int, level: Int) {
        updateShopItem(data, level)
    }

    private lateinit var b: FragmentShopBinding
    private val fb = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentShopBinding.bind(
            inflater.inflate(
                R.layout.fragment_shop, container, false
            )
        )
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.shopRv.adapter = ShopAdapter(requireContext(), shopList, this)
        b.shopRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun updateShopItem(position: Int, level: Int) {
        fb.collection("Users").document("user:${App.dm.getUserMail()}")
            .update("si$position", (level + 1))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    shopList[position].level++
                    shopList[position].price = (shopList[position].price * shopCoef).toLong()


                    (activity as GameActivity).updateDate()
                    b.shopRv.adapter!!.notifyItemChanged(position)
                }
            }
    }
}