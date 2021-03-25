package vinova.kane.touristguide.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vinova.kane.touristguide.utils.NetworkHelper
import vinova.kane.touristguide.vo.City
import vinova.kane.touristguide.vo.Resource

class CityViewModel(
    private val cityRepository: CityRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _cities = MutableLiveData<Resource<List<City>>>()
    val cities: LiveData<Resource<List<City>>>
        get() = _cities

    init {
        fetchCities()
    }


    private fun fetchCities() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _cities.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    cityRepository.getCities().let {
                        if (it.isSuccessful) {
                            _cities.postValue(Resource.success(it.body()?.results))
                        } else _cities.postValue(
                            Resource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                } else _cities.postValue(Resource.error("No internet connection", null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}