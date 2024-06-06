package com.shakilpatel.techsavvy.ui.classes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.R
import com.shakilpatel.techsavvy.data.models.ClassModel
import com.shakilpatel.techsavvy.data.resources.Resource
import com.shakilpatel.techsavvy.nav.ChatAppBar
import com.shakilpatel.techsavvy.utils.getDateFromMillis
import com.shakilpatel.techsavvy.utils.shareText
import com.shakilpatel.techsavvy.utils.shimmerEffect
import com.shakilpatel.techsavvy.utils.timeAgo

@Composable
fun ClassesScreen(viewModel: ClassesViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ChatAppBar(
            title = "Daily Classes",
            info = "In this section you can find out and get daily updates about the regular classes and there timings."
        )

        viewModel.classesList.collectAsState().value.let {
            when (it) {
                is Resource.Loading -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            Color(0x6F454646)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(150.dp)
                            .shimmerEffect()
                    ) {
                    }
                }

                is Resource.Failure -> {
                    Text(it.message, color = MaterialTheme.colorScheme.error)
                }
                is Resource.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize() ,
                        reverseLayout = true
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                            Text("Latest 👆",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(top = 10.dp),
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center)
                        }
                        items(it.result.sortedBy { it.created }.reversed()){
                            ClassesItemCard(it)
                        }

                    }
                }

                else -> {}
            }
        }


    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClassesItemCard(item: ClassModel) {
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(.35f),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = getDateFromMillis("hh:mm a dd MMM yy", item.created),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            HorizontalDivider(
                modifier = Modifier.weight(.35f),
                color = MaterialTheme.colorScheme.primary
            )
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
                    modifier = Modifier.weight(.1f)
                ) {
                    Card(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(35.dp),
                        shape = CircleShape
                    ) {
                        Image(
                            painterResource(id = R.drawable.img_classes),
                            "",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(.8f)
                ) {
                    Text(
                        timeAgo(item.starttime),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.End),
                        fontSize = 9.sp
                    )
                    FlowRow(
                        horizontalArrangement = SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Class for ${item.batch}",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Card(
                            colors = CardDefaults.cardColors(
                                MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                text = "#${item.batch}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                    }

                    HorizontalDivider()
                    Spacer(Modifier.height(10.dp))
                    Text(
                        item.desc,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        ) {
                        FlowRow(
                            modifier = Modifier.weight(.6f)
                        ) {
                            item.tags.split(",").forEach {
                                Card(
                                    modifier = Modifier.padding(5.dp),
                                    colors = CardDefaults.cardColors(
                                        Color(0x6F454646)
                                    ),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(
                                            horizontal = 5.dp,
                                            vertical = 2.dp
                                        )
                                    )
                                }
                            }
                        }
                        Column(
                            modifier = Modifier.weight(.4f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    text = getDateFromMillis("hh:mm a dd MMM",item.starttime),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                "To",
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    text = getDateFromMillis("hh:mm a dd MMM",item.endtime),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val urlHandler = LocalUriHandler.current
                val context = LocalContext.current
                Button(
                    onClick = {
                        urlHandler.openUri(item.meet)
                    },
                    modifier = Modifier.weight(.9f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Join")
                }
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(
                    onClick = {
                        //write a code that can make to share the link from item.meet
                        context.shareText(item.meet)
                    },
                    modifier = Modifier.weight(.1f)
                ) {
                    Icon(
                        Icons.Default.Share,
                        "",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

        }
    }
}