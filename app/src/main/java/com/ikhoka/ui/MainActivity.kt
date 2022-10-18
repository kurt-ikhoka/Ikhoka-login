package com.ikhoka.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.ikhoka.R
import com.ikhoka.core.isError
import com.ikhoka.core.isLoading
import com.ikhoka.databinding.MainActivityBinding
import com.ikhoka.login.LoginViewModel
import com.ikhoka.login.meta.LoginRequest
import com.ikhoka.login.meta.LoginState
import com.ikhoka.login.ui.ErrorComponent
import com.ikhoka.login.ui.LoadingComponent
import com.ikhoka.login.ui.LoginComponent
import com.ikhoka.login.ui.SuccessComponent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val root = binding.root

        //Login component
        val loginFlow =
            viewModel.authState.map { it.data == LoginState.UNAUTHENTICATED }
        LoginComponent(
            this,
            root.findViewById(R.id.login_component),
            OnLogin = {
                viewModel.login(LoginRequest("", ""))
            }
        ).collect(loginFlow, viewModel.authState)

        //loading component
        val loadingFlow = viewModel.authState.map { it.isLoading() }
        LoadingComponent(
            this,
            root.findViewById(R.id.loading_component),
        ).collect(loadingFlow, viewModel.authState)

        //error component
        val errorFlow = viewModel.authState.map { it.isError() }
        ErrorComponent(
            this,
            root.findViewById(R.id.error_component),
            OnRetry = {
                viewModel.login(LoginRequest("", ""))
            }
        ).collect(errorFlow, viewModel.authState)

        //Success component
        val successFlow = viewModel.authState.map { it.data != null && it.data == LoginState.AUTHENTICATED }
        SuccessComponent(
            this,
            root.findViewById(R.id.success_component),
            OnLogOut = {
                viewModel.logout()
            }
        ).collect(successFlow, viewModel.authState)

        setContentView(root)

    }
}