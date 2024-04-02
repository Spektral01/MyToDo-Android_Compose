package com.example.mytodo.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.mytodo.Data.Priority
import com.example.mytodo.Data.Task
import java.time.LocalDate
import java.time.LocalDateTime

class MainScreenViewModel : ViewModel() {
    var uiState by mutableStateOf(IsEmptyListState.Clear)
        private set

    var tasks by mutableStateOf(
        mutableListOf<Task>()
    )
    var currentTsk = Task()
    var checkState by mutableStateOf(false)
    fun getState(): Boolean {
        return when (uiState) {
            IsEmptyListState.Clear -> false
            IsEmptyListState.Tasks -> true
        }
    }
    fun toggleTaskCheckedState(task: Task, chek:Boolean) {
        task.completed = chek
    }
    fun changeState() {
        uiState = IsEmptyListState.Tasks
    }

    fun getTaskColor(tsk: Priority): Color {
        return when (tsk) {
            Priority.Critical -> Color.Red
            Priority.Major -> Color.Yellow
            Priority.Moderate -> Color.Blue
            Priority.Low -> Color.Green
            else -> {
                return Color.Transparent
            }
        }
    }
}

enum class IsEmptyListState {
    Clear,
    Tasks
}