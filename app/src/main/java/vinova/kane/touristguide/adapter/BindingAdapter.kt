package vinova.kane.touristguide.adapter

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import vinova.kane.touristguide.R
import vinova.kane.touristguide.ext.Any.round
import vinova.kane.touristguide.ext.View.show
import vinova.kane.touristguide.vo.Image


@BindingAdapter("app:loadImage")
fun loadImage(view: AppCompatImageView, image: Image?) {
    if (image != null) {
        Glide.with(view.context)
            .load(image.sizes.original.url)
            .thumbnail(
                Glide.with(view.context)
                    .load(image.sizes.thumbnail.url)
            )
            .into(view)
        view.show()
    } else {
        view.setImageDrawable(
            ResourcesCompat.getDrawable(
                view.resources,
                R.drawable.no_photo,
                null
            )
        )
    }
}

@BindingAdapter("app:loadScore")
fun loadScore(view: TextView, score: Float) {
    view.text = score.round()
}

@BindingAdapter("app:loadDistance")
fun loadDistance(view: TextView, distance: Int) {
    view.text = "$distance m"
}
