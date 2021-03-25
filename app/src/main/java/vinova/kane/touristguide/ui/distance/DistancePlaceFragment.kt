package vinova.kane.touristguide.ui.distance

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import timber.log.Timber
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentDistancePlaceBinding
import vinova.kane.touristguide.ext.View.hide
import vinova.kane.touristguide.ext.View.show
import vinova.kane.touristguide.ui.home.PlaceViewModel
import vinova.kane.touristguide.utils.Args
import vinova.kane.touristguide.utils.GpsTracker
import vinova.kane.touristguide.vo.Status
import java.io.IOException
import java.util.*


class DistancePlaceFragment : Fragment() {
    private lateinit var binding: FragmentDistancePlaceBinding
    private lateinit var adapter: DistancePlaceAdapter

    private val placeViewModel: PlaceViewModel by viewModel()

    companion object {
        fun newInstance() =
            DistancePlaceFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDistancePlaceBinding.inflate(inflater)

        initAdapter()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requirePermission()
    }

    private fun initAdapter(){
        adapter = DistancePlaceAdapter{ place ->
            val placeId = place.id
            val bundle = bundleOf(Pair(Args.PLACE_ID, placeId))
            findNavController().navigate(R.id.action_home_to_detail_place, bundle)
        }

        binding.rvListPlaces.adapter = adapter
    }

    private fun setupObserver(){
        placeViewModel.distancePlaces.observe(viewLifecycleOwner, Observer {
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

    private fun requirePermission() {
        try {
            if (ContextCompat.checkSelfPermission(
                    activity?.applicationContext!!,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(ACCESS_FINE_LOCATION),
                    101
                )
            } else {
                getLocation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getLocation() {
        val gpsTracker = GpsTracker(context)
        if (gpsTracker.canGetLocation()) {
            val latitude: Double = gpsTracker.latitude
            val longitude: Double = gpsTracker.longitude
            val address = getAddress(latitude, longitude)
            Timber.d(address)
            placeViewModel.getDistancePlaces("distance:$latitude,$longitude")
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geo = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address> = geo.getFromLocation(lat, lng, 1)
            return if (addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0)
            } else {
                "List Address is empty"
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
        return "No location name"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.d("Enable GPS")
                getLocation()
            }
        } catch (e: Exception) {
            Timber.e("$e")
        }
    }

}