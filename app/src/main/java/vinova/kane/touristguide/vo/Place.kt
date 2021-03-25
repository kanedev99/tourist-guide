package vinova.kane.touristguide.vo

import com.google.gson.annotations.SerializedName


data class Place (
    val id: String ,
    val attribution: List<Attribution>,
    val coordinates: Coordinates,
    val images: List<Image>?,
    val name: String,
    val score: Float,
    val facebookId: String,
    val intro: String,
    val snippet: String,
    val distance: Int
)

data class Attribution(
    @SerializedName("source_id")
    val sourceId: String,
    val url: String
)

data class Image(
    val sizes: Sizes,
    val sourceId: String,
    val sourceUrl: String
)

data class Sizes(
    val medium: Medium,
    val original: Original,
    val thumbnail: Thumbnail
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class Original(
    val format: String,
    val url: String,
)

data class Medium(
    val format: String,
    val url: String,
)

data class Thumbnail(
    val format: String,
    val url: String,
)


