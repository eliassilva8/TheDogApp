package com.example.thedogapp.presentationlayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thedogapp.databinding.FragmentHomeBinding
import com.example.thedogapp.presentationlayer.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dogListAdapter = DogListAdapter()
        bindAdapter(dogListAdapter)

        handleUnsortedList(dogListAdapter)

        binding.changeViewButton.setOnCheckedChangeListener { _, isChecked ->
            dogListAdapter.setListViewMode(!isChecked)

            if (isChecked) {
                binding.dogsRecyclerView.layoutManager = GridLayoutManager(context, 2)
            } else {
                binding.dogsRecyclerView.layoutManager = LinearLayoutManager(context)
            }
        }

        binding.sortListButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                handleSortedList(dogListAdapter)
            } else {
                handleUnsortedList(dogListAdapter)
            }
        }
        return root
    }

    private fun handleSortedList(dogListAdapter: DogListAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.itemsSorted.collectLatest {
                        dogListAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun handleUnsortedList(dogListAdapter: DogListAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.items.collectLatest {
                        dogListAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun bindAdapter(dogListAdapter: DogListAdapter) {
        with(binding.dogsRecyclerView) {
            adapter = dogListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, 0))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}