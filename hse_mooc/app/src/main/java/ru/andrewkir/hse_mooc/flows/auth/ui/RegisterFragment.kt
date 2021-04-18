package ru.andrewkir.hse_mooc.flows.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.startActivityClearBackStack
import ru.andrewkir.hse_mooc.flows.courses.ui.CoursesActivity
import ru.andrewkir.hse_mooc.databinding.FragmentRegisterBinding
import ru.andrewkir.hse_mooc.flows.auth.AuthRepository
import ru.andrewkir.hse_mooc.flows.auth.LoginViewModel
import ru.andrewkir.hse_mooc.flows.auth.RegisterViewModel
import ru.andrewkir.hse_mooc.network.api.AuthApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse

class RegisterFragment :
    BaseFragment<RegisterViewModel, AuthRepository, FragmentRegisterBinding>() {

    //TODO SAVE EDIT TEXT STATE

    override fun provideViewModelClass(): Class<RegisterViewModel> = RegisterViewModel::class.java

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
    ): FragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("EDIT_TEXT_EMAIL", bind.emailTextInput.editText!!.text.toString())
        outState.putString("EDIT_TEXT_USERNAME", bind.loginTextInput.editText!!.text.toString())
        outState.putString("EDIT_TEXT_PASSWORD", bind.passwordTextInput.editText!!.text.toString())
        outState.putString(
            "EDIT_TEXT_PASSWORD2",
            bind.passwordSecondTextInput.editText!!.text.toString()
        )
    }

    private fun restoreSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            bind.emailTextInput.editText!!.setText(
                savedInstanceState.getString(
                    "EDIT_TEXT_EMAIL",
                    ""
                )
            )

            bind.loginTextInput.editText!!.setText(
                savedInstanceState.getString(
                    "EDIT_TEXT_USERNAME",
                    ""
                )
            )
            bind.passwordTextInput.editText!!.setText(
                savedInstanceState.getString(
                    "EDIT_TEXT_PASSWORD",
                    ""
                )
            )

            bind.passwordSecondTextInput.editText!!.setText(
                savedInstanceState.getString(
                    "EDIT_TEXT_PASSWORD2",
                    ""
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreSavedState(savedInstanceState)

        subscribeToLoading()
        setupButtons()
        adjustButtonToText()
        setupInputs()
        subscribeToLoginResult()

        bind.progressBar.visibility = View.INVISIBLE
    }

    private fun adjustButtonToText() {
        bind.apply {
            registerButton.isEnabled = emailTextInput.editText!!.text.isNotBlank() &&
                    loginTextInput.editText!!.text.isNotBlank() &&
                    passwordTextInput.editText!!.text.isNotBlank() &&
                    passwordSecondTextInput.editText!!.text.isNotBlank() &&
                    passwordTextInput.editText!!.text.toString() == passwordSecondTextInput.editText!!.text.toString()

        }
    }

    private fun setupInputs() {
        bind.apply {
            emailTextInput.editText!!.addTextChangedListener { adjustButtonToText() }
            loginTextInput.editText!!.addTextChangedListener { adjustButtonToText() }
            passwordTextInput.editText!!.addTextChangedListener { adjustButtonToText() }
            passwordSecondTextInput.editText!!.addTextChangedListener { adjustButtonToText() }

            passwordSecondTextInput.editText!!.addTextChangedListener {
                if (passwordTextInput.editText!!.text.toString() != passwordSecondTextInput.editText!!.text.toString()) {
                    passwordSecondTextInput.error = "Пароли не совпадают"
                } else {
                    passwordSecondTextInput.error = ""
                }
            }
        }
    }

    private fun setupButtons() {
        bind.registerButton.setOnClickListener {
            val email = bind.emailTextInput.editText!!.text.toString()
            val login = bind.loginTextInput.editText!!.text.toString()
            val password = bind.passwordTextInput.editText!!.text.toString()
            viewModel.register(email, login, password)
        }

        bind.loginTextView.setOnClickListener {
            Navigation.findNavController(bind.root).navigate(R.id.register_to_login)
        }
    }

    private fun subscribeToLoginResult() {
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApiResponse.OnSuccessResponse -> {
                    Toast.makeText(requireContext(), it.value.string(), Toast.LENGTH_SHORT).show()
                    requireActivity().startActivityClearBackStack(CoursesActivity::class.java)
                }
                is ApiResponse.OnErrorResponse -> {
                    if (it.isNetworkFailure)
                        Toast.makeText(
                            requireContext(),
                            "Проверьте подключение к интернету",
                            Toast.LENGTH_SHORT
                        ).show()
                    else Toast.makeText(requireContext(), it.body?.string(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun subscribeToLoading() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            enableInputsAndButtons(!it)
            bind.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
    }

    private fun enableInputsAndButtons(isEnabled: Boolean) {
        bind.registerButton.isEnabled = isEnabled
        bind.loginTextInput.isEnabled = isEnabled
        bind.emailTextInput.isEnabled = isEnabled
        bind.passwordTextInput.isEnabled = isEnabled
        bind.passwordSecondTextInput.isEnabled = isEnabled
        bind.loginTextView.isEnabled = isEnabled
    }
}