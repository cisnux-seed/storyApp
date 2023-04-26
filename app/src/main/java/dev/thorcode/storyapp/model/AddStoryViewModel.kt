package dev.thorcode.storyapp.model

import androidx.lifecycle.ViewModel
import dev.thorcode.storyapp.data.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val userRepository: UserRepository):ViewModel() {
    fun addStory(imageMultipart: MultipartBody.Part, description: RequestBody) = userRepository.addStory(imageMultipart, description)
}
