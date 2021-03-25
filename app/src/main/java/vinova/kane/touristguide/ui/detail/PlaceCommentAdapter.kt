package vinova.kane.touristguide.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.ItemCommentBinding
import vinova.kane.touristguide.vo.Comment


class PlaceCommentAdapter(options: FirebaseRecyclerOptions<Comment>):
    FirebaseRecyclerAdapter<Comment, PlaceCommentAdapter.PlaceCommentViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceCommentViewHolder {
        val withDataBinding: ItemCommentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_comment,
            parent,
            false
        )
        return PlaceCommentViewHolder(withDataBinding)
    }

    inner class PlaceCommentViewHolder(private val itemBinding: ItemCommentBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun onBind(item: Comment){
            itemBinding.comment = item

            if (item.imgUrl.isNotBlank()){
                Glide.with(itemBinding.root)
                    .load(item.imgUrl)
                    .into(itemBinding.ivAvatarUser)
            } else {
                itemBinding.ivAvatarUser.setImageResource(R.drawable.avatar_default)
            }

        }
    }

    override fun onBindViewHolder(holder: PlaceCommentViewHolder, position: Int, model: Comment) {
        holder.onBind(model)
    }

}