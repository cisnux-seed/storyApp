package dev.thorcode.storyapp.model

import androidx.lifecycle.ViewModel
import dev.thorcode.storyapp.UserPreference
import dev.thorcode.storyapp.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)
}