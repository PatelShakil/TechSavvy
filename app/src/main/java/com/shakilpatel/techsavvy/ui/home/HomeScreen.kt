package com.shakilpatel.techsavvy.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.R
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.nav.AppScreen
import com.shakilpatel.techsavvy.ui.common.ProgressBarNonCancelable

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    viewModel.studentLocal.collectAsState().value.let {
        when (it) {
            is Resource.Loading -> {
                ProgressBarNonCancelable()
            }

            is Resource.Failure -> {
                if (it.message == "No student found") {
                    navController.navigate(AppScreen.Auth.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }

            is Resource.Success -> {

                Column(modifier = Modifier.fillMaxWidth()) {

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp)
                            .padding(top = 20.dp),
                        onClick = {

                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painterResource(id = R.drawable.img_classes),
                                "",
                                modifier = Modifier.size(50.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                "Classes",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    }
                    LazyVerticalGrid(
                        GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            HomeNavComp(R.drawable.img_group, "Discussion") {

                            }
                        }
                        if (it.result.gender == "Male") {
                            item {
                                HomeNavComp(R.drawable.img_boys, "Boys") {

                                }
                            }
                        } else {
                            item {
                                HomeNavComp(R.drawable.img_girls, "Girls") {

                                }
                            }
                        }
                        item {
                            HomeNavComp(R.drawable.img_books, "Materials") {

                            }
                        }

                    }
                }

            }

            else -> {}
        }


    }
}

@Composable
fun HomeNavComp(icon: Int, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.padding(horizontal = 30.dp, vertical = 10.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = icon),
                "",
                modifier = Modifier.size(50.dp)
            )
            Text(title)
        }
    }
}