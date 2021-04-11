package ru.andrewkir.hse_mooc.flows.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.startActivityClearBackStack
import ru.andrewkir.hse_mooc.flows.courses.ui.CoursesActivity
import ru.andrewkir.hse_mooc.databinding.FragmentLoginBinding
import ru.andrewkir.hse_mooc.flows.auth.AuthRepository
import ru.andrewkir.hse_mooc.flows.auth.AuthViewModel
import ru.andrewkir.hse_mooc.network.api.AuthApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse

class LoginFragment : BaseFragment<AuthViewModel, AuthRepository, FragmentLoginBinding>() {

    override fun provideViewModelClass(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun provideRepository(): AuthRepository =
        AuthRepository(
            apiProvider.provideApi(
                AuthApi::class.java,
                requireContext(),
                null,
                null
            )
        )

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        adjustButtonToText()
        setupInputs()
        subscribeToLoginResult()

        bind.progressBar.visibility = View.INVISIBLE
    }

    private fun adjustButtonToText() {
        bind.apply {
            loginButton.isClickable = loginTextInput.editText!!.text.isNotBlank() &&
                    passwordTextInput.editText!!.text.isNotBlank()
        }
    }

    private fun setupInputs() {
        bind.apply {
            loginTextInput.editText!!.addTextChangedListener { adjustButtonToText() }
            passwordTextInput.editText!!.addTextChangedListener { adjustButtonToText() }
        }
    }

    private fun setupButtons() {
        bind.loginButton.setOnClickListener {
            bind.progressBar.visibility = View.VISIBLE
            val login = bind.loginTextInput.editText?.text.toString()
            val password = bind.passwordTextInput.editText?.text.toString()
            viewModel.login(login, password)
        }

        bind.registerTextView.setOnClickListener {
            Navigation.findNavController(bind.root).navigate(R.id.login_to_register)
        }
    }

    private fun subscribeToLoginResult() {
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            bind.progressBar.visibility = View.INVISIBLE
            when (it) {
                is ApiResponse.OnSuccessResponse -> {
                    Toast.makeText(
                        requireContext(),
                        "${it.value.access_token}\n${it.value.refresh_token}",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    userPrefsManager.saveAccessToken(it.value.access_token)
                    userPrefsManager.saveRefreshToken(it.value.refresh_token)
                    requireActivity().startActivityClearBackStack(CoursesActivity::class.java)
                }
                is ApiResponse.OnErrorResponse -> {
                    Toast.makeText(requireContext(), "Nah", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}