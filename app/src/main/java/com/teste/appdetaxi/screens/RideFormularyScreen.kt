package com.teste.appdetaxi.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teste.appdetaxi.databinding.FragmentRideFormularyScreenBinding

class RideFormularyScreen : Fragment() {

    private var _binding: FragmentRideFormularyScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRideFormularyScreenBinding.inflate(inflater, container, false)



        return binding.root
    }

}