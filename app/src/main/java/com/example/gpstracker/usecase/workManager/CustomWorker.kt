package com.example.gpstracker.usecase.workManager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gpstracker.app.App
import com.example.gpstracker.roomdb.LocationRepository
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import javax.inject.Inject

class CustomWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    init {
        (context.applicationContext as App).appComponent.inject(this)
    }

   @Inject
    lateinit var locationServiceUseCase: FirebaseDatabaseUseCase
    @Inject
    lateinit var locationRepository: LocationRepository

    override suspend fun doWork(): Result {

        Log.d("SomeCustoWorkerm", "working")
        return try {
            val result = locationRepository.getUnsynchronizedLocations()

            for (location in result) {
                // Step 2: Send unsynchronized data to Firebase
                Log.d("MyworkManagerRun",location.id.toString() + "long: " + location.latitude + "sync: " + location.isSynchronized.toString())
                val success = locationServiceUseCase.isLocationSavedFromRoomDatabase(location.latitude, location.longitude)

                if (success) {
                    // Mark the location as synchronized if it was sent successfully
                    locationRepository.markLocationAsSynchronizedBasedOnId(location.id)
                }
            }

/*            val allLocations = locationRepository.getAllLocations()
            for (myLocations in allLocations) {
                Log.d("MyworkManagerRun",myLocations.id.toString() + "long: " + myLocations.latitude + "sync: " + myLocations.isSynchronized.toString())
            }*/

            Log.d("MyWorkerJob", "success")
            Result.success()
        } catch (e: Exception) {
            Log.e("MyWorkerJob", "Failure: ${e.message}", e)
            Result.failure()
        }
    }
}