package com.example.gpstracker.usecase

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gpstracker.roomdb.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SyncDatabaseUseCase(
    private val context: Context,
    private val params: WorkerParameters,
    private val locationRepository: LocationRepository,
    private val locationServiceUseCase: FirebaseDatabaseUseCase
) : Worker(context, params){


    override fun doWork(): Result {
        Log.d("MyWorkerJob", "success")

        return Result.success()
 /*       return try {
            val unsynchronizedLocations = locationRepository.getUnsynchronizedLocations()

            for (location in unsynchronizedLocations) {
                // Step 2: Send unsynchronized data to Firebase\
                val result = locationServiceUseCase.saveLocationFromRoomDatabase(
                    location.latitude,
                    location.longitude)

                if (result) {
                    // Mark the location as synchronized if it was sent successfully
                    locationRepository.markLocationAsSynchronizedBasedOnId(location.id)
                }
            }
            //Log.d("MyWorkerJob", "success")
            Result.success()
        } catch (e: Exception) {
            //Log.d("MyWorkerJob", "failure")
            Result.failure()
        }*/
    }
}