package com.zeew.ui.signup

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zeew.R
import com.zeew.databinding.FragmentSignupBinding
import com.zeew.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {
    private val viewModel: SignupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignupBinding.bind(view)

        initViews(binding)

        initObservers(binding)
    }

    private fun initViews(binding: FragmentSignupBinding) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.firstNameTextInput.addTextChangedListener {
            binding.firstNameTextInputLayout.isErrorEnabled = false
            viewModel.onFirstNameChanged(it.toString())
        }

        binding.lastNameTextInput.addTextChangedListener {
            binding.lastNameTextInputLayout.isErrorEnabled = false
            viewModel.onLastNameChanged(it.toString())
        }

        binding.emailTextInput.addTextChangedListener {
            binding.emailTextInputLayout.isErrorEnabled = false
            viewModel.onEmailChanged(it.toString())
        }

        binding.passwordTextInput.addTextChangedListener {
            binding.passwordTextInputLayout.isErrorEnabled = false
            viewModel.onPasswordChanged(it.toString())
        }

        binding.confirmPasswordTextInput.addTextChangedListener {
            binding.confirmPasswordTextInputLayout.isErrorEnabled = false
            viewModel.onConfirmPasswordChanged(it.toString())
        }

        binding.phoneNumberTextInput.addTextChangedListener {
            binding.phoneNumberInputLayout.isErrorEnabled = false
            viewModel.onPhoneNumberChanged(it.toString())
        }

        binding.referralCodeTextInput.addTextChangedListener {
            viewModel.onReferralCodeChanged(it.toString())
        }

    }

    private fun initObservers(binding: FragmentSignupBinding) {
        viewModel.shouldShowFirstNameValidationError.observe(viewLifecycleOwner, {
            binding.firstNameTextInputLayout.error = getString(R.string.error_message_name)
        })
        viewModel.shouldShowLastNameValidationError.observe(viewLifecycleOwner, {
            binding.lastNameTextInputLayout.error = getString(R.string.error_message_name)
        })
        viewModel.shouldShowEmailValidationError.observe(viewLifecycleOwner, {
            binding.emailTextInputLayout.error = getString(R.string.error_message_email)
        })
        viewModel.shouldShowPasswordValidationError.observe(viewLifecycleOwner, {
            binding.passwordTextInputLayout.error = getString(R.string.error_message_password)
        })
        viewModel.shouldShowConfirmPasswordValidationError.observe(viewLifecycleOwner, {
            binding.confirmPasswordTextInputLayout.error =
                getString(R.string.error_message_confirm_password)
        })
        viewModel.shouldShowPhoneNumberValidationError.observe(viewLifecycleOwner, {
            binding.phoneNumberInputLayout.error = getString(R.string.error_message_phone_number)
        })

        viewModel.navigateToHome.observe(viewLifecycleOwner, { navigateToHome() })

        viewModel.signupResource.observe(viewLifecycleOwner, {
            it?.let { status ->
                if (status is Resource.Error) {
                    it.errorMessage?.let { message -> showErrorSnackbar(message) }
                }
            }
        })
    }

    private fun showErrorSnackbar(message: String) {
        view?.let { viewToAttach ->
            Snackbar.make(viewToAttach, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome() {
        if (findNavController().currentDestination?.id != R.id.SignupFragment) return
        findNavController().navigate(SignupFragmentDirections.navigateToHome())
    }

}