package vinova.kane.touristguide.ui.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.touristguide.databinding.ItemPopularPlaceBinding
import vinova.kane.touristguide.vo.Place


class PopularPlaceAdapter(
    val placeClickCallback: (Place) -> Unit
): ListAdapter<Place, PopularPlaceAdapter.PopularPlaceViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularPlaceViewHolder {
        val itemBinding = ItemPopularPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularPlaceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holderPopular: PopularPlaceViewHolder, position: Int) {
        holderPopular.bind(getItem(position))
    }

    inner class PopularPlaceViewHolder(private val itemBinding: ItemPopularPlaceBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bind(place: Place){
            itemBinding.place = place
            itemBinding.root.setOnClickListener{
                place.let { placeClickCallback.invoke(it) }
            }
        }
    }

    class DiffCallBack: DiffUtil.ItemCallback<Place>(){
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }

    }
}
