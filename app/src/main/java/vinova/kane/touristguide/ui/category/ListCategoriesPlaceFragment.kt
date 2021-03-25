package vinova.kane.touristguide.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentTypePlaceBinding
import vinova.kane.touristguide.utils.Query

class ListCategoriesPlaceFragment : Fragment() {

    private lateinit var binding: FragmentTypePlaceBinding

    companion object {
        fun newInstance() =
            ListCategoriesPlaceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTypePlaceBinding.inflate(inflater)

        binding.fragment = this

        return binding.root
    }

    fun initEvent(view: View){
        when (view) {
            binding.cvSightSeeing -> navigateToCategoryPlace(Query.SIGHT_SEEING, getString(R.string.sight_seeing))
            binding.cvEatAndDrink -> navigateToCategoryPlace(Query.EAT_AND_DRINK, getString(R.string.eat_and_drink))
            binding.cvNightLife -> navigateToCategoryPlace(Query.NIGHT_LIFE, getString(R.string.nightlife))
            binding.cvHotel -> navigateToCategoryPlace(Query.HOTEL, getString(R.string.hotel))
            binding.cvShopping -> navigateToCategoryPlace(Query.SHOPPING, getString(R.string.shopping))
            binding.cvActivity -> navigateToCategoryPlace(Query.ACTIVITY, getString(R.string.activity))
            binding.cvAirport -> navigateToCategoryPlace(Query.AIRPORT, getString(R.string.airport))
            binding.cvBank -> navigateToCategoryPlace(Query.BANK, getString(R.string.bank))
        }
    }

    private fun navigateToCategoryPlace(label: String, title: String){
        val bundle = bundleOf(Pair("TAG_LABEL", label), Pair("CATEGORY_TITLE", title))
        findNavController().navigate(R.id.action_home_to_category_place, bundle)
    }

}