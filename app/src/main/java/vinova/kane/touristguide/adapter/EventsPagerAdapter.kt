package vinova.kane.touristguide.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import vinova.kane.touristguide.ui.distance.DistancePlaceFragment
import vinova.kane.touristguide.ui.popular.PopularPlaceFragment
import vinova.kane.touristguide.ui.category.ListCategoriesPlaceFragment

const val POPULAR = 0
const val DISTANCE = 1
const val TYPE = 2

class PlacePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        POPULAR to { PopularPlaceFragment.newInstance() },
        DISTANCE to { DistancePlaceFragment.newInstance() },
        TYPE to { ListCategoriesPlaceFragment.newInstance() }
    )

    override fun getItemCount(): Int = tabFragmentCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
