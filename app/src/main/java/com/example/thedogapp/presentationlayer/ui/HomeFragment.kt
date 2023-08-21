package com.example.thedogapp.presentationlayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thedogapp.R
import com.example.thedogapp.databinding.FragmentHomeBinding
import com.example.thedogapp.presentationlayer.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var dogListAdapter: DogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dogListAdapter = DogListAdapter(this)
        bindAdapter()

        handleUnsortedList()

        binding.changeViewButton.setOnCheckedChangeListener { _, isChecked ->
            dogListAdapter.setListViewMode(!isChecked)
            viewModel.isListView = !isChecked

            if (isChecked) {
                binding.dogsRecyclerView.layoutManager = GridLayoutManager(context, 2)
            } else {
                binding.dogsRecyclerView.layoutManager = LinearLayoutManager(context)
            }
        }

        binding.sortListButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                handleSortedList()
            } else {
                handleUnsortedList()
            }
        }
        return root
    }

    private fun handleSortedList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getDogsSorted().collectLatest {
                        dogListAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun handleUnsortedList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getDogs().collectLatest {
                        dogListAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun bindAdapter() {
        with(binding.dogsRecyclerView) {
            adapter = dogListAdapter.apply {
                handleLoadState(this)
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun handleLoadState(adapter: DogListAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is LoadState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.dogsRecyclerView.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.something_went_wrong_please_try_again_later),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        else -> {
                            binding.dogsRecyclerView.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: DogUiModel) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationDetails(item)
        findNavController().navigate(action)
    }
}