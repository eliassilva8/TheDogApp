package com.example.thedogapp.presentationlayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
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
import com.example.thedogapp.datalayer.ApiEmptyResponseException
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
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.noDogs.visibility = View.GONE
                        }
                        is LoadState.Error -> {
                            val error = (loadState.refresh as LoadState.Error).error
                            if (error is ApiEmptyResponseException) {
                                binding.noDogs.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(requireContext(), getString(R.string.something_went_wrong_please_try_again_later), Toast.LENGTH_SHORT)
                                    .show()
                            }
                            binding.progressBar.visibility = View.GONE
                            binding.dogsRecyclerView.visibility = View.GONE
                        }
                        else -> {
                            binding.noDogs.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            binding.dogsRecyclerView.visibility = View.VISIBLE
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
        val action = SearchFragmentDirections.actionNavigationSearchToNavigationDetails(item)
        findNavController().navigate(action)
    }
}