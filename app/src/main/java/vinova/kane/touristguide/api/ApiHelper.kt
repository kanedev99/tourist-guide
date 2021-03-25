package vinova.kane.touristguide.api

import retrofit2.Response
import vinova.kane.touristguide.vo.CityResponse
import vinova.kane.touristguide.vo.PlacesResponse

interface ApiHelper {
    suspend fun getPopularPlaces(): Response<PlacesResponse>
    suspend fun getDistancePlaces(location: String): Response<PlacesResponse>
    suspend fun getDetailPlace(placeId: String): Response<PlacesResponse>
    suspend fun getCategoryPlace(label: String): Response<PlacesResponse>
    suspend fun getCities(): Response<CityResponse>
    suspend fun getPlaceByCity(locationId: String): Response<PlacesResponse>

}