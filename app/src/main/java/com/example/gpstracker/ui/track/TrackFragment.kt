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
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.databinding.FragmentTrackBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.ui.track.screens.customview.CircleView
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


class TrackFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentTrackBinding

    private var isTracking = false

    private var internetTimer: Timer? = null
    private var dataSentTimer: Timer? = null

    private var workManager: WorkManager? = null
    private var workRequest: WorkRequest? = null

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
        binding = FragmentTrackBinding.inflate(inflater, container, false)

        viewModelInstanciation()
        startButtonClicked()
        observeTrackState()
        initAnimation()


/*        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener { animation ->
            Log.d("Anim", animation.animatedValue.toString())
            binding.circleView.setValue(animation.animatedValue as Int)
        }*/
        //alueAnimator.start()


        return binding.root
    }

    private fun initAnimation(){
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener { animation ->
            Log.d("Anim", animation.animatedValue.toString())
            binding.circleView.setValue(animation.animatedValue as Int)
        }
    }

    private fun startCircleAnimation(){
        valueAnimator.start()
    }

    private fun stopCircleAnimation(){
        valueAnimator.cancel()
    }

    private fun startWorkManager() {
        trackViewModel?.syncLocalDatabaseAndRemoteDatabase()
    }

    private fun observeTrackState() {
        trackViewModel?.getStateLiveData()?.observe(viewLifecycleOwner) { state ->
            when (state) {
                TrackerState.ON -> {
                    activeCustomView()
                    buttonIsDisabled()
                    startTrackLocation()
                    trackInternetAvailability()
                    setTitleOn()
                    setProgressBarToYellow()

                    startCircleAnimation()
                    binding.circleView.setState(3)
                }

                TrackerState.OFF -> {
                    inactiveCustomView()
                    buttonIsEnabled()
                    stopTrackInternetAvailability()
                    stopTrackLocation()
                    setTitleOff()
                    setProgressBarToGrey()

                    binding.circleView.setState(1)
                    stopCircleAnimation()
                }

                TrackerState.DISCONNECTED -> {
                    disconnectedCustomView()
                    buttonIsEnabled()
                    startWorkManager() //
                    setTitleDisconnected()
                    setProgressBarToRed()

                    binding.circleView.setState(2)
               }
            }
        }
    }

/*    private fun updateProgressBar() {
        binding.myCustomProgressBar.setProgress(progress)
    }*/

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
 /*           Log.d("startButtonclick", "startButtonclick")

            workManager = WorkManager.getInstance(requireContext())
            val workRequest = PeriodicWorkRequest.Builder(
                SyncDatabaseUseCase::class.java,
                15, // Repeat interval in minutes
                TimeUnit.MINUTES
            )
                .build()

            workManager?.enqueue(workRequest)*/
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

    private fun setProgressBarToYellow() {
        val color = resources.getColor(R.color.colorAccent)
        //binding.progressBar.getIndeterminateDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        //binding.progressBar.progress = 360
    }

    private fun setProgressBarToRed() {
        val color = resources.getColor(R.color.red)
       // binding.progressBar.getIndeterminateDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);

    }

    private fun setProgressBarToGrey() {
        val color = resources.getColor(R.color.light_grey)
        //binding.progressBar.getIndeterminateDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
       // val progress = 50 // Change this value as needed
        //binding.progressBar.setProgress(100)
    }

    private fun buttonIsEnabled() {
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        binding.startButton.backgroundTintList = ColorStateList.valueOf(colorWhite)
        binding.startButton.setText("Start")
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