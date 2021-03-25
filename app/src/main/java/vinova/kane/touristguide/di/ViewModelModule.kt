package vinova.kane.touristguide.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import vinova.kane.touristguide.firebase.FirebaseViewModel
import vinova.kane.touristguide.ui.home.PlaceViewModel
import vinova.kane.touristguide.ui.search.CityViewModel

val viewModelModule = module {
    viewModel { PlaceViewModel(get(), get()) }
    viewModel { CityViewModel(get(), get()) }
    viewModel {FirebaseViewModel()}
}