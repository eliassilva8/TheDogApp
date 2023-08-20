package com.example.thedogapp.presentationlayer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thedogapp.R
import com.example.thedogapp.databinding.FragmentSearchBinding
import com.example.thedogapp.presentationlayer.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var dogSearchAdapter: DogSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dogSearchAdapter = DogSearchAdapter(this)
        bindAdapter()

        binding.searchView.queryHint = getString(R.string.search_for_dog_breed)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handleSearchDogs(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return root
    }

    private fun bindAdapter() {
        with(binding.dogsRecyclerView) {
            binding.dogsRecyclerView.adapter = dogSearchAdapter.apply {
                handleLoadState(this)
            }
            binding.dogsRecyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun handleSearchDogs(query: String?) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.searchDogs(query).collectLatest {
                        Log.d("Elias", "collect: $query")
                        dogSearchAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun handleLoadState(adapter: DogSearchAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { loadState ->
                    binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: DogUiModel) {
        val action = SearchFragmentDirections.actionNavigationSearchToNavigationDetails(item)
        findNavController().navigate(action)
    }
}