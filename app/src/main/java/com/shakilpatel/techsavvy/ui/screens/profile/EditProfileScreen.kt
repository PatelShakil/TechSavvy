package com.shakilpatel.techsavvy.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.techsavvy.ui.theme.BackgroundLight
import com.shakilpatel.techsavvy.ui.theme.TextPrimary
import com.shakilpatel.techsavvy.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val currentStudent by authViewModel.currentStudent.collectAsState()
    var name by remember { mutableStateOf(currentStudent?.name ?: "") }
    var phone by remember { mutableStateOf(currentStudent?.phone ?: "") }
    var college by remember { mutableStateOf(currentStudent?.college ?: "") }
    var year by remember { mutableStateOf(currentStudent?.year ?: "") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val firestore = FirebaseFirestore.getInstance()
    LaunchedEffect(true) {
        authViewModel.fetchCurrentStudent()
    }

    LaunchedEffect(currentStudent) {
        currentStudent?.let {
            name = it.name
            phone = it.phone
            college = it.college
            year = it.year
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Update Your Information",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = college,
                            onValueChange = { college = it },
                            label = { Text("College/Institution") },
                            leadingIcon = { Icon(Icons.Default.School, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = year,
                            onValueChange = { year = it },
                            label = { Text("Year of Study") },
                            leadingIcon = { Icon(Icons.Default.Class, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        scope.launch {
                            loading = true
                            try {
                                currentStudent?.let { student ->
                                    firestore.collection("students")
                                        .document(student.id)
                                        .update(
                                            mapOf(
                                                "name" to name,
                                                "phone" to phone,
                                                "college" to college,
                                                "year" to year
                                            )
                                        ).await()
                                    navController.popBackStack()
                                }
                            } catch (e: Exception) {
                                // Handle error
                            } finally {
                                loading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !loading
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Changes", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
