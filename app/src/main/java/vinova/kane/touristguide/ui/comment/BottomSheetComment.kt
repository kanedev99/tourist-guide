package vinova.kane.touristguide.ui.comment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import vinova.kane.touristguide.R
import vinova.kane.touristguide.utils.Args
import vinova.kane.touristguide.utils.SaveSharedPreference
import vinova.kane.touristguide.vo.Comment
import java.util.*


class BottomSheetComment: BottomSheetDialogFragment() {
    private lateinit var ivAvatar: CircleImageView
    private lateinit var edtComment: AppCompatEditText
    private lateinit var ivSendComment: AppCompatImageView

    private var placeId = ""
    private var comment = Comment()

    private lateinit var auth: FirebaseAuth
    private lateinit var commentsReference: DatabaseReference

    companion object {
        fun newInstance(bundle: Bundle?): BottomSheetComment {
            val fragment = BottomSheetComment()
            if (bundle != null) {
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        placeId = arguments?.getString(Args.PLACE_ID) ?: ""

        auth = FirebaseAuth.getInstance()
        commentsReference = FirebaseDatabase.getInstance().getReference("comments").child(placeId)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.bottom_sheet_comment, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(
            resources.getColor(
                android.R.color.transparent,
                null
            )
        )

        ivAvatar = contentView.findViewById(R.id.ivAvatarUser)
        edtComment = contentView.findViewById(R.id.edtComment)
        ivSendComment = contentView.findViewById(R.id.ivSendComment)

        initView()

        setupEvent()
    }

    private fun setupEvent(){
        ivSendComment.setOnClickListener {
            val commentContent = edtComment.text.toString()
            if (commentContent.isNotEmpty()){
                comment.userName = SaveSharedPreference().getUsername(requireContext()).toString()
                comment.commentContent = commentContent
                comment.imgUrl = SaveSharedPreference().getImageUri(requireContext()).toString()
                commentsReference.child(UUID.randomUUID().toString()).setValue(comment)
                edtComment.setText("")

                dialog?.dismiss()
            }
        }
    }

    private fun initView(){
        val imgUri = SaveSharedPreference().getImageUri(requireContext())
        if (imgUri?.isNotEmpty()!!){
            Glide.with(requireContext())
                .load(imgUri)
                .into(ivAvatar)
        } else {
            ivAvatar.setImageResource(R.drawable.avatar_default)
        }
    }
}