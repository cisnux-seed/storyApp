package dev.thorcode.storyapp

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import dev.thorcode.storyapp.databinding.FragmentRegisterBinding
import dev.thorcode.storyapp.model.RegisterViewModel
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

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

        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        registerViewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

        binding.registerButton.setOnClickListener {
            setupAction()
        }

        binding.haveAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun setupAction() {
        val name = binding.usernameEditText.toString()
        val email = binding.emailEditText.toString()
        val password = binding.passwordEditText.toString()
        registerViewModel.register(name, email, password).observe(viewLifecycleOwner){ result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Log.d("TAG_REGIS", result.error.toString())
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}