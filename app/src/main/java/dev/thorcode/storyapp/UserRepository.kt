package dev.thorcode.storyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import dev.thorcode.storyapp.api.*


class UserRepository(private val userPreferences: UserPreference, private val apiService: ApiService,) {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterUserRes>> = liveData {
        try {
            val response = apiService.register(RegisterUserReq(name, email, password))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(loginUserReq: LoginUserReq): LiveData<Result<LoginUserRes>> = liveData {
        try {
            val response = apiService.login(loginUserReq)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUser(): LiveData<UserModel> {
        return userPreferences.getUser().asLiveData()
    }

    suspend fun saveUser(user: UserModel) {
        userPreferences.saveUser(user)
    }

    suspend fun login() {
        userPreferences.login()
    }

    suspend fun logout() {
        userPreferences.logout()
    }
}