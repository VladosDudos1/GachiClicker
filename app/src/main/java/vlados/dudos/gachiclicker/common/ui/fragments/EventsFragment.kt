package vlados.dudos.gachiclicker.common.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.Case.listEvents
import vlados.dudos.gachiclicker.common.ui.adapters.EventsAdapter
import vlados.dudos.gachiclicker.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {

    private lateinit var b: FragmentEventsBinding

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

        Handler().postDelayed({
            b.progressBar.visibility = View.GONE
            b.rvEvents.visibility = View.VISIBLE
            b.rvEvents.adapter = EventsAdapter(requireContext(), listEvents)
        }, 500)

    }
}