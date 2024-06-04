package com.shakilpatel.techsavvy.ui.classes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.R
import com.shakilpatel.techsavvy.data.models.ClassModel
import com.shakilpatel.techsavvy.nav.ChatAppBar

@Composable
fun ClassesScreen(viewModel: ClassesViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val classType = ClassModel(
            "abc12334",
            "June-2024",
            324569848454,
            1212154978746,
            "C,C++,Computer Basics",
            "we discuss about the basics of computer",
            "https://tech-savvy-solution.web.app",
            46597854264848,
            "04-06-2024"
        )

        ChatAppBar(
            title = "Daily Classes",
            info = "In this section you can find out and get daily updates about the regular classes and there timings."
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            reverseLayout = true
        ) {
            item {
                ClassesItemCard(classType)
            }

        }

    }
}

@Composable
fun ClassesItemCard(item: ClassModel) {
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(.35f), color = MaterialTheme.colorScheme.primary)
            Text(
                text = item.batch,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier= Modifier.padding(horizontal = 5.dp)
            )

            HorizontalDivider(modifier = Modifier.weight(.35f), color = MaterialTheme.colorScheme.primary)
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            colors = CardDefaults.cardColors(
                Color(0x6F454646)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier.weight(.2f)
                ) {
                    Card(
                        modifier = Modifier.size(35.dp),
                        shape = CircleShape
                    ) {
                        Image(
                            painterResource(id = R.drawable.img_classes),
                            "",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        )
                    }
                }
                Column(

                ){
                    Text("3sec ago",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier= Modifier.align(Alignment.End),
                        fontSize = 9.sp)
                    Text(item.batch)
                }
            }

        }
    }
}