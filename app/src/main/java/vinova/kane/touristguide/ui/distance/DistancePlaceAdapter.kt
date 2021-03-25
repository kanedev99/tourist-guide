package vinova.kane.touristguide.ui.distance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.touristguide.databinding.ItemDistancePlaceBinding
import vinova.kane.touristguide.vo.Place

class DistancePlaceAdapter(
    val placeClickCallback: (Place) -> Unit
): ListAdapter<Place, DistancePlaceAdapter.DistancePlaceViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistancePlaceViewHolder {
        val itemBinding = ItemDistancePlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DistancePlaceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holderDistance: DistancePlaceViewHolder, position: Int) {
        holderDistance.bind(getItem(position))
    }

    inner class DistancePlaceViewHolder(private val itemBinding: ItemDistancePlaceBinding): RecyclerView.ViewHolder(itemBinding.root){
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
