package com.example.gpstracker.ui.track

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.base.BaseFragment
import com.example.gpstracker.databinding.FragmentForgetPasswordBinding
import com.example.gpstracker.databinding.FragmentSignInBinding
import com.example.gpstracker.databinding.FragmentTrackBinding
import com.example.gpstracker.di.ViewModelFactory
import com.example.gpstracker.ui.track.viewmodel.TrackViewModel
import com.example.gpstracker.utils.TrackerState
import javax.inject.Inject

class TrackFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentTrackBinding

    @JvmField
    @Inject
    var trackViewModel: TrackViewModel? = null

    override fun init() {
        (requireContext().applicationContext as App).appComponent.inject(this)
        trackViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(TrackViewModel::class.java)

         }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackBinding.inflate(inflater, container, false)

        startButtonClicket()



        return binding.root
    }

    private fun startButtonClicket() {


       binding.startButton.setOnClickListener {
           binding.statefulCircleView.setState(TrackerState.ON)

           val statefulCircleView = binding.statefulCircleView
           statefulCircleView.setProgressBarColor(resources.getColor(R.color.colorAccent))


           //binding.progressBar5.setState(TrackerState.OFF)


           /*            gpsStatusOn()
                       buttonIsDisabled()*/
        }
    }

/*    private fun gpsStatusOff(){
        binding.roundImageView.setBackgroundResource(R.drawable.grey_pin)


    }
    private fun gpsStatusOn(){
        binding.roundImageView.setBackgroundResource(R.drawable.round_image_clicked)


    }

    private fun gpsStatusLocationNotFound(){
        binding.roundImageView.setBackgroundResource(R.drawable.pin_exclamation_mark)

    }*/

    private fun buttonIsDisabled(){
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        //binding.startButton.backgroundTintList = ColorStateList.valueOf(colorWhite)

        binding.startButton.background.setColorFilter(colorWhite, PorterDuff.Mode.SRC_ATOP)

        binding.startButton.invalidate()


        binding.startButton.setText("Stop")
    }

    private fun buttonIsEnabled(){
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        binding.startButton.backgroundTintList = ColorStateList.valueOf(colorWhite)
        binding.startButton.setText("Start")
    }


}