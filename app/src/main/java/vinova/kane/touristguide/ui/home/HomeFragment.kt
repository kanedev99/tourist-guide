package vinova.kane.touristguide.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.item_tab_place.view.*
import vinova.kane.touristguide.R
import vinova.kane.touristguide.adapter.DISTANCE
import vinova.kane.touristguide.adapter.POPULAR
import vinova.kane.touristguide.adapter.PlacePagerAdapter
import vinova.kane.touristguide.adapter.TYPE
import vinova.kane.touristguide.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        bindEvents()

        return binding.root
    }

    private fun bindEvents() {
        with(binding) {
            vpPlaces.adapter = PlacePagerAdapter(this@HomeFragment)
            vpPlaces.offscreenPageLimit = 2

            TabLayoutMediator(placesTab, vpPlaces) { tab, position ->
                tab.text = getTabTitle(position)
                tab.customView = getTabCustom(position)
            }.attach()

            placesTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    (tab?.customView as View).tvPlaceTabTitle
                        .setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.white)
                        )
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    (tab?.customView as View).tvPlaceTabTitle
                        .setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.new_brand_green)
                        )
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            POPULAR -> "Popular"
            DISTANCE -> "Distance"
            TYPE -> "Categories"
            else -> null
        }
    }

    private fun getTabCustom(position: Int): View? {
        val tabItem =
            LayoutInflater.from(requireActivity()).inflate(R.layout.item_tab_place, null)
        val tvTitle =
            tabItem.findViewById<AppCompatTextView>(R.id.tvPlaceTabTitle)
        tvTitle.text = getTabTitle(position)
        if (position != POPULAR) {
            tvTitle.setTextColor(
                ContextCompat.getColor(requireActivity(), R.color.new_brand_green)
            )
        }
        return tabItem
    }

}