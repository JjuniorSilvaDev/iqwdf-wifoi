package com.teste.appdetaxi

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.teste.appdetaxi.databinding.ActivityMainBinding
import com.teste.appdetaxi.screens.RideFormularyScreen

class MainActivity : AppCompatActivity(),
    RideFormularyScreen.RideFormularyToMainActivityInteraction {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: FragmentContainerView
    private var pageName = "Ride Formulary"
    private lateinit var screenLoad: View
    private lateinit var loadProgressBar: ProgressBar
    private lateinit var waitMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareBinding()

    }

    private fun prepareBinding() {
        binding.apply {
            navHostFragment = screenNavHostFragment
            screenLoad = screenLoadFragment
            loadProgressBar = loadProgressBarFragment
            waitMessage = waitMessageFragment
        }
    }

    private fun showLoadScreen() {
        screenLoad.visibility = View.VISIBLE
        loadProgressBar.visibility = View.VISIBLE
        waitMessage.visibility = View.VISIBLE
    }

    private fun hideLoadScreen() {
        screenLoad.visibility = View.INVISIBLE
        loadProgressBar.visibility = View.INVISIBLE
        waitMessage.visibility = View.INVISIBLE
    }

    private fun rideFormularySelf() {
        navHostFragment.findNavController()
            .navigate(R.id.action_rideFormularyScreen_self)
    }

    private fun rideFormularyToChooseDriver() {
        pageName = "Choose Driver"

        navHostFragment.findNavController()
            .navigate(R.id.action_rideFormularyScreen_to_chooseDriverScreen)
    }

    private fun rideFormularyToTravelHistory() {
        pageName = "Travel History"

        navHostFragment.findNavController()
            .navigate(R.id.action_rideFormularyScreen_to_travelHistoryScreen)
    }

    private fun chooseDriverSelf() {
        navHostFragment.findNavController()
            .navigate(R.id.action_chooseDriverScreen_self)
    }

    private fun chooseDriverToRideFormulary() {
        pageName = "Ride Formulary"

        navHostFragment.findNavController()
            .navigate(R.id.action_chooseDriverScreen_to_rideFormularyScreen)
    }

    private fun chooseDriverTravelHistory() {
        pageName = "Travel History"

        navHostFragment.findNavController()
            .navigate(R.id.action_chooseDriverScreen_to_travelHistoryScreen)
    }

    private fun travelHistorySelf() {
        navHostFragment.findNavController()
            .navigate(R.id.action_travelHistoryScreen_self)
    }

    private fun travelHistoryToRideFormulary() {
        pageName = "Ride Formulary"

        navHostFragment.findNavController()
            .navigate(R.id.action_travelHistoryScreen_to_rideFormularyScreen)
    }

    override fun callTransactionToChooseDriver() {
        rideFormularyToChooseDriver()
    }
}