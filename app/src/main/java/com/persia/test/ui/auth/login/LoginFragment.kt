package com.persia.test.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.persia.test.R
import com.persia.test.databinding.FragmentLoginBinding
import com.persia.test.ui.panel.PanelActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val inputMobile = binding.etPhoneNumber
        val inputPassword = binding.etPassword
        val btnLogin = binding.btnLogin
        val progressBar = binding.loginProgressBar

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                btnLogin.isEnabled = loginFormState.isDataValid
                loginFormState.mobileError?.let {
                    binding.loginFormMobileLayout.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    binding.loginFormPasswordLayout.error = getString(it)
                }
            })

        loginViewModel.loginRequestStatus.observe(viewLifecycleOwner,
            Observer { status ->
                Timber.i("request state: ${status}")
                when (status) {
                    LoginRequestStatus.LOADING -> progressBar.visibility = View.VISIBLE
                    else -> progressBar.visibility = View.GONE
                }
            })

        loginViewModel.loginResponse.observe(viewLifecycleOwner,
            Observer { response ->
                Timber.i("got response: $response")
                goToPanelActivity()
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    inputMobile.text.toString(),
                    inputPassword.text.toString()
                )
            }
        }

        inputMobile.addTextChangedListener(afterTextChangedListener)
        inputPassword.addTextChangedListener(afterTextChangedListener)
        btnLogin.setOnClickListener {
            // progressBar.visibility = View.VISIBLE
            loginViewModel.login()
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    private fun goToPanelActivity() {
        activity?.let {
            val intent = Intent(it, PanelActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
            it.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}