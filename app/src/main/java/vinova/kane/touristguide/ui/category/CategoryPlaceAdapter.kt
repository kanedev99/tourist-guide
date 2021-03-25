package vinova.kane.touristguide.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vinova.kane.touristguide.databinding.ItemCategoryPlaceBinding
import vinova.kane.touristguide.vo.Place

class CategoryPlaceAdapter(
    val placeClickCallback: (Place) -> Unit
): ListAdapter<Place, CategoryPlaceAdapter.CategoryPlaceViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryPlaceViewHolder {
        val itemBinding = ItemCategoryPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryPlaceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holderCategory: CategoryPlaceViewHolder, position: Int) {
        holderCategory.bind(getItem(position))
    }

    inner class CategoryPlaceViewHolder(private val itemBinding: ItemCategoryPlaceBinding): RecyclerView.ViewHolder(itemBinding.root){
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
