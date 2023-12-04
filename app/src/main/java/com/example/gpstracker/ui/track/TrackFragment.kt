package com.example.gpstracker.ui.track

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.databinding.FragmentTrackBinding
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class TrackFragment : Fragment(R.layout.fragment_track) {

    private val binding by viewBinding(FragmentTrackBinding::bind)

    @Inject
    lateinit var trackViewModel: TrackViewModel

    private var isTracking = false
    val valueAnimator = ValueAnimator.ofInt(1, 360)
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInstantiation()
        startButtonClicked()
        observeTrackState()
        initAnimation()
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
        trackViewModel.syncLocalDatabaseAndRemoteDatabase()
    }

    private fun observeTrackState() {
        trackViewModel.getStateLiveData().observe(viewLifecycleOwner) { state ->
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
                    buttonIsDisabled()
                    startWorkManager()
                    setTitleDisconnected()
                }
            }
        }
    }


    private fun startButtonClicked() {
        binding.startButton.setOnClickListener {
            if (!isTracking) {
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
                }else{
                    // Permission granted
                    isTracking = true
                    trackViewModel._stateLiveData.value = TrackerState.ON
                }

            } else {
                isTracking = false
                trackViewModel._stateLiveData.value = TrackerState.OFF
            }
        }
    }

    private fun startTrackLocation() {
        trackViewModel.startTrackingService()
    }

    private fun stopTrackLocation() {
        trackViewModel.stopTrackingService()
    }


    private fun trackInternetAvailability() {
        trackViewModel.startTrackInternetAvailability()
    }

    private fun stopTrackInternetAvailability() {
        trackViewModel.stopTrackInternetAvailability()
    }

    private fun viewModelInstantiation() {
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    private fun changeCustomViewState(state: TrackerState) {
        binding.statefulCircleView.setState(state)
    }

    private fun buttonIsDisabled() {
        binding.startButton.invalidate()
        binding.startButton.setText(getString(R.string.stop))
    }

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