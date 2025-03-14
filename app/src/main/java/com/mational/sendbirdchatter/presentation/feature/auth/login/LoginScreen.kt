package com.mational.sendbirdchatter.presentation.feature.auth.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mational.sendbirdchatter.presentation.navigation.Screens
import com.sendbird.uikit.compose.component.CircularProgressIndicator

@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val fillState = uiState as? LoginState.FillFormState
    val errorMessage = fillState?.errorMessage

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        if (uiState is LoginState.FillFormState) {
            Text(
                text = "Sendbird Chatter",
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(92.dp))
            TextField(
                modifier = Modifier.fillMaxWidth().background(Color.Black),
                value = (uiState as LoginState.FillFormState).email,
                onValueChange = { viewModel.onEmailValueChange(it) },
                label = { Text(text = "Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                modifier = Modifier.fillMaxWidth().background(Color.Black),
                value = (uiState as LoginState.FillFormState).password,
                onValueChange = { viewModel.onPasswordValueChange(it) },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    enabled = viewModel.isLoginButtonEnabled(),
                    onClick = {
                        viewModel.onLoginButtonClicked()
                    },
                ) {
                    Text(text = "Login")
                }
                TextButton(
                    onClick = {
                        viewModel.navigateTo(Screens.RegisterRoute.route)
                    }
                ) { Text(text = "Don't have account yet?") }
            }
        } else if (uiState is LoginState.LoadingState) {
            CircularProgressIndicator()
        }
    }
}