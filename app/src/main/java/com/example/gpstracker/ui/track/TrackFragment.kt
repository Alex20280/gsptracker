package com.example.gpstracker.ui.track

import android.Manifest
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.extensions.viewBinding
import com.example.gpstracker.databinding.FragmentTrackBinding
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
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

        injectDependencies()
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


    private fun observeTrackState() {
        trackViewModel.getStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                TrackerState.ON -> {
                    changeCustomViewState(TrackerState.ON)
                    buttonIsDisabled()
                    startTrackLocation()
                    trackGpsAvailability()
                    startTrackInternetAvailability()
                    setTitleOn()
                    startCircleAnimation()
                }

                TrackerState.OFF -> {
                    changeCustomViewState(TrackerState.OFF)
                    buttonIsEnabled()
                    stopTrackGpsAvailability()
                    stopTrackLocation()
                    setTitleOff()
                    stopTrackInternetAvailability()
                    stopCircleAnimation()
                }

                TrackerState.DISCONNECTED -> {
                    changeCustomViewState(TrackerState.DISCONNECTED)
                    buttonIsEnabled()
                    setTitleDisconnected()
                    isTracking = false
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
                } else {
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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    trackViewModel._stateLiveData.value = TrackerState.ON
                    isTracking = true
                }
            }
        }
    }

    private fun startTrackLocation() {
        val isGpsEnabled = trackViewModel.isLocationEnabled()
        if (!isGpsEnabled) {
            locationServiceDialogShow()
            return
        }
        trackViewModel.startTrackingService()
    }

    private fun locationServiceDialogShow() {
        AlertDialog.Builder(requireContext())
            .setTitle("GPS Disabled")
            .setMessage("Please enable location services to allow tracking")
            .setPositiveButton("Enable") { _, _ ->
                trackViewModel.requestLocationEnable()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun stopTrackLocation() {
        trackViewModel.stopTrackingService()
    }


    private fun trackGpsAvailability() {
        trackViewModel.startTrackGpsAvailability()
    }

    private fun stopTrackGpsAvailability() {
        trackViewModel.stopTrackGpsAvailabilityCheck()
    }

    private fun startTrackInternetAvailability(){
        trackViewModel.startTrackInternetAvailability()
    }

    private fun stopTrackInternetAvailability(){
        trackViewModel.stopInternetAvailabilityCheck()
    }

    private fun injectDependencies() {
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