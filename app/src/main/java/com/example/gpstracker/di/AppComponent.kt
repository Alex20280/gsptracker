package com.example.gpstracker.di

import com.example.gpstracker.MainActivity
import com.example.gpstracker.ui.forgetpassword.ForgetPasswordFragment
import com.example.gpstracker.ui.signin.SignInFragment
import com.example.gpstracker.ui.signup.SignUpFragment
import com.example.gpstracker.ui.track.TrackFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ViewModelModule::class, FirebaseRegistrationModule::class, LocationServiceModule::class, RoomDatabaseModule::class])
@Singleton
interface AppComponent {
    fun inject(mainActivity: MainActivity?)
    fun inject(collectionsFragment: ForgetPasswordFragment?)
    fun inject(mapsFragment: SignInFragment?)
    fun inject(mapsFragment: SignUpFragment?)
    fun inject(mapsFragment: TrackFragment?)
}
