package vlados.dudos.gachiclicker.common.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import vlados.dudos.gachiclicker.R
import vlados.dudos.gachiclicker.common.Case.listEvents
import vlados.dudos.gachiclicker.common.ui.adapters.EventsAdapter
import vlados.dudos.gachiclicker.databinding.FragmentEventsBinding
import kotlin.math.abs

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

        startViewPager()
    }

    private fun startViewPager() {
        Handler().postDelayed({
            b.progressBar.visibility = View.GONE
            b.rvEvents.visibility = View.VISIBLE
            b.rvEvents.adapter = EventsAdapter(requireContext(), listEvents)
        }, 500)
        viewPagerTransform()
    }

    private fun viewPagerTransform() {
        b.rvEvents.apply {
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPadding(
                dpFromPx(context, 26f).toInt(),
                0,
                dpFromPx(context, 26f).toInt(),
                0
            )
            setPageTransformer { page, position ->
                page.scaleY = 1 - (0.06f * abs(position))
            }
        }
    }

    private fun dpFromPx(context: Context, px: Float): Float {
        return px * context.resources.displayMetrics.density
    }

    override fun onStop() {
        super.onStop()
        listEvents.clear()
    }
}