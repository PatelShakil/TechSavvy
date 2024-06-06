package com.shakilpatel.techsavvy.ui.students

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.data.models.StudentModel
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.nav.ChatAppBar

@Composable
fun StudentsScreen(gender :String,viewModel: StudentsViewModel,navController: NavController) {
    Column {

        ChatAppBar(title = if(gender == "Male") "Boys Area \uD83D\uDDFF" else "Girls Area \uD83D\uDC69\uD83C\uDFFB\u200D\uD83D\uDCBC",
            info = if(gender == "Male"){
                "Here you can chat with your friends and get updates about the boys."
            }else{
                "Here you can chat with your friends and get updates about the girls."
            }
        )

        viewModel.students.collectAsState().value.let {
            when (it) {
                is Resource.Loading -> {
                    Text("Loading ...",
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary)
                }
                is Resource.Failure -> {}
                is Resource.Success -> {
                    LazyColumn(
                        modifier= Modifier.fillMaxSize()
                    ) {
                        items(
                            it.result
                        ){
                            UserItemCard(it)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun UserItemCard(item:StudentModel, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(Color(0x6F454646)),
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        ) {
            Card(
                modifier = Modifier.size(46.dp),
                shape = CircleShape
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.name.first().toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 1.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.width(5.dp))
            Column(modifier = Modifier.weight(.7f)
                .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center) {
                Text(item.name.trim(),
                    color = MaterialTheme.colorScheme.primary)
            }
        }

    }
}
