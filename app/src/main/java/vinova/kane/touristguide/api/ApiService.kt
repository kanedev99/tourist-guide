package vinova.kane.touristguide.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import vinova.kane.touristguide.utils.Api
import vinova.kane.touristguide.vo.CityResponse
import vinova.kane.touristguide.vo.Place
import vinova.kane.touristguide.vo.PlacesResponse

interface ApiService {
    @GET("poi.json")
    suspend fun getCategoryPlace(
        @Query("countrycode") countryCode: String = Api.COUNTRY_CODE,
        @Query("count") count: Int = Api.COUNT_RESULT,
        @Query("tag_labels") label: String,
        @Query("fields") fields: String = Api.FIELDS,
        @Header("X-Triposo-Account") account: String = Api.REQUEST_ACCOUNT_ID,
        @Header("X-Triposo-Token") token: String = Api.REQUEST_API_TOKEN
    ): Response<PlacesResponse>

    @GET("poi.json")
    suspend fun getPopularPlace(
        @Query("countrycode") countryCode: String = Api.COUNTRY_CODE,
        @Query("count") count: Int = Api.COUNT_RESULT,
        @Query("fields") fields: String = Api.FIELDS,
        @Query("order_by") order: String = Api.ORDER_BY_SCORE,
        @Header("X-Triposo-Account") account: String = Api.REQUEST_ACCOUNT_ID,
        @Header("X-Triposo-Token") token: String = Api.REQUEST_API_TOKEN
    ): Response<PlacesResponse>

    @GET("poi.json")
    suspend fun getPlaceDetail(
        @Query("id") placeId: String,
        @Query("fields") fields: String = Api.FIELDS,
        @Header("X-Triposo-Account") account: String = Api.REQUEST_ACCOUNT_ID,
        @Header("X-Triposo-Token") token: String = Api.REQUEST_API_TOKEN
    ): Response<PlacesResponse>

    @GET("poi.json")
    suspend fun getDistancePlace(
        @Query("count") count: Int = Api.COUNT_RESULT,
        @Query("fields") fields: String = Api.FIELDS,
        @Query("annotate") location: String,
        @Query("distance") distance: String = Api.DISTANCE,
        @Query("order_by") order: String = Api.ORDER_BY_DISTANCE,
        @Header("X-Triposo-Account") account: String = Api.REQUEST_ACCOUNT_ID,
        @Header("X-Triposo-Token") token: String = Api.REQUEST_API_TOKEN
    ): Response<PlacesResponse>

    @GET("location.json")
    suspend fun getCities(
        @Query("count") count: Int = Api.COUNT_RESULT,
        @Query("fields") fields: String = Api.CITY_FIELDS,
        @Query("part_of") partOf: String = "Vietnam",
        @Query("tag_labels") tagLabels: String = "city",
        @Query("order_by") order: String = "name",
        @Header("X-Triposo-Account") account: String = Api.REQUEST_ACCOUNT_ID,
        @Header("X-Triposo-Token") token: String = Api.REQUEST_API_TOKEN
    ): Response<CityResponse>

    @GET("poi.json")
    suspend fun getPlaceByCity(
        @Query("count") count: Int = Api.COUNT_RESULT,
        @Query("fields") fields: String = Api.FIELDS,
        @Query("location_id") locationId: String,
        @Header("X-Triposo-Account") account: String = Api.REQUEST_ACCOUNT_ID,
        @Header("X-Triposo-Token") token: String = Api.REQUEST_API_TOKEN
    ): Response<PlacesResponse>

}