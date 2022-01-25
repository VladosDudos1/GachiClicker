package vlados.dudos.gachiclicker.common.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.ui.adapters.EventsAdapter
import vlados.dudos.gachiclicker.common.ui.models.Event
import vlados.dudos.gachiclicker.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {

    private lateinit var b: FragmentEventsBinding
    private var listEvents = listOf(
        Event(
            0,
            "Rikardo fight",
            "Are you ready to smell his underpants?",
            "https://i0.wp.com/cdn140.picsart.com/300840060233211.png",
            "Win: +10 Cum /click\nLose: -10 Cum /click",
            "https://steamuserimages-a.akamaihd.net/ugc/949596928134142092/4C0E46016EE7ABF8440FCA7B9B5AB60EF55AA969/"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentEventsBinding.bind(
            inflater.inflate(
                R.layout.fragment_events, container, false
            )
        )
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.rvEvents.adapter = EventsAdapter(requireContext(), listEvents)
    }
}