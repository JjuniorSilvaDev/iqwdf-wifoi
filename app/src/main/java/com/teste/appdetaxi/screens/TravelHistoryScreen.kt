package com.teste.appdetaxi.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.teste.appdetaxi.databinding.FragmentTravelHistoryScreenBinding
import com.teste.appdetaxi.viewModel.SharedViewModel

class TravelHistoryScreen : Fragment() {

    private var _binding: FragmentTravelHistoryScreenBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return binding.root
    }

}