package vinova.kane.touristguide.ui.home

import vinova.kane.touristguide.api.ApiHelper

class PlaceRepository(private val apiHelper: ApiHelper) {
    suspend fun getPopularPlaces() = apiHelper.getPopularPlaces()
    suspend fun getDistancePlaces(location: String) = apiHelper.getDistancePlaces(location)
    suspend fun getDetailPlace(placeId: String) = apiHelper.getDetailPlace(placeId)
    suspend fun getCategoryPlaces(label: String) = apiHelper.getCategoryPlace(label)
    suspend fun getPlaceByCity(locationId: String) = apiHelper.getPlaceByCity(locationId)

}