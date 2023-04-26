package dev.thorcode.storyapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dev.thorcode.storyapp.model.DetailStoryViewModel
import dev.thorcode.storyapp.data.ViewModelFactory
import dev.thorcode.storyapp.databinding.FragmentDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailStoryViewModel: DetailStoryViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        detailStoryViewModel = ViewModelProvider(this, viewModelFactory)[DetailStoryViewModel::class.java]

        id = DetailFragmentArgs.fromBundle(arguments as Bundle).id

        detailStoryViewModel.getDetailStory(id)

        detailStoryViewModel.detailStory.observe(viewLifecycleOwner) {
                with(binding){
                    showLoading(false)
                    Glide.with(this@DetailFragment)
                        .load(it.story.photoUrl)
                        .into(ivImageDetail)
                    tvNameDetail.text = it.story.name
                    val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = originalFormat.parse(it.story.createdAt)
                    tvCreatedAtDetail.text = date.let { it?.let { it1 -> targetFormat.format(it1) } }
                    tvDescriptionDetail.text = it.story.description
                }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}