package vinova.kane.touristguide.utils

class Api {
    companion object {
        const val BASE_URL = "https://www.triposo.com/api/20201111/"
        const val COUNTRY_CODE = "vn"
        const val COUNT_RESULT = 100
        const val REQUEST_ACCOUNT_ID = "EJHI0TE3"
        const val REQUEST_API_TOKEN = "49xo0o4eyg2r6ksbl5bo4ggbtc5h0y4b"
        const val FIELDS = "intro,name,score,images,attribution,id,coordinates,snippet"
        const val CITY_FIELDS = "id,name"
        const val ORDER_BY_SCORE = "-score"
        const val ORDER_BY_DISTANCE = "distance"
        const val DISTANCE = "<=15000"
    }
}

class Query {
    companion object{
        const val PLACE= "PLACE_TYPE"
        const val SIGHT_SEEING = "sightseeing"
        const val EAT_AND_DRINK = "eatingout"
        const val NIGHT_LIFE = "nightlife"
        const val HOTEL = "hotels"
        const val SHOPPING = "shopping"
        const val ACTIVITY = "do"
        const val AIRPORT = "poitype-Airport"
        const val BANK = "poitype-Bank"
    }
}

class Args {
    companion object{
        const val PLACE_OBJECT = "PLACE_OBJECT_ARG"
        const val PLACE_ID = "PLACE_ID_ARG"
        const val COORDINATE = "COORDINATE_ARG"
    }
}


