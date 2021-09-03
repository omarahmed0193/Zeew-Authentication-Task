package com.zeew.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zeew.R
import com.zeew.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)

        initViews(binding)
        initObservers()
    }

    private fun initViews(binding: FragmentHomeBinding) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun initObservers() {
        viewModel.navigateToLogin.observe(viewLifecycleOwner, { navigateToLogin() })
    }

    private fun navigateToLogin() {
        if (findNavController().currentDestination?.id != R.id.HomeFragment) return
        findNavController().navigate(HomeFragmentDirections.navigateToLogin())
    }
}