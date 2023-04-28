package dev.thorcode.storyapp.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.thorcode.storyapp.*
import dev.thorcode.storyapp.data.ViewModelFactory
import dev.thorcode.storyapp.databinding.FragmentAddStoryBinding
import dev.thorcode.storyapp.model.AddStoryViewModel
import dev.thorcode.storyapp.utils.Result
import dev.thorcode.storyapp.utils.createCustomTempFile
import dev.thorcode.storyapp.utils.reduceFileImage
import dev.thorcode.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var uploadFile: File? = null
    private lateinit var currentPhotoPath: String
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        addStoryViewModel = ViewModelProvider(this, viewModelFactory)[AddStoryViewModel::class.java]

        binding.btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED )
                startTakePhoto()
            else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            if(uploadFile != null && binding.etAddStory.text.isNotEmpty()){
                val file = reduceFileImage(uploadFile as File)

                val description = binding.etAddStory.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                addStoryViewModel.addStory(imageMultipart, description).observe(viewLifecycleOwner){
                    if(it is Result.Success)
                        findNavController().navigateUp()
                }
                Toast.makeText(requireContext(), "Upload Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.message_isempty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startTakePhoto() {
        val intentToCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(intentToCamera)

        createCustomTempFile(requireContext()).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "dev.thorcode.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intentToCamera)
        }
    }

    private fun startGallery() {
        val intentToGallery = Intent()
        intentToGallery.action = ACTION_GET_CONTENT
        intentToGallery.type = "image/*"
        val chooser = Intent.createChooser(intentToGallery, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK) {
            @Suppress("DEPRECATION")
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            binding.imageAddStory.setImageBitmap(imageBitmap)

            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                uploadFile = myFile
                binding.imageAddStory.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg = it.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireActivity())
                uploadFile = myFile
                binding.imageAddStory.setImageURI(uri)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startTakePhoto()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.camera_access_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
