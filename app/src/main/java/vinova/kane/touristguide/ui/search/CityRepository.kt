package vinova.kane.touristguide.ui.search

import vinova.kane.touristguide.api.ApiHelper

class CityRepository(private val apiHelper: ApiHelper) {
    suspend fun getCities() = apiHelper.getCities()
}