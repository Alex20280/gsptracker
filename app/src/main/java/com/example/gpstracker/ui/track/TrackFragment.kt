package com.example.gpstracker.ui.track

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.databinding.FragmentTrackBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
import com.example.gpstracker.utils.TrackerState
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class TrackFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentTrackBinding

    private var isTracking = false

    val timer = Timer()

    @JvmField
    @Inject
    var trackViewModel: TrackViewModel? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 100 //123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLocationPermission()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackBinding.inflate(inflater, container, false)

        viewModelInstanciation()
        startButtonClicked()

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted; you can proceed with location-related tasks
                //trackViewModel?.startLocationUpdates() //TODO
                //TODO

            } else {
                // Permission denied; handle accordingly
                // You may want to show a message to the user or disable location-related features
            }
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted; request it
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission is already granted; you can proceed with location-related tasks
        }
    }

    private fun startButtonClicked() {
        binding.startButton.setOnClickListener {
            val isInternetConnected = trackViewModel?.isInternetConnected()
            val isFirebaseAvailable = trackViewModel?.isFirebaseDatabaseAvailable()

            if (isInternetConnected == true && isFirebaseAvailable == true) {
                // Internet is available, and Firebase is available
                if (!isTracking) {
                    isTracking = true
                    //trackViewModel?.startTracking()
                    switchToOffState()
                    buttonIsDisabled()
                    startTrackingPeriodically()
                } else {
                    // If already tracking, you can stop it here if needed
                    stopTracking()  //TODO
                }
            } else {
                trackViewModel?.saveToRoomDatabase()
            }
        }
    }


    private fun startTrackingPeriodically() {
        // Schedule tracking to occur every 10 minutes (600,000 milliseconds)
        val trackingIntervalMillis = 1000L//600000L  //TODO
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                trackViewModel?.startTracking()
            }
        }, 0, trackingIntervalMillis)
    }

    private fun stopTracking() {
        // Stop the tracking process and timer if needed
        // You can add logic here to stop location updates
        isTracking = false
        // Cancel the timer if it's no longer needed
        timer.cancel()
    }

    private fun viewModelInstanciation() {
        (requireContext().applicationContext as App).appComponent.inject(this)
        trackViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(TrackViewModel::class.java)
    }

    private fun switchToOnState() {
        binding.statefulCircleView.setState(TrackerState.ON)
        binding.statefulCircleView.setProgressBarColor(resources.getColor(R.color.light_grey))
    }

    private fun switchToOffState() {
        binding.statefulCircleView.setState(TrackerState.OFF)
        binding.statefulCircleView.setProgressBarColor(resources.getColor(R.color.colorAccent))
    }

    private fun switchToDisconnectedState() {
        binding.statefulCircleView.setState(TrackerState.DISCONNECTED)
        binding.statefulCircleView.setProgressBarColor(resources.getColor(R.color.red))
    }

    private fun buttonIsDisabled() {
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        //binding.startButton.background.setColorFilter(colorWhite, PorterDuff.Mode.SRC_ATOP)
        binding.startButton.invalidate()
        binding.startButton.setText("Stop")
    }

    private fun buttonIsEnabled() {
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        binding.startButton.backgroundTintList = ColorStateList.valueOf(colorWhite)
        binding.startButton.setText("Start")
    }


}