package com.example.mytodo.Screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mytodo.Data.Priority
import com.example.mytodo.Data.Task
import com.example.mytodo.Navigation.Routes
import com.example.mytodo.R
import com.example.mytodo.ViewModel.AddOrChangeViewModel
import com.example.mytodo.ViewModel.MainScreenViewModel

@Composable
fun TaskScreen(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel,
    viewModel: AddOrChangeViewModel
) {
    var existingTask = Task()
    LaunchedEffect(true) {
        /*var tsk = mainScreenViewModel.currentTsk.id
        if (tsk != null) {
            existingTask = viewModel.refreshTask(mainScreenViewModel, tsk)
        } else {
            viewModel.setStateToCreate()
            viewModel.selectedOption = Priority.Critical.toString()
        }*/
        viewModel.taska = mainScreenViewModel.currentTsk
        viewModel.updateFields()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .padding(8.dp)
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = stringResource(R.string.one_task),
                fontSize = 24.sp
            )
        }
        Row(
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = viewModel.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = stringResource(R.string.back),
                tint = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .size(28.dp)
                    .clickable {
                        viewModel.setStateToEdit()
                        navController.navigate(Routes.ADD_OR_CHANGE_SCREEN)
                    }
            )
        }
        var taskPhaseText by remember {
            mutableStateOf("")
        }
        if (viewModel.taska.completed)
            taskPhaseText = "Выполнено"
        else
            taskPhaseText = "В процессе"
        Text(
            text = taskPhaseText,
            color = if (viewModel.taska.completed) Color.Green else Color.Gray,
            modifier = Modifier.align(Alignment.End)
        )
        Text(
            text = viewModel.text,
            modifier = Modifier.padding(top = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_access_time),
                contentDescription = null,
                tint = Color.Gray
            )
            Text(
                text = "До ${viewModel.selectedDate}",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CutCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                    .background(mainScreenViewModel.getTaskColor(Priority.valueOf(viewModel.selectedOption)))
            ) {
                Text(
                    text = viewModel.selectedOption,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}
