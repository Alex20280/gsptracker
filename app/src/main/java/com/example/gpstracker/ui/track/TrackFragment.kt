package com.example.gpstracker.ui.track

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
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
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class TrackFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentTrackBinding

    private var isTracking = false

    private var timer: Timer? = null

    @JvmField
    @Inject
    var trackViewModel: TrackViewModel? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

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
        observeTrackState()

        return binding.root
    }

    private fun observeTrackState() {
        trackViewModel?.getStateLiveData()?.observe(viewLifecycleOwner) { state ->
            when (state) {
                TrackerState.ON -> {
                    activeCustomView()
                    buttonIsDisabled()
                    startTrackingPeriodically()
                }

                TrackerState.OFF -> {
                    inactiveCustomView()
                    buttonIsEnabled()
                    stopTrackingPeriodically()
                }

                TrackerState.DISCONNECTED -> {
                    disconnectedCustomView()
                    buttonIsEnabled()
                    stopTrackingPeriodically()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO
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
            if (!isTracking) {
                isTracking = true
                trackViewModel?._stateLiveData?.value = TrackerState.ON
            } else {
                isTracking = false
                trackViewModel?._stateLiveData?.value = TrackerState.OFF
            }
        }
    }


    private fun startTrackingPeriodically() {
        if (timer == null) {
            timer = Timer()
            val trackingIntervalMillis = 10000L//600000L  // 10 minutes
            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val isInternetConnected = trackViewModel?.isInternetConnected()
                    val isFirebaseConnected = trackViewModel?.isFirebaseDatabaseAvailable()
                    if (isInternetConnected == true && isFirebaseConnected == true) {
                        trackViewModel?.startTracking()
                    } else {
                        trackViewModel?._stateLiveData?.postValue(TrackerState.DISCONNECTED)
                        trackViewModel?.saveToRoomDatabase()
                    }
                }
            }, 0, trackingIntervalMillis)
        }
    }

    private fun stopTrackingPeriodically() {
        timer?.cancel()
        timer = null
    }

    private fun viewModelInstanciation() {
        (requireContext().applicationContext as App).appComponent.inject(this)
        trackViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(TrackViewModel::class.java)
    }

    private fun activeCustomView() {
        binding.statefulCircleView.setProgressBarColor(resources.getColor(R.color.colorAccent))
        binding.statefulCircleView.setState(TrackerState.ON)
    }

    private fun inactiveCustomView() {
        binding.statefulCircleView.setProgressBarColor(resources.getColor(R.color.light_grey))
        binding.statefulCircleView.setState(TrackerState.OFF)
    }

    private fun disconnectedCustomView() {
        binding.statefulCircleView.setProgressBarColor(resources.getColor(R.color.red))
        binding.statefulCircleView.setState(TrackerState.DISCONNECTED)
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