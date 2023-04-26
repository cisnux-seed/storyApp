package dev.thorcode.storyapp.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.thorcode.storyapp.adapter.ListStoryAdapter
import dev.thorcode.storyapp.R
import dev.thorcode.storyapp.data.ViewModelFactory
import dev.thorcode.storyapp.databinding.FragmentHomeBinding
import dev.thorcode.storyapp.model.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeVIewModel: HomeViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var adapter: ListStoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(
            ContextCompat.getColor(requireContext(), R.color.bluePrimary)))
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory.getInstance(requireContext())
        homeVIewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        homeVIewModel.isLogin.observe(viewLifecycleOwner){
            if(!it){
                val navigateToLogin = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                findNavController().navigate(navigateToLogin)
            } else
                binding.root.visibility = View.VISIBLE
        }

        adapter = ListStoryAdapter(object : ListStoryAdapter.OnItemClickListener {
            override fun onClickListener(id: String) {
                val navigateToDetail = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
                findNavController().navigate(navigateToDetail)
            }
        })

        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@HomeFragment.adapter
        }

        binding.fabAdd.setOnClickListener {
            val navigateToAddStory = HomeFragmentDirections.actionHomeFragmentToAddStoryFragment()
            findNavController().navigate(navigateToAddStory)
        }

        setupAction()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        @Suppress("DEPRECATION")
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_menu -> {
                lifecycleScope.launch {
                    homeVIewModel.logout()
                }
            }
        }
        @Suppress("DEPRECATION")
        return super.onOptionsItemSelected(item)
    }

    private fun setupAction() {
        homeVIewModel.getAllStories().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}