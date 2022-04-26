package com.dontsu.composereaderapp.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dontsu.composereaderapp.R
import com.dontsu.composereaderapp.components.EmailInput
import com.dontsu.composereaderapp.components.PasswordInput
import com.dontsu.composereaderapp.components.ReaderLogoText

@ExperimentalComposeUiApi
@Composable
fun ReaderLoginScreen(navController: NavHostController) {
    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReaderLogoText()
            if (showLoginForm.value) {
                UserForm() { email, password ->
                    // todo : Firebase login
                }
            } else {
                UserForm(isCreateAccount = true) { email, password ->
                    // todo : create Firebase account
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val text = if (showLoginForm.value) "Sign up" else "Login"
            Text(text = "New User?")
            Text(
                modifier = Modifier
                    .clickable {
                        showLoginForm.value = !showLoginForm.value
                    }
                    .padding(start = 5.dp),
                fontWeight = FontWeight.Bold,
                text = text,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }

}

@ExperimentalComposeUiApi
@Preview
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { _, _ -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /** Notice */
        if (isCreateAccount) Text(
            modifier = Modifier.padding(4.dp),
            text = stringResource(id = R.string.create_account)
        )

        /** Email */
        EmailInput(
            emailState = email,
            enabled = !loading,
            keyboardActions = KeyboardActions {
                passwordFocusRequest.requestFocus() // email을 다 적으면 password field에 포커스 가도록 하기
            }
        )

        /** Password */
        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            keyboardActions = KeyboardActions {
                if (!valid) return@KeyboardActions
                keyboardController?.hide()
            }
        )

        /** Login */
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid,
        ) {
            onDone(email.value.trim(), password.value.trim())
        }
    }

}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClicked: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        onClick = onClicked,
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(
            modifier = Modifier.padding(5.dp),
            text = textId
        )
    }
}
