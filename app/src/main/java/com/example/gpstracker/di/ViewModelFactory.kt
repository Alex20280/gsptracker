package com.example.gpstracker.di

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.gpstracker.base.extentions.myDataStore
import com.example.gpstracker.prefs.DataStorePreference
import com.example.gpstracker.roomdb.LocationRepositoryImpl
import com.example.gpstracker.roomdb.dao.LocationDao
import com.example.gpstracker.ui.forgetpassword.viewmodel.ForgetPasswordViewModel
import com.example.gpstracker.ui.service.TrackingService
import com.example.gpstracker.ui.signin.viewmodel.SignInViewModel
import com.example.gpstracker.ui.signup.viewmodel.SignUpViewModel
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
import com.example.gpstracker.usecase.FirebaseAuthenticationUseCase
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import com.example.gpstracker.usecase.LocationTrackerUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val databaseReference: DatabaseReference,
    private val viewModels: MutableMap<Class<out ViewModel>,
            Provider<ViewModel>>,
    private val context: Context,
    private val locationDao: LocationDao,
    private val workManager: WorkManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModels[modelClass]

        if (viewModelProvider != null) {
            return viewModelProvider.get() as T
        }
        when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel(
                    firebaseAuthenticationUseCase = FirebaseAuthenticationUseCase(
                        auth = FirebaseAuth.getInstance()),
                    dataStorePreference = DataStorePreference(context.myDataStore)
                ) as T
            }

            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                return SignUpViewModel(registrationUseCase = FirebaseAuthenticationUseCase(
                    auth = FirebaseAuth.getInstance())) as T
            }

            modelClass.isAssignableFrom(TrackViewModel::class.java) -> {
                return TrackViewModel(
                    locationServiceUseCase = FirebaseDatabaseUseCase(
                        fusedLocationClient = fusedLocationProviderClient,
                        database = databaseReference
                    ), firebaseDatabase = databaseReference,
                    applicationContext = context,
                    locationRepository = LocationRepositoryImpl(locationDao),
                    locationTrackerUseCase = LocationTrackerUseCase(
                        fusedLocationClient = fusedLocationProviderClient
                    ),
                    workManager = workManager,
                    dataStorePreference = DataStorePreference(context.myDataStore)
                ) as T
            }

            modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java) -> {
                return ForgetPasswordViewModel(firebaseAuth = FirebaseAuthenticationUseCase(
                    auth = FirebaseAuth.getInstance())) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(
    RetentionPolicy.RUNTIME
)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out Any>)

@Module
internal abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(signUpViewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrackViewModel::class)
    abstract fun bindTrackViewMode(trackViewMode: TrackViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgetPasswordViewModel::class)
    abstract fun bindForgetPasswordViewModel(forgetPasswordViewModel: ForgetPasswordViewModel): ViewModel

}
