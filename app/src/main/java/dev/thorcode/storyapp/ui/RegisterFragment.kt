package dev.thorcode.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dev.thorcode.storyapp.R
import dev.thorcode.storyapp.utils.Result
import dev.thorcode.storyapp.data.ViewModelFactory
import dev.thorcode.storyapp.databinding.FragmentRegisterBinding
import dev.thorcode.storyapp.model.RegisterViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        registerViewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        val textSpan = SpannableString(resources.getString(R.string.haveAccount))
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                textSpan.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    25,
                    30,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                textSpan.setSpan(
                    ForegroundColorSpan(Color.YELLOW),
                    25,
                    30,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        binding.haveAccount.text = textSpan
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                @Suppress("DEPRECATION")
                binding.passwordEditTextLayout.isPasswordVisibilityToggleEnabled = binding.passwordEditText.error == null
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.registerButton.setOnClickListener {
            setupAction()
        }

        binding.haveAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        playAnimation()
    }

    private fun setupAction() {
        showLoading(true)
        val name = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        registerViewModel.register(name, email, password).observe(viewLifecycleOwner){ result ->
            when {
                name.isEmpty() -> {
                    binding.usernameEditText.error = "Please input your username"
                }
                email.isEmpty() -> {
                    binding.emailEditText.error = "Please input your email"
                }
                password.isEmpty() -> {
                    binding.passwordEditText.error = "Please input your password"
                }
                else -> {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            AlertDialog.Builder(requireContext()).apply {
                                setTitle(getString(R.string.yeahh))
                                setMessage(getString(R.string.created_account_success))
                                setPositiveButton("OK"){ _, _ ->
                                    findNavController().navigate(R.id.loginFragment)
                                }
                                create()
                                show()
                            }
                        }
                        is Result.Loading -> showLoading(true)
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            showLoading(false)
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        val registerImage = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA,1F).setDuration(500)
        val titleTVReg = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA,1F).setDuration(500)
        val usernameInputText = ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val emailInputReg = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val passwordInputReg = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1F).setDuration(500)
        val alreadyHaveAccount = ObjectAnimator.ofFloat(binding.haveAccount, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                registerImage,
                titleTVReg,
                usernameInputText,
                emailInputReg,
                passwordInputReg,
                registerButton,
                alreadyHaveAccount
            )
        }.start()
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}