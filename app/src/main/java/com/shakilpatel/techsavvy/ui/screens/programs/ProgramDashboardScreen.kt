package com.shakilpatel.techsavvy.ui.screens.programs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shakilpatel.techsavvy.ui.screens.programs.tabs.*
import com.shakilpatel.techsavvy.viewmodel.AuthViewModel
import com.shakilpatel.techsavvy.viewmodel.ProgramViewModel
import kotlinx.coroutines.launch

enum class ProgramTab(val title: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard),
    SESSIONS("Sessions", Icons.Default.Event),
    RECORDINGS("Recordings", Icons.Default.VideoLibrary),
    MILESTONES("Milestones", Icons.Default.Flag),
    GROUP_CHAT("Group Chat", Icons.Default.Group),
    SUPPORT("Support", Icons.Default.SupportAgent),
    DOCUMENTS("Documents", Icons.Default.Description)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProgramDashboardScreen(
    navController: NavController,
    programId: String,
    authViewModel: AuthViewModel = viewModel(),
    programViewModel: ProgramViewModel = viewModel()
) {
    val currentStudent by authViewModel.currentStudent.collectAsState()
    val currentProgram by programViewModel.currentProgram.collectAsState()
    val currentGroup by programViewModel.currentGroup.collectAsState()
    val loading by programViewModel.loading.collectAsState()

    // Pager state for swiping
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { ProgramTab.values().size }
    )
    val coroutineScope = rememberCoroutineScope()

    // Load program data
    LaunchedEffect(programId) {
        authViewModel.fetchCurrentStudent()
        programViewModel.loadProgram(programId)
        programViewModel.loadSessions(programId)
    }

    // Load enrollment to get group ID
    LaunchedEffect(currentStudent, programId) {
        currentStudent?.let { student ->
            programViewModel.loadEnrolledPrograms(student.id)
            programViewModel.loadGroup(programId, student.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentProgram?.name ?: "Loading...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (currentProgram != null) {
                            Text(
                                text = currentProgram!!.category.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase() else it.toString()
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Scrollable Tab Row
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 8.dp,
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                ProgramTab.values().forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tab.title,
                                    fontSize = 13.sp,
                                    fontWeight = if (pagerState.currentPage == index)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Horizontal Pager (Swipeable Content)
            if (loading && currentProgram == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                currentProgram?.let { program ->
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (ProgramTab.values()[page]) {
                            ProgramTab.DASHBOARD -> DashboardTab(
                                program = program,
                                student = currentStudent,
                                group = currentGroup
                            )
                            ProgramTab.SESSIONS -> SessionsTab(
                                programId = programId,
                                programViewModel = programViewModel
                            )
                            ProgramTab.RECORDINGS -> RecordingsTab(programId = programId)
                            ProgramTab.MILESTONES -> MilestonesTab(program = program)
                            ProgramTab.GROUP_CHAT -> GroupChatTab(
                                programId = programId,
                                student = currentStudent,
                                programViewModel = programViewModel
                            )
                            ProgramTab.SUPPORT -> SupportChatTab(
                                programId = programId,
                                student = currentStudent,
                                programViewModel = programViewModel
                            )
                            ProgramTab.DOCUMENTS -> DocumentsTab(
                                programId = programId,
                                studentId = currentStudent?.id ?: "",
                                programViewModel = programViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
