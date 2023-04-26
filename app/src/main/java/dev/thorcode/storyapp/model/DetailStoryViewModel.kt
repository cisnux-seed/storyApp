package dev.thorcode.storyapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thorcode.storyapp.data.UserRepository
import dev.thorcode.storyapp.api.DetailStory
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _detailStory = MutableLiveData<DetailStory>()
    val detailStory: LiveData<DetailStory> = _detailStory

    fun getDetailStory(id: String) = viewModelScope.launch {
        try {
            _detailStory.value = repository.getDetailStory(id)
        } catch (e: Exception) {
            Log.d("DETAIL", e.message.toString())
        }
    }
}