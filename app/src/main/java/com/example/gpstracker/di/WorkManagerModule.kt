package com.example.gpstracker.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.gpstracker.roomdb.LocationRepository
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import com.example.gpstracker.usecase.LocationTrackerUseCase
import com.example.gpstracker.usecase.workManager.ChildWorkerFactory
import com.example.gpstracker.usecase.workManager.CustomWorker
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Binds
import dagger.Component
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
object WorkManagerModule {

    @Singleton
    @Provides
    fun provideWorkManager(context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideWorkerParameters(context: Context, workerParams: WorkerParameters): WorkerParameters {
        return workerParams
    }


    @Provides
    fun provideSyncDataUseCase(context: Context, workerParams: WorkerParameters): CustomWorker {
        return provideSyncDataUseCase(context, workerParams)
    }

    @Provides
    fun provideLocationTrackerUseCase(fusedLocationClient: FusedLocationProviderClient): LocationTrackerUseCase {
        return LocationTrackerUseCase(fusedLocationClient)
    }


    @MapKey
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class WorkerKey(val value: KClass<out ListenableWorker>)

/*    @Module
    interface WorkerBindingModule {
        @Binds
        @IntoMap
        @WorkerKey(CustomWorker::class)
        fun bindHelloWorldWorker(factory: CustomWorker.Factory): ChildWorkerFactory
    }*/
/*
    @Component(
        modules = [
            WorkerBindingModule::class,
        ]
    )*/


/*    @Provides
    fun provideDaggerWorkerFactory(factory: CustomWorkerFactory): WorkerFactory {
        return factory
    }

    @Provides
    @Singleton
    fun workerFactory(locationServiceUseCase: FirebaseDatabaseUseCase, locationRepository: LocationRepository): WorkerFactory {
        return CustomWorkerFactory(locationServiceUseCase,locationRepository)
    }*/

}