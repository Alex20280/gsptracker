package com.example.gpstracker.ui.track

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.databinding.FragmentTrackBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


class TrackFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentTrackBinding? = null
    private val binding get() = _binding!!

    private var isTracking = false

    private var internetTimer: Timer? = null
    private var dataSentTimer: Timer? = null

    val valueAnimator = ValueAnimator.ofInt(1, 360)

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
        _binding = FragmentTrackBinding.inflate(inflater, container, false)

        viewModelInstanciation()
        startButtonClicked()
        observeTrackState()
        initAnimation()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initAnimation() {
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener { animation ->
            binding.statefulCircleView.setValue(animation.animatedValue as Int)
        }
    }

    private fun startCircleAnimation() {
        valueAnimator.start()
    }

    private fun stopCircleAnimation() {
        valueAnimator.cancel()
    }

    private fun startWorkManager() {
        trackViewModel?.syncLocalDatabaseAndRemoteDatabase()
    }

    private fun observeTrackState() {
        trackViewModel?.getStateLiveData()?.observe(viewLifecycleOwner) { state ->
            when (state) {
                TrackerState.ON -> {
                    changeCustomViewState(TrackerState.ON)
                    buttonIsDisabled()
                    startTrackLocation()
                    trackInternetAvailability()
                    setTitleOn()
                    startCircleAnimation()
                }

                TrackerState.OFF -> {
                    changeCustomViewState(TrackerState.OFF)
                    buttonIsEnabled()
                    stopTrackInternetAvailability()
                    stopTrackLocation()
                    setTitleOff()
                    stopCircleAnimation()
                }

                TrackerState.DISCONNECTED -> {
                    changeCustomViewState(TrackerState.DISCONNECTED)
                    buttonIsEnabled()
                    startWorkManager() //
                    setTitleDisconnected()
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
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("dscdcdscdvf", trackViewModel?.getDataStoreUID().toString())
            }
            if (!isTracking) {
                isTracking = true
                trackViewModel?._stateLiveData?.value = TrackerState.ON
            } else {
                isTracking = false
                trackViewModel?._stateLiveData?.value = TrackerState.OFF
            }
        }
    }

    private fun startTrackLocation() {
        if (dataSentTimer == null) {
            dataSentTimer = Timer()
            val trackingIntervalMillis = 10000L// 10 sec
            dataSentTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val isInternetConnected = trackViewModel?.isInternetConnected()
                    val isFirebaseConnected = trackViewModel?.isFirebaseDatabaseAvailable()
                    if (isInternetConnected == true && isFirebaseConnected == true) {
                        trackViewModel?.saveLocation()
                    } else {
                        trackViewModel?.saveToRoomDatabase()
                    }
                }
            }, 0, trackingIntervalMillis)
        }
    }

    private fun stopTrackLocation() {
        dataSentTimer?.cancel()
        dataSentTimer = null
    }


    private fun trackInternetAvailability() {
        if (internetTimer == null) {
            internetTimer = Timer()
            val trackingIntervalMillis = 5000L//600000L  // 5 minutes
            internetTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val isInternetConnected = trackViewModel?.isInternetConnected()
                    val isFirebaseConnected = trackViewModel?.isFirebaseDatabaseAvailable()
                    val currentState = trackViewModel?.getStateLiveData()?.value
                    if (currentState == TrackerState.DISCONNECTED && isInternetConnected == true && isFirebaseConnected == true) {
                        trackViewModel?._stateLiveData?.postValue(TrackerState.ON)
                    } else if (currentState == TrackerState.ON && (isInternetConnected == false || isFirebaseConnected == false)) {
                        trackViewModel?._stateLiveData?.postValue(TrackerState.DISCONNECTED)
                    }
                }
            }, 0, trackingIntervalMillis)
        }
    }

    private fun stopTrackInternetAvailability() {
        internetTimer?.cancel()
        internetTimer = null
    }

    private fun viewModelInstanciation() {
        (requireContext().applicationContext as App).appComponent.inject(this)
        trackViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(TrackViewModel::class.java)
    }

    private fun changeCustomViewState(state: TrackerState) {
        binding.statefulCircleView.setState(state)
    }

    private fun buttonIsDisabled() {
        binding.startButton.invalidate()
        binding.startButton.setText(getString(R.string.stop))
    }

    /*    private fun changeProgressBarState(state: TrackerState){
            binding.statefulCircleView.setProgressState(state)
        }*/

    private fun buttonIsEnabled() {
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        binding.startButton.backgroundTintList = ColorStateList.valueOf(colorWhite)
        binding.startButton.setText(getString(R.string.start))
    }

    private fun setTitleOn() {
        binding.trackerStatusTv.text = getString(R.string.tracker_collects_locations)
    }

    private fun setTitleOff() {
        binding.trackerStatusTv.text = getString(R.string.tracker_is_off)
    }

    private fun setTitleDisconnected() {
        binding.trackerStatusTv.text = getString(R.string.gps_is_off)
    }
}