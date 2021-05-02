package ru.andrewkir.hse_mooc.flows.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAuthBinding.inflate(inflater, container, false)

        binding.alreadyHaveAnAccount.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.auth_to_login)
        }

        binding.createAccountButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.auth_to_register)
        }

        return binding.root
    }
}