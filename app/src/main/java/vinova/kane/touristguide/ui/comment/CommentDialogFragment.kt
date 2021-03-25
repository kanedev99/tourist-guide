package vinova.kane.touristguide.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentCommentDialogBinding
import vinova.kane.touristguide.ui.detail.PlaceCommentAdapter
import vinova.kane.touristguide.utils.Args
import vinova.kane.touristguide.vo.Comment

class CommentDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCommentDialogBinding

    private lateinit var commentAdapter: PlaceCommentAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var commentsReference: DatabaseReference

    private var placeId = ""

    companion object {
        fun newInstance(bundle: Bundle?): CommentDialogFragment {
            val fragment = CommentDialogFragment()
            if (bundle != null) {
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar)

        placeId = arguments?.getString(Args.PLACE_ID) ?: ""

        auth = FirebaseAuth.getInstance()
        commentsReference = FirebaseDatabase.getInstance().getReference("comments").child(placeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentDialogBinding.inflate(inflater)

        initEvent()

        fetch()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        commentAdapter.startListening()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    private fun initEvent(){
        binding.tvCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.tvAdd.setOnClickListener {
            val bundle = bundleOf(Args.PLACE_ID to placeId)
            BottomSheetComment.newInstance(bundle).show(parentFragmentManager, "COMMENT_DIALOG")
        }
    }

    private fun fetch() {
        val options: FirebaseRecyclerOptions<Comment> = FirebaseRecyclerOptions.Builder<Comment>()
            .setQuery(commentsReference, Comment::class.java)
            .build()

        commentAdapter = PlaceCommentAdapter(options)
        binding.rvListComments.adapter = commentAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        commentAdapter.stopListening()
    }

}