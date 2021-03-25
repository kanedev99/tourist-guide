package vinova.kane.touristguide.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentCategoryBinding
import vinova.kane.touristguide.ext.View.hide
import vinova.kane.touristguide.ext.View.show
import vinova.kane.touristguide.ui.home.PlaceViewModel
import vinova.kane.touristguide.utils.Args
import vinova.kane.touristguide.vo.Status

class CategoryPlaceFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding

    private lateinit var adapter: CategoryPlaceAdapter

    private val placeViewModel: PlaceViewModel by viewModel()

    private var categoryTitle = ""
    private lateinit var locationId: String
    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryLabel = arguments?.getString("TAG_LABEL") ?: ""
        categoryTitle = arguments?.getString("CATEGORY_TITLE") ?: ""

        locationId = arguments?.getString("location_id") ?: ""
        cityName = arguments?.getString("city_name") ?: ""

        if (locationId.isNotEmpty()){
            placeViewModel.getPlaceByCity(locationId)
        }

        placeViewModel.getCategoryPlaces(categoryLabel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater)

        binding.fragment = this

        if (cityName.isNotEmpty()){
            binding.tvCategoryPlace.text = cityName
        } else {
            binding.tvCategoryPlace.text = categoryTitle
        }

        initAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun initAdapter(){
        adapter = CategoryPlaceAdapter{ place ->
            val placeId = place.id
            val bundle = bundleOf(Pair(Args.PLACE_ID, placeId))
            findNavController().navigate(R.id.action_category_to_detail_place, bundle)
        }

        binding.rvListPlaces.adapter = adapter
    }

    private fun setupObserver(){
        placeViewModel.categoryPlaces.observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.rvListPlaces.show()
                        binding.progressBar.hide()
                        resource.data.let { listPlaces ->
                            adapter.submitList(listPlaces)
                        }
                    }
                    Status.ERROR -> {
                        binding.rvListPlaces.show()
                        binding.progressBar.hide()
                        Timber.d(it.message)
                    }
                    Status.LOADING -> {
                        binding.rvListPlaces.hide()
                        binding.progressBar.show()
                    }
                }
            }
        })

        placeViewModel.placesByCity.observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.rvListPlaces.show()
                        binding.progressBar.hide()
                        resource.data.let { listPlaces ->
                            adapter.submitList(listPlaces)
                        }
                    }
                    Status.ERROR -> {
                        binding.rvListPlaces.show()
                        binding.progressBar.hide()
                        Timber.d(it.message)
                    }
                    Status.LOADING -> {
                        binding.rvListPlaces.hide()
                        binding.progressBar.show()
                    }
                }
            }
        })
    }

    fun initEvent(view: View){
        if (view == binding.ivArrowBack){
            findNavController().popBackStack()
        }
    }

}