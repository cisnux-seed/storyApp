package dev.thorcode.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import dev.thorcode.storyapp.utils.Result
import dev.thorcode.storyapp.api.*
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody


class UserRepository(private val userPreferences: UserPreference, private val apiService: ApiService) {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterUserRes>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(RegisterUserReq(name, email, password))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

     fun login(loginUserReq: LoginUserReq): LiveData<Result<LoginUserRes>> = liveData {
         emit(Result.Loading)
         try {
            val response = apiService.login(loginUserReq)
            emit(Result.Success(response))
         } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
         }
    }

    fun getAllStories(): LiveData<List<ListStory>> = liveData {
        val token = "Bearer ${userPreferences.getUser().first().token}"
        if (token.isEmpty()) {
            emit(emptyList())
        } else {
            try {
                emit(apiService.getAllStories(token).listStory)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    suspend fun getDetailStory(id: String): DetailStory? {
        val token = "Bearer ${userPreferences.getUser().first().token}"
        if (token.isEmpty()) {
            return null
        }
        return try {
            apiService.getDetailStory(id, token)
        } catch (e: Exception) {
            Log.e(UserRepository::class.simpleName, "Error getting detail story: ${e.message}", e)
            null
        }
    }

    fun addStory(imageMultipart: MultipartBody.Part, description: RequestBody)= liveData {
        try {
            emit(Result.Loading)
            val token = "Bearer ${userPreferences.getUser().first().token}"
            apiService.addStory(token, imageMultipart, description)
            emit(Result.Success(null))
        } catch (e: Exception){
            emit(Result.Error(e.message ?: ""))
        }
    }

    fun getDataUser(): LiveData<UserModel> {
        return userPreferences.getUser().asLiveData()
    }

    suspend fun saveUser(user: UserModel) {
        userPreferences.saveUser(user)
    }

    suspend fun logout() {
        userPreferences.logout()
    }
}