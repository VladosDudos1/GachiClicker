package vlados.dudos.gachiclicker.common.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.ui.adapters.EventsAdapter
import vlados.dudos.gachiclicker.common.ui.models.Event
import vlados.dudos.gachiclicker.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {

    private lateinit var b: FragmentEventsBinding
    private var listEvents = mutableListOf<Event>()

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
        updateEvents()
    }

    private fun updateEvents() {
        FirebaseFirestore.getInstance()
            .collection("Events")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (i in task.result.documents) {
                        listEvents.add(
                            Event(
                                i.data?.get("id").toString().toInt(),
                                i.data?.get("nameEvent").toString(),
                                i.data?.get("description").toString(),
                                i.data?.get("img").toString(),
                                checkString(i.data?.get("prizeName").toString())
                            )
                        )
                    }

                    b.rvEvents.adapter = EventsAdapter(requireContext(), listEvents)
                } else Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkString(str: String): String {
        return str.replace("\\n", "\n")
    }
}