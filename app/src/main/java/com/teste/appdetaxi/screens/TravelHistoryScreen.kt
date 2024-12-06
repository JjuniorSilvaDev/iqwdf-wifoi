package com.teste.appdetaxi.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teste.appdetaxi.R
import com.teste.appdetaxi.databinding.FragmentTravelHistoryScreenBinding

class TravelHistoryScreen : Fragment() {

    private var _binding: FragmentTravelHistoryScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return binding.root
    }

}