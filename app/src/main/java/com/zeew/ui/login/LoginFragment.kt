package com.zeew.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.zeew.R
import com.zeew.databinding.FragmentLoginBinding
import com.zeew.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login), FacebookCallback<LoginResult>,
    OnCompleteListener<AuthResult> {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleResultLauncher: ActivityResultLauncher<Intent>

    private val loginManager = LoginManager.getInstance()
    private lateinit var callbackManager: CallbackManager

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGoogleSignIn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)

        initViews(binding)

        initObservers(binding)
    }

    private fun initViews(binding: FragmentLoginBinding) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.emailTextInput.addTextChangedListener { email ->
            binding.emailTextInputLayout.isErrorEnabled = false
            viewModel.onEmailChanged(email.toString())
        }
        binding.passwordTextInput.addTextChangedListener { password ->
            binding.passwordTextInputLayout.isErrorEnabled = false
            viewModel.onPasswordChanged(password.toString())
        }

        binding.googleSignInButton.setOnClickListener {
            googleResultLauncher.launch(googleSignInClient.signInIntent)
            viewModel.updateSocialLoginStatus(Resource.Loading())
        }

        binding.fcSignInButton.setOnClickListener {
            initFacebookSignIn()
            viewModel.updateSocialLoginStatus(Resource.Loading())
        }
    }

    private fun initObservers(binding: FragmentLoginBinding) {

        viewModel.shouldShowEmailValidationError.observe(viewLifecycleOwner, {
            binding.emailTextInputLayout.error = getString(R.string.error_message_email)
        })

        viewModel.shouldShowPasswordValidationError.observe(viewLifecycleOwner, {
            binding.passwordTextInputLayout.error = getString(R.string.error_message_password)
        })

        viewModel.loginResource.observe(viewLifecycleOwner, {
            it?.let { status ->
                if (status is Resource.Error) {
                    it.errorMessage?.let { message -> showErrorSnackbar(message) }
                }
            }
        })

        viewModel.navigateToSignup.observe(viewLifecycleOwner, { navigateToSignup() })

        viewModel.navigateToHome.observe(viewLifecycleOwner, { navigateToHome() })
    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        googleResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        e.localizedMessage?.let {
                            showErrorSnackbar(it)
                            viewModel.updateSocialLoginStatus(Resource.Error(it))
                        }
                    }
                }
            }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let { firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) }
    }

    private fun initFacebookSignIn() {
        callbackManager = CallbackManager.Factory.create()
        loginManager.logInWithReadPermissions(this, arrayListOf("email", "public_profile"))
        loginManager.registerCallback(callbackManager, this)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this)
    }

    @Suppress("warnings")
    //https://github.com/facebook/facebook-android-sdk/issues/875
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccess(result: LoginResult) = handleFacebookAccessToken(result.accessToken)

    override fun onCancel() {
        val errorMessage = getString(R.string.error_message_facebook)
        viewModel.updateSocialLoginStatus(Resource.Error(errorMessage))
        showErrorSnackbar(errorMessage)
    }

    override fun onError(error: FacebookException) {
        error.localizedMessage?.let {
            showErrorSnackbar(it)
            viewModel.updateSocialLoginStatus(Resource.Error(it))
        }
    }

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            viewModel.onUsernameAvailable(firebaseAuth.currentUser?.displayName)
            viewModel.updateSocialLoginStatus(Resource.Success(""))
            navigateToHome()
        } else {
            task.exception?.localizedMessage?.let {
                showErrorSnackbar(it)
                viewModel.updateSocialLoginStatus(Resource.Error(it))
            }
        }
    }

    private fun navigateToSignup() {
        if (findNavController().currentDestination?.id != R.id.LoginFragment) return
        findNavController().navigate(LoginFragmentDirections.navigateToSignup())
    }

    private fun navigateToHome() {
        if (findNavController().currentDestination?.id != R.id.LoginFragment) return
        findNavController().navigate(LoginFragmentDirections.navigateToHome())
    }

    private fun showErrorSnackbar(message: String) {
        view?.let { viewToAttach ->
            Snackbar.make(viewToAttach, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}