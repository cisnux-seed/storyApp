package dev.thorcode.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dev.thorcode.storyapp.R
import dev.thorcode.storyapp.utils.Result
import dev.thorcode.storyapp.data.UserModel
import dev.thorcode.storyapp.data.ViewModelFactory
import dev.thorcode.storyapp.api.LoginUserReq
import dev.thorcode.storyapp.customview.MyButton
import dev.thorcode.storyapp.customview.MyPasswordEditText
import dev.thorcode.storyapp.databinding.FragmentLoginBinding
import dev.thorcode.storyapp.model.HomeViewModel
import dev.thorcode.storyapp.model.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        val textSpan = SpannableString(resources.getString(R.string.dontHaveAccount))
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                textSpan.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    23,
                    31,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                textSpan.setSpan(
                    ForegroundColorSpan(Color.YELLOW),
                    23,
                    31,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        binding.dontHaveAccount.text = textSpan
        binding.passwordEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                @Suppress("DEPRECATION")
                setMyButtonEnable()
                binding.passwordEditTextLayout.isPasswordVisibilityToggleEnabled = binding.passwordEditText.error == null
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.loginButton.setOnClickListener {
            setupAction()
        }

        binding.dontHaveAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        playAnimation()
    }

    private fun setupAction() {
        showLoading(true)
        val data = LoginUserReq(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
        loginViewModel.login(data).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    showLoading(false)
                    val resp = result.data
                    lifecycleScope.launch {
                        loginViewModel.saveUser(
                            UserModel(
                            resp.loginResult.name,
                            resp.loginResult.token,
                            true
                        )
                        )
                        val navigateToHome = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(navigateToHome)
                    }
                }
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
                else -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setMyButtonEnable() {
        val emailText = binding.emailEditText.text
        val passwordText = binding.passwordEditText.text
        binding.loginButton.isEnabled = emailText.toString().isNotEmpty() && passwordText.toString().length >= 8
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val loginImage = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA,1F).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1F).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1F).setDuration(500)
        val emailTextInput = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val passwordTextInput = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1F).setDuration(500)
        val haveAccount = ObjectAnimator.ofFloat(binding.dontHaveAccount, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                loginImage,
                title,
                message,
                emailTextInput,
                passwordTextInput,
                loginButton,
                haveAccount
            )
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}