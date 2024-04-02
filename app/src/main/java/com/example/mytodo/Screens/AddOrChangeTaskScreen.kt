package com.example.mytodo.Screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mytodo.Components.CustomDropdownMenu
import com.example.mytodo.Data.Priority
import com.example.mytodo.Data.Task
import com.example.mytodo.Navigation.Routes
import com.example.mytodo.R
import com.example.mytodo.ViewModel.AddOrChangeViewModel
import com.example.mytodo.ViewModel.EditOrCreateState
import com.example.mytodo.ViewModel.MainScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrChange(
    viewModel: AddOrChangeViewModel,
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel
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
        if (viewModel.taska.id == null) viewModel.setStateToCreate()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                text = stringResource(viewModel.headerTitleState),
                fontSize = 24.sp
            )
        }
        Text(
            text = stringResource(R.string.title),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 24.dp)
        )
        TextField(
            value = viewModel.title,
            onValueChange = {
                viewModel.title = it
                viewModel.taska.title = viewModel.title
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Blue,
                focusedIndicatorColor = Color.Blue
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.text,
            onValueChange = {
                //if (it.length <= viewModel.maxChar) {
                viewModel.text = it
                viewModel.taska.text = viewModel.text
                //}
            },
            label = { Text("Описание") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            supportingText = {
                Text(
                    text = "${viewModel.text.length} / ${viewModel.maxChar}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            },
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 4.dp)
        ) {
            TextField(
                value = viewModel.selectedDate,
                onValueChange = {
                    viewModel.selectedDate = it
                },
                label = { Text("Срок выполнения задачи") },
                readOnly = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Black,
                    focusedIndicatorColor = Color.Black
                ),
                modifier = Modifier
                    .weight(0.86f)
            )
            IconButton(onClick = { viewModel.isShowDialog = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date picker",
                    tint = Color.Blue,
                    modifier = Modifier
                        .weight(0.14f)
                        .size(32.dp)
                )
            }
            if (viewModel.isShowDialog) {
                DatePickerDialog(
                    onDismiss = {
                        viewModel.isShowDialog = false
                    },
                    onConfirm = {
                        viewModel.selectedDate = it
                        viewModel.taska.date = viewModel.selectedDate
                    }
                )
            }
        }
        CustomDropdownMenu(
            options = viewModel.options,
            onOptionSelected = {
                viewModel.selectedOption = it
                viewModel.taska.priority = Priority.valueOf(viewModel.selectedOption)
            },
            selectedOption = if (Priority.valueOf(viewModel.selectedOption) == Priority.Prior) "Приоритет" else viewModel.selectedOption,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        val context = LocalContext.current
        Button(
            onClick = {
                if (
                    viewModel.taska.priority != Priority.Prior
                ) {
                    if (viewModel.uiState == EditOrCreateState.Create) {
                        viewModel.taska.id = UUID.randomUUID().toString()
                        mainScreenViewModel.tasks.add(viewModel.taska)
                        navController.popBackStack(Routes.MAIN_SCREEN, false)
                    } else {
                        val indexToUpdate =
                            mainScreenViewModel.tasks.indexOfFirst { it.id == viewModel.taska.id }
                        mainScreenViewModel.tasks[indexToUpdate] = viewModel.taska
                        navController.popBackStack(Routes.MAIN_SCREEN, false)
                    }
                }
                else{
                    Toast.makeText(context, "Укажите приоритет", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.save),
                fontSize = 18.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
@ExperimentalMaterial3Api
@Composable
private fun DatePickerDialog(
    onDismiss: () -> Unit = {},
    onConfirm: (String) -> Unit = {}
) {
    val datePickerState = rememberDatePickerState()
    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                datePickerState.selectedDateMillis?.let {
                    onConfirm(convertDate(it))
                }
                onDismiss()
            }) {
                Text(text = "Done")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun convertDate(time: Long): String {
    if (time == 0L) return ""
    return SimpleDateFormat("yyyy-MM-dd").format(Date(time))
}
