package vinova.kane.touristguide.vo

data class CityResponse(
    val results: List<City>,
    val more: Boolean
)