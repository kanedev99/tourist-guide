package vinova.kane.touristguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import vinova.kane.touristguide.R
import vinova.kane.touristguide.ext.setImageUrl
import vinova.kane.touristguide.vo.Photo

class SlidePagerAdapter(private val imageList: List<Photo>) : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int = imageList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageLayout =
            LayoutInflater
                .from(container.context)
                .inflate(R.layout.item_slide_image, container, false)

        val image = imageLayout.findViewById<ImageView>(R.id.ivImageSlide)
        image.setImageUrl(imageList[position].imgUrl)
        container.addView(imageLayout, 0)
        return imageLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}