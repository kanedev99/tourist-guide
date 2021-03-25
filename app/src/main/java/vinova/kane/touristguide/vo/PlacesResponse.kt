package vinova.kane.touristguide.vo

data class PlacesResponse(
    val results: List<Place>,
    val more: Boolean
)