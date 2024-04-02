package com.example.mytodo.Data

data class Task(
    var id: String? = null,
    var title: String? = null,
    var text: String? = null,
    var date: String? = null,
    var completed: Boolean = false,
    var priority: Priority? = Priority.Prior,
)

enum class Priority {
    Critical,
    Major,
    Moderate,
    Low,
    Prior
}