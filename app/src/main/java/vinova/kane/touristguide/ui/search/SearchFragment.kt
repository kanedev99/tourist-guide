package vinova.kane.touristguide.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentSearchBinding
import vinova.kane.touristguide.vo.City
import vinova.kane.touristguide.vo.Status

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val cityViewModel: CityViewModel by viewModel()

    lateinit var adapter: ArrayAdapter<String>
    private var listCitiesName: ArrayList<String> = arrayListOf()
    private var listCities: ArrayList<City> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)

        setUpObserver()
        initEvent()
        initAdapter()

        return binding.root
    }

    private fun initEvent(){
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }

            override fun afterTextChanged(searchText: Editable?) {

            }
        })
    }

    private fun setUpObserver(){
        cityViewModel.cities.observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        resource.data.let { cities ->
                            if (cities != null) {
                                listCities = cities as ArrayList<City>
                                for(city in cities){
                                    listCitiesName.add(city.name)
                                }
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        })
    }

    private fun initAdapter(){
        adapter = ArrayAdapter(requireContext(), R.layout.item_category, R.id.tvCategory, listCitiesName)

        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val cityName = adapter.getItem(position)
            val locationId = cityName?.let { lookupIdByName(it) }
            val bundle = bundleOf("location_id" to locationId, "city_name" to cityName)
            findNavController().navigate(R.id.action_search_to_category_place, bundle)
        }

        binding.listView.adapter = adapter
    }

    private fun lookupIdByName(name: String): String{
        for (item in listCities){
            if (item.name == name){
                return item.id
            }
        }
        return ""
    }

}