package com.example.gpstracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gpstracker.ui.forgetpassword.viewmodel.ForgetPasswordViewModel
import com.example.gpstracker.ui.signin.viewmodel.SignInViewModel
import com.example.gpstracker.ui.signup.viewmodel.SignUpViewModel
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
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
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModels[modelClass]

        if (viewModelProvider != null) {
            return viewModelProvider.get() as T
        }
        when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                return SignInViewModel() as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                return SignUpViewModel() as T
            }
            modelClass.isAssignableFrom(TrackViewModel::class.java) -> {
                return TrackViewModel() as T
            }
            modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java) -> {
                return ForgetPasswordViewModel() as T
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
