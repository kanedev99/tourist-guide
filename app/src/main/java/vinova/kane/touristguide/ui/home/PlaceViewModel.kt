package vinova.kane.touristguide.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vinova.kane.touristguide.utils.NetworkHelper
import vinova.kane.touristguide.vo.Place
import vinova.kane.touristguide.vo.Resource

class PlaceViewModel(
    private val placeRepository: PlaceRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _popularPlaces = MutableLiveData<Resource<List<Place>>>()
    val popularPlaces: LiveData<Resource<List<Place>>>
        get() = _popularPlaces

    private val _distancePlaces = MutableLiveData<Resource<List<Place>>>()
    val distancePlaces: LiveData<Resource<List<Place>>>
        get() = _distancePlaces

    private val _categoryPlaces = MutableLiveData<Resource<List<Place>>>()
    val categoryPlaces: LiveData<Resource<List<Place>>>
        get() = _categoryPlaces

    private val _detailPlace = MutableLiveData<Resource<Place>>()
    val detailPlace: LiveData<Resource<Place>>
        get() = _detailPlace

    private val _placesByCity = MutableLiveData<Resource<List<Place>>>()
    val placesByCity: LiveData<Resource<List<Place>>>
        get() = _placesByCity

    init {
        fetchPopularPlaces()
    }

    private fun fetchPopularPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _popularPlaces.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    placeRepository.getPopularPlaces().let {
                        if (it.isSuccessful) {
                            _popularPlaces.postValue(Resource.success(it.body()?.results))
                        } else _popularPlaces.postValue(
                            Resource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                } else _popularPlaces.postValue(Resource.error("No internet connection", null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun getDistancePlaces(location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _distancePlaces.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    placeRepository.getDistancePlaces(location).let {
                        if (it.isSuccessful) {
                            _distancePlaces.postValue(Resource.success(it.body()?.results))
                        } else _distancePlaces.postValue(
                            Resource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                } else _distancePlaces.postValue(Resource.error("No internet connection", null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCategoryPlaces(label: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _categoryPlaces.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    placeRepository.getCategoryPlaces(label).let {
                        if (it.isSuccessful) {
                            _categoryPlaces.postValue(Resource.success(it.body()?.results))
                        } else _categoryPlaces.postValue(
                            Resource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                } else _categoryPlaces.postValue(Resource.error("No internet connection", null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getDetailPlace(placeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _detailPlace.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    placeRepository.getDetailPlace(placeId).let {
                        if (it.isSuccessful) {
                            _detailPlace.postValue(Resource.success(it.body()?.results?.get(0)))
                        } else _detailPlace.postValue(
                            Resource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                } else _detailPlace.postValue(Resource.error("No internet connection", null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPlaceByCity(locationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _placesByCity.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    placeRepository.getPlaceByCity(locationId).let {
                        if (it.isSuccessful) {
                            _placesByCity.postValue(Resource.success(it.body()?.results))
                        } else _placesByCity.postValue(
                            Resource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                } else _placesByCity.postValue(Resource.error("No internet connection", null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}