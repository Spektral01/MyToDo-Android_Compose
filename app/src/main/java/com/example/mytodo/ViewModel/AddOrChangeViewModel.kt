package com.example.mytodo.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mytodo.Data.Priority
import com.example.mytodo.Data.Task
import com.example.mytodo.Navigation.Routes
import com.example.mytodo.R

class AddOrChangeViewModel : ViewModel() {
    var uiState by mutableStateOf(EditOrCreateState.Create)
        private set

    var selectedOption by mutableStateOf("Prior")
    val options = Priority.values().map { it.name }
    var isShowDialog by mutableStateOf(false)
    val maxChar = 50
    var selectedDate by mutableStateOf("")
    var title by mutableStateOf("")
    var text by mutableStateOf("")
    var taska by mutableStateOf(Task())
    var headerTitleState by mutableStateOf(R.string.add_task)

    fun setStateToEdit() {
        uiState = EditOrCreateState.Edit
        headerTitleState = R.string.edit_task
        updateFields()
    }

    fun setStateToCreate() {
        uiState = EditOrCreateState.Create
        headerTitleState = R.string.add_task
        updateFields()
        selectedOption = "Prior"
    }
    fun updateFields(){
        title = taska.title ?: ""
        text = taska.text ?: ""
        selectedDate = taska.date ?:""
    }

    fun nullCheck(str : String?):String {
        return str ?: ""
    }

    fun refreshTask(mainScreenViewModel: MainScreenViewModel, tsk: String): Task {
        val existingTask = tsk.let { taskId ->
            mainScreenViewModel.tasks.find { it.id == taskId }
        } ?: Task()
        title = nullCheck(existingTask.title)
        text = nullCheck(existingTask.text)
        selectedDate = nullCheck(existingTask.date)
        selectedOption = existingTask.priority.toString()
        mainScreenViewModel.currentTsk = Task()
        setStateToEdit()
        return existingTask
    }

}

enum class EditOrCreateState {
    Create,
    Edit
}