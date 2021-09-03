package com.zeew.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zeew.R
import com.zeew.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSplashBinding.bind(view)

        initViews(binding)

        initObservers()
    }

    private fun initViews(binding: FragmentSplashBinding) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun initObservers() {
        viewModel.navigateToHome.observe(viewLifecycleOwner, { navigateToHome() })
        viewModel.navigateToLogin.observe(viewLifecycleOwner, { navigateToLogin() })
    }

    private fun navigateToHome() {
        if (findNavController().currentDestination?.id != R.id.SplashFragment) return
        findNavController().navigate(SplashFragmentDirections.navigateToHome())
    }

    private fun navigateToLogin() {
        if (findNavController().currentDestination?.id != R.id.SplashFragment) return
        findNavController().navigate(SplashFragmentDirections.navigateToLogin())
    }
}