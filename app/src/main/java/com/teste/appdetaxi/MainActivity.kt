package com.teste.appdetaxi

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.teste.appdetaxi.databinding.ActivityMainBinding
import com.teste.appdetaxi.screens.SelectDriverScreen
import com.teste.appdetaxi.screens.RideFormularyScreen

class MainActivity : AppCompatActivity(),
    RideFormularyScreen.RideFormularyToMainActivityInteraction,
    SelectDriverScreen.ChooseDriverToMainActivityInteraction {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: FragmentContainerView
    private var pageName = "Ride Formulary"
    private lateinit var screenLoad: View
    private lateinit var loadProgressBar: ProgressBar
    private lateinit var waitMessage: TextView
    private lateinit var loadingMessageBackground: View
    private lateinit var homeButton: View
    private lateinit var historyButton: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareBinding()

        prepareCallbackButton()

        inflateItems()

    }

    private fun prepareBinding() {
        binding.apply {
            navHostFragment = screenNavHostFragment
            screenLoad = screenLoadFragment
            loadProgressBar = loadProgressBarFragment
            waitMessage = waitMessageFragment
            loadingMessageBackground = loadingMessageBackgroundFragment
            homeButton = homeButtonActivity
            historyButton = historyButtonActivity
        }
    }

    private fun inflateItems() {
        screenLoad.setOnClickListener {

        }

        homeButton.setOnClickListener {
            goToHome()
        }

        historyButton.setOnClickListener {
            goToHistory()
        }
    }

    private fun prepareCallbackButton() {
        val callback = object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (pageName != "Ride Formulary") {
                    goToHome()
                } else {
                    finish()
                }
            }

        }
        this.onBackPressedDispatcher.addCallback(this,callback)
    }

    private fun goToHome() {

        when (pageName) {

            "Ride Formulary" -> {
                rideFormularySelf()
            }

            "Choose Driver" -> {
                chooseDriverToRideFormulary()
            }

            "Travel History" -> {
                travelHistoryToRideFormulary()
            }

        }

    }

    private fun goToHistory() {

        when (pageName) {

            "Ride Formulary" -> {
                rideFormularyToTravelHistory()
            }

            "Choose Driver" -> {
                chooseDriverToTravelHistory()
            }

            "Travel History" -> {
                travelHistorySelf()
            }

        }

    }

    private fun showLoadScreen() {
        screenLoad.visibility = View.VISIBLE
        loadProgressBar.visibility = View.VISIBLE
        waitMessage.visibility = View.VISIBLE
        loadingMessageBackground.visibility = View.VISIBLE
    }

    private fun hideLoadScreen() {
        screenLoad.visibility = View.INVISIBLE
        loadProgressBar.visibility = View.INVISIBLE
        waitMessage.visibility = View.INVISIBLE
        loadingMessageBackground.visibility = View.INVISIBLE
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

    private fun chooseDriverToRideFormulary() {
        pageName = "Ride Formulary"

        navHostFragment.findNavController()
            .navigate(R.id.action_chooseDriverScreen_to_rideFormularyScreen)
    }

    private fun chooseDriverToTravelHistory() {
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

    override fun callHideLoadScreen() {
        hideLoadScreen()
    }

    override fun callTransactionToTravelHistory() {
        chooseDriverToTravelHistory()
    }

    override fun callShowLoadScreen() {
        showLoadScreen()
    }
}