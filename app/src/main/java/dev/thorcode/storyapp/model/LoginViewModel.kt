package dev.thorcode.storyapp.model

import androidx.lifecycle.ViewModel
import dev.thorcode.storyapp.data.UserModel
import dev.thorcode.storyapp.data.UserRepository
import dev.thorcode.storyapp.api.LoginUserReq

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(loginUserReq: LoginUserReq) =
        userRepository.login(loginUserReq)

    suspend fun saveUser(user: UserModel) =
        userRepository.saveUser(user)
}