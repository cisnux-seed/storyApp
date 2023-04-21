package dev.thorcode.storyapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thorcode.storyapp.UserModel
import dev.thorcode.storyapp.UserPreference
import dev.thorcode.storyapp.UserRepository
import dev.thorcode.storyapp.api.LoginUserReq
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(loginUserReq: LoginUserReq) {
        userRepository.login(loginUserReq)
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}