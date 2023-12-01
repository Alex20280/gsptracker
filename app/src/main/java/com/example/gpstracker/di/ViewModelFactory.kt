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
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = providers[modelClass]
        return provider?.get() as T ?: throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}