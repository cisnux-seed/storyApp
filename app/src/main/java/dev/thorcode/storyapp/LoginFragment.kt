package dev.thorcode.storyapp

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import dev.thorcode.storyapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
                binding.passwordEditTextLayout.isPasswordVisibilityToggleEnabled = binding.passwordEditText.error == null
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.dontHaveAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}