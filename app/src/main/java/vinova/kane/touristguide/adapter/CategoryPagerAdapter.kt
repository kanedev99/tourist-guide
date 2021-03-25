package vinova.kane.touristguide.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CategoryPagerAdapter(
    fm: FragmentManager,
    private val fragmentList: ArrayList<Fragment>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var currentFragment: Fragment? = null

    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun setPrimaryItem(container: ViewGroup, position: Int, any: Any) {
        if (currentFragment !== any) {
            currentFragment = any as Fragment
        }
        super.setPrimaryItem(container, position, any)
    }
}