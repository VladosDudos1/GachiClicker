package vlados.dudos.gachiclicker.common.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.Case.bossImage
import vlados.dudos.gachiclicker.common.ui.activity.BossActivity
import vlados.dudos.gachiclicker.common.ui.models.Event
import vlados.dudos.gachiclicker.databinding.EventViewBinding

class EventsAdapter(private var context: Context, private var list: List<Event>) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    lateinit var b: EventViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.event_view, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        b = EventViewBinding.bind(holder.itemView)

        b.nameEventTxt.text = list[position].nameEvent
        b.descriptionEventTxt.text = list[position].description
        b.prizeEventTxt.text = list[position].prizeName

        Glide.with(context)
            .load(list[position].img)
            .error(R.drawable.block)
            .into(b.imgEvent)

        b.layoutEvent.setOnClickListener {
            when(position){
                0 -> context.startActivity(Intent(context, BossActivity::class.java))
            }
            bossImage = list[position].bossImage
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}