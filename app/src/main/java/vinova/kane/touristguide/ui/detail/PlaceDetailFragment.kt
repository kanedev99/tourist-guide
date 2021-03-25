package vinova.kane.touristguide.ui.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vinova.kane.touristguide.R
import vinova.kane.touristguide.adapter.SlidePagerAdapter
import vinova.kane.touristguide.databinding.FragmentPlaceDetailBinding
import vinova.kane.touristguide.ext.View.hide
import vinova.kane.touristguide.ext.View.show
import vinova.kane.touristguide.firebase.FirebaseViewModel
import vinova.kane.touristguide.ui.comment.CommentDialogFragment
import vinova.kane.touristguide.ui.home.PlaceViewModel
import vinova.kane.touristguide.utils.Args
import vinova.kane.touristguide.vo.*
import java.util.*


class PlaceDetailFragment : Fragment() {

    private lateinit var binding: FragmentPlaceDetailBinding

    private val placeViewModel: PlaceViewModel by viewModel()
    private val firebaseViewModel: FirebaseViewModel by viewModel()
    private lateinit var userLiveData: LiveData<DataSnapshot>

    private lateinit var auth: FirebaseAuth
    private lateinit var commentsReference: DatabaseReference

    private var expandDescription = false
    private var placeId = ""
    private var user: User? = User()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        placeId = arguments?.getString(Args.PLACE_ID) ?: ""
        placeViewModel.getDetailPlace(placeId)

        auth = FirebaseAuth.getInstance()
        commentsReference = FirebaseDatabase.getInstance().getReference("comments").child(placeId)

        userLiveData = firebaseViewModel.getUserLiveData()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlaceDetailBinding.inflate(inflater)

        binding.fragment = this

        return binding.root
    }

    private fun initView(images: List<Image>) {
        with(binding) {
            if (images.size > 1) {
                loadImageSlider(images)
                viewPager.show()
                tvSlideIndex.show()
                ivCoverPhoto.hide()
            } else {
                viewPager.hide()
                tvSlideIndex.hide()
                ivCoverPhoto.show()
            }

        }
    }

    private fun setupObserver() {
        placeViewModel.detailPlace.observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data.let { place ->
                            binding.place = place
                            place?.images?.let { listImages ->
                                initView(listImages)
                            }
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
            }
        })

        userLiveData.observe(viewLifecycleOwner,
            Observer<DataSnapshot> { dataSnapShot ->
                user = dataSnapShot.child(auth.uid!!).getValue(User::class.java)
            })
    }

    fun onClick(view: View) {
        view.startAnimation(
            AnimationUtils.loadAnimation(
                activity,
                R.anim.image_view_click
            )
        )

        with(binding) {
            when (view) {
                ivBack -> findNavController().popBackStack()

                llDescription -> {
                    expandDescription = !expandDescription
                    showDescription(expandDescription)
                }

                tvWikipedia -> binding.place?.attribution?.let {
                    val success = loadWikiWebView(it)
                    if (!success) {
                        Toast.makeText(
                            requireContext(),
                            "No information about this place in Wikipedia",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                fabMap -> {
                    binding.place?.coordinates?.let { loadMapWebView(it) }
                }

                tvFeedback -> {
                    val bundle = bundleOf(Args.PLACE_ID to placeId)
                    CommentDialogFragment.newInstance(bundle).show(parentFragmentManager, "COMMENT_DIALOG")
                }

                else -> {
                }
            }
        }
    }

    private fun showDescription(show: Boolean) {
        with(binding) {
            if (show) {
                tvDescription.show()
                tvDescriptionShow.show()
                tvDescriptionHide.hide()
            } else {
                tvDescription.hide()
                tvDescriptionShow.hide()
                tvDescriptionHide.show()
            }
        }
    }

    private fun loadImageSlider(images: List<Image>) {
        with(binding) {
            val slidePagerAdapter = SlidePagerAdapter(getListPhotos(images))
            viewPager.offscreenPageLimit = 1
            viewPager.adapter = slidePagerAdapter

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(
                    position: Int, positionOffset: Float, positionOffsetPixels: Int
                ) {}

                override fun onPageSelected(position: Int) {
                    tvSlideIndex.text = getString(R.string.slide_index, position + 1, images.size)
                }
            })
            tvSlideIndex.text = getString(R.string.slide_index_init, images.size)
        }
    }

    private fun getListPhotos(images: List<Image>): List<Photo> {
        val listPhotos: ArrayList<Photo> = arrayListOf()
        for (image in images) {
            val index = 0
            val photo = Photo(index, image.sizes.medium.url)
            listPhotos.add(photo)
        }
        return listPhotos
    }

    private fun loadWikiWebView(attrList: List<Attribution>): Boolean {
        if (!attrList.isNullOrEmpty()) {
            for (attribution in attrList) {
                if (attribution.sourceId == "wikipedia") {
                    val builder = CustomTabsIntent.Builder()
                    val customTabsIntent = builder.build()
                    builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
                    customTabsIntent.launchUrl(requireContext(), Uri.parse(attribution.url))
                    return true
                }
            }
            return false
        }
        return false
    }

    private fun loadMapWebView(coordinates: Coordinates) {
        val geo = "${coordinates.latitude},${coordinates.longitude}"
        val gmmIntentUri = Uri.parse("geo:$geo?q=$geo(${binding.place?.name})")
        Timber.d(geo)
        Timber.d(binding.place?.name)

        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        activity?.packageManager?.let {
            try {
                startActivity(mapIntent)
            } catch (ex: ActivityNotFoundException) {
                Timber.e(ex)
            }
        }
    }

}