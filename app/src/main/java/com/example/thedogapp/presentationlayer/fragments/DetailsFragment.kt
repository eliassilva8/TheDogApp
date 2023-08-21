package com.example.thedogapp.presentationlayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.thedogapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dog = args.dogUiModel
        binding.breedName.text = dog.name.ifBlank { "--" }
        binding.breedGroup.text = dog.group.ifBlank { "--" }
        binding.origin.text = dog.origin.ifBlank { "--" }
        binding.temperament.text = dog.temperament.ifBlank { "--" }
        binding.dogImage.load(dog.imageUrl)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}