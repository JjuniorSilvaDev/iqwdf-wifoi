package com.teste.appdetaxi.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.teste.appdetaxi.MainActivity
import com.teste.appdetaxi.databinding.FragmentRideFormularyScreenBinding

class RideFormularyScreen : Fragment() {

    private var _binding: FragmentRideFormularyScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var buttonSearchRide : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRideFormularyScreenBinding.inflate(inflater, container, false)

        prepareBinding()

        inflateItems()

        return binding.root
    }

    private fun prepareBinding() {
        binding.apply {
            buttonSearchRide = buttonSearchRideFragment
        }
    }

    private fun inflateItems() {
        buttonSearchRide.setOnClickListener {
            (context as MainActivity).callTransactionToChooseDriver()
        }
    }

    interface RideFormularyToMainActivityInteraction {
        fun callTransactionToChooseDriver()
    }

}