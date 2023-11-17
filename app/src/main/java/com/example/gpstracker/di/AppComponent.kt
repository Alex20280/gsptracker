package com.example.gpstracker.di

import com.example.gpstracker.MainActivity
import com.example.gpstracker.ui.forgetpassword.ForgetPasswordFragment
import com.example.gpstracker.ui.service.TrackingService
import com.example.gpstracker.ui.signin.SignInFragment
import com.example.gpstracker.ui.signup.SignUpFragment
import com.example.gpstracker.ui.track.TrackFragment
import com.example.gpstracker.usecase.workManager.CustomWorker
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    AppModule::class,
    ViewModelModule::class,
    FirebaseRegistrationModule::class,
    LocationServiceModule::class,
    RoomDatabaseModule::class,
    WorkManagerModule::class,
    DataPreferenceModule::class])
@Singleton
interface AppComponent {
    fun inject(mainActivity: MainActivity?)
    fun inject(collectionsFragment: ForgetPasswordFragment?)
    fun inject(mapsFragment: SignInFragment?)
    fun inject(mapsFragment: SignUpFragment?)
    fun inject(mapsFragment: TrackFragment?)
    fun inject(worker: CustomWorker?)
    fun inject(service: TrackingService?)
}
