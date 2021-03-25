package vinova.kane.touristguide.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import timber.log.Timber
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentPopularPlaceBinding
import vinova.kane.touristguide.ext.View.hide
import vinova.kane.touristguide.ext.View.show
import vinova.kane.touristguide.ui.home.PlaceViewModel
import vinova.kane.touristguide.utils.Args
import vinova.kane.touristguide.vo.Status

class PopularPlaceFragment : Fragment() {

    private lateinit var binding: FragmentPopularPlaceBinding
    private lateinit var adapterPopular: PopularPlaceAdapter

    private val placeViewModel: PlaceViewModel by viewModel()

    companion object {
        fun newInstance() =
            PopularPlaceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPopularPlaceBinding.inflate(inflater)

        initAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun initAdapter(){
        adapterPopular = PopularPlaceAdapter{ place ->
            val bundle = bundleOf(Args.PLACE_ID to place.id)
            findNavController().navigate(R.id.action_home_to_detail_place, bundle)
        }

        binding.rvListPlaces.adapter = adapterPopular
    }

    private fun setupObserver(){
        placeViewModel.popularPlaces.observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.rvListPlaces.show()
                        binding.progressBar.hide()
                        resource.data.let { listPlaces ->
                            adapterPopular.submitList(listPlaces)
                        }
                    }
                    Status.ERROR -> {
                        binding.rvListPlaces.show()
                        binding.progressBar.hide()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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

}