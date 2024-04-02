package com.example.mytodo.Screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mytodo.Data.Task
import com.example.mytodo.Navigation.Routes
import com.example.mytodo.R
import com.example.mytodo.ViewModel.AddOrChangeViewModel
import com.example.mytodo.ViewModel.MainScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun MainScreen(viewModel: MainScreenViewModel, navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.not_forgot),
            fontSize = 22.sp
        )
        if (viewModel.tasks.isEmpty()) {
            EmptyTaskList()
        } else {
            TaskList(viewModel, navController)
        }
    }
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(24.dp)
    ) {
        Button(
            onClick = {
                viewModel.currentTsk = Task()
                navController.navigate(Routes.ADD_OR_CHANGE_SCREEN)
            },
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "+",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraLight,
                modifier = Modifier.offset(x = (-1).dp, y = (-1).dp)
            )
        }
    }
}

@Composable
fun EmptyTaskList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_main),
                contentDescription = stringResource(R.string.main_background_man_with_cocktail)
            )
            Text(
                text = stringResource(R.string.u_have_no_task),
                fontSize = 18.sp
            )
            Text(
                text = stringResource(R.string.have_nice_rest),
                fontSize = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskList(viewModel: MainScreenViewModel, navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 36.dp)
    ) {
        Text(
            text = stringResource(R.string.task),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            modifier = Modifier.weight(2.5f)
        ) {
            items(
                viewModel.tasks.size,
                key = { it }
            ) { index ->
                val task = viewModel.tasks[index]
                SwipeToDismiss(
                    task,
                    onRemove = {
                        viewModel.tasks.remove(task)
                        navController.navigate(Routes.MAIN_SCREEN)
                        navController.popBackStack()
                    },
                    viewModel,
                    navController
                )
            }
            /*itemsIndexed(viewModel.tasks) { index, item ->
                    item,
                    onRemove = {
                        viewModel.tasks.remove(item)
                    },
                    modifier = Modifier.animateItemPlacement(tween(200)),
                    viewModel,
                    navController
                )
            }*/
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismiss(
    item: Task,
    onRemove: () -> Unit,
    viewModel: MainScreenViewModel,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                coroutineScope.launch {
                    delay(1.seconds)
                    onRemove()
                }
                true
            } else {
                false
            }
        }
    )
    SwipeToDismissBox(
        state = swipeToDismissState,
        backgroundContent = {
            val backgroundColor by animateColorAsState(
                targetValue = when (swipeToDismissState.currentValue) {
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    SwipeToDismissBoxValue.Settled -> Color.White
                }, label = ""
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            )
        },
    ) {
        TaskCard(item, viewModel, navController)
    }
}

@Composable
fun TaskCard(tsk: Task, viewModel: MainScreenViewModel, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = viewModel.getTaskColor(tsk.priority!!)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable {
                viewModel.currentTsk = tsk
                navController.navigate(Routes.TASK_SCREEN)
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.88f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                tsk.title?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                tsk.text?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 18.sp,
                        maxLines = 1
                    )
                }
            }
            var isChecked by remember {
                mutableStateOf(tsk.completed)
            }
            Checkbox(
                checked = isChecked,
                onCheckedChange = { completed ->
                    viewModel.toggleTaskCheckedState(tsk, completed)
                    isChecked = completed
                },
                modifier = Modifier
                    .weight(0.12f)
            )
        }
    }
}