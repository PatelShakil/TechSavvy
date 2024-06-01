package com.shakilpatel.techsavvy.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.R
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.nav.AppScreen
import com.shakilpatel.techsavvy.ui.common.ComposeToast
import com.shakilpatel.techsavvy.ui.common.ProgressBarNonCancelable
import com.shakilpatel.techsavvy.ui.theme.TechSavvyTheme

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController) {


        Surface {
            viewModel.student.collectAsState().value.let {
            when(it){
                is Resource.Loading->{
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ProgressBarNonCancelable()
                    }
                }
                is Resource.Failure->{}
                is Resource.Success->{
                    LaunchedEffect(key1 = true) {
                        viewModel.checkStudent(it.result)
                    }
                }
                else->{}
            }
        }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var phoneNo by remember { mutableStateOf("") }
                var otpValue by remember { mutableStateOf("") }


                Image(
                    painter = painterResource(id = R.drawable.ic_login),
                    "",
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Note: Enter your phone no. and you're security pin which can provided by Tech Savvy Classes",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 10.dp)
                )


                OutlinedTextField(
                    value = phoneNo, onValueChange = {
                        phoneNo = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    placeholder = { Text("Enter your phone no.") },
                    label = { Text("Phone No.") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                BasicTextField(value = otpValue, onValueChange = {
                    if (it.length <= 6) {
                        otpValue = it
                    }
                    if (it.length == 6) {
                        if(phoneNo.isNotEmpty() && otpValue.isNotEmpty() && otpValue.length == 6 && phoneNo.length == 10) {
                            viewModel.login(phoneNo, otpValue)
                        }
                    }
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    decorationBox = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(
                                "Security Pin", style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                repeat(6) { index ->
                                    val char = when {
                                        index >= otpValue.length -> ""
                                        else -> otpValue[index].toString()
                                    }

                                    Card(
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            char,
                                            style = MaterialTheme.typography.titleLarge,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .width(40.dp)
                                                .border(
                                                    1.dp,
                                                    if (char.isEmpty()) Color.LightGray else Color.Gray,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .padding(8.dp)
                                                .padding(top = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }
                    }
                )

                Button(
                    onClick = {
                        viewModel.login(phoneNo, otpValue)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .padding(top = 40.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    enabled = phoneNo.isNotEmpty() && otpValue.isNotEmpty() && otpValue.length == 6 && phoneNo.length == 10
                ) {
                    Text("Login")
                }

                viewModel.loginResult.collectAsState().value.let {
                    when(it){
                        is Resource.Loading->{
                            ProgressBarNonCancelable()
                        }
                        is Resource.Success->{
                            LaunchedEffect(key1 = it.result) {
                                navController.navigate(AppScreen.Home.route){
                                    popUpTo(navController.graph.id){
                                        inclusive = true
                                    }
                                }
                            }
                        }
                        is Resource.Failure-> {
                            ComposeToast(msg = it.message)
                            viewModel.resetLoginResult()
                        }
                        else->{}
                    }
                }
            }
        }
}