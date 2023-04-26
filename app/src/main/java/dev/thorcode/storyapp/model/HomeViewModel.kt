package dev.thorcode.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dev.thorcode.storyapp.data.UserRepository
import dev.thorcode.storyapp.api.ListStory

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    val isLogin: LiveData<Boolean> get() = repository.getDataUser().map {
        it.isLogin
    }

    fun getAllStories(): LiveData<List<ListStory>> =
        repository.getAllStories()

    suspend fun logout() = repository.logout()
}