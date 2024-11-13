package com.rick.morty.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rick.morty.models.quizQuestions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CharacterComparisonTool() {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }
    var showCorrectAnswer by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (isQuizCompleted) {
        showDialog = true
    }

    if (showDialog) {
        QuizCompletionDialog(
            score = score,
            totalQuestions = quizQuestions.size,
            onDismiss = {
                showDialog = false
                score = 0
                currentQuestionIndex = 0
                selectedOption = null
                isQuizCompleted = false
            }
        )
    } else {
        val question = quizQuestions[currentQuestionIndex]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            // Title and Explanation Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Rick and Morty Quiz",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Test your knowledge about Rick and Morty!",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Score: $score/${quizQuestions.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Current Question Count (Left) and Score (Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentQuestionIndex + 1}/${quizQuestions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question Text
            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Display options with color indicators for correct/incorrect answers
            question.options.forEach { option ->
                OptionButton(
                    option = option,
                    isSelected = selectedOption == option,
                    isCorrect = option == question.answer && showCorrectAnswer,
                    isWrong = option != question.answer && showCorrectAnswer && selectedOption == option
                ) {
                    if (!showCorrectAnswer) {
                        selectedOption = option
                        if (selectedOption == question.answer) {
                            score++
                        }
                        showCorrectAnswer = true
                        coroutineScope.launch {
                            delay(3000)
                            showCorrectAnswer = false
                            selectedOption = null
                            if (currentQuestionIndex < quizQuestions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                isQuizCompleted = true
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OptionButton(option: String, isSelected: Boolean, isCorrect: Boolean, isWrong: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isCorrect && !isWrong) { onClick() }
            .padding(8.dp)
            .shadow(
                elevation = 2.dp, // Softer, lighter shadow
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
            .background(
                color = when {
                    isCorrect -> Color.Green.copy(alpha = 0.2f)
                    isWrong -> Color.Red.copy(alpha = 0.2f)
                    isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    else -> MaterialTheme.colorScheme.background
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = option, style = MaterialTheme.typography.bodyMedium)
    }
}



@Composable
fun QuizCompletionDialog(score: Int, totalQuestions: Int, onDismiss: () -> Unit) {
    val title = if (score == totalQuestions) "Congratulations!" else "Quiz Over"
    val message = if (score == totalQuestions) "You got all the questions correct!" else "You scored $score out of $totalQuestions."

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "OK")
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier.padding(16.dp)
    )
}



