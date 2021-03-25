package vinova.kane.touristguide.api

import retrofit2.Response
import vinova.kane.touristguide.vo.CityResponse
import vinova.kane.touristguide.vo.PlacesResponse

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPopularPlaces(): Response<PlacesResponse> = apiService.getPopularPlace()

    override suspend fun getDistancePlaces(location: String):
            Response<PlacesResponse> = apiService.getDistancePlace(location = location)

    override suspend fun getDetailPlace(placeId: String):
            Response<PlacesResponse> = apiService.getPlaceDetail(placeId)

    override suspend fun getCategoryPlace(label: String):
            Response<PlacesResponse> = apiService.getCategoryPlace(label = label)

    override suspend fun getCities():
            Response<CityResponse> = apiService.getCities()

    override suspend fun getPlaceByCity(locationId: String):
            Response<PlacesResponse> = apiService.getPlaceByCity(locationId = locationId)
}
