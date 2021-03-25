package vinova.kane.touristguide.di

import org.koin.dsl.module
import vinova.kane.touristguide.ui.home.PlaceRepository
import vinova.kane.touristguide.ui.search.CityRepository

val repoModule = module {
    single { PlaceRepository(get()) }
    single { CityRepository(get()) }
}