package com.example.elearnjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background

// Theme Colors
val PrimaryColor = Color(0xFF2DAA9E)
val BackgroundColor = Color(0xFFEAEAEA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ELearnTheme {
                ELearnApp()
            }
        }
    }
}

@Composable
fun ELearnApp() {
    var selectedCategory by remember { mutableStateOf("Development") }
    val categories = listOf("Development", "Design", "Marketing")
    val courses = mapOf(
        "Development" to listOf("Android Development", "Web Development", "Python Programming"),
        "Design" to listOf("UI/UX Fundamentals", "Graphic Design Basics", "Illustration"),
        "Marketing" to listOf("Social Media Marketing", "SEO Strategies", "Email Marketing")
    )
    var progress by remember { mutableStateOf(mutableMapOf<String, Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(BackgroundColor)
    ) {
        DropdownMenuComponent(
            label = "Select Category",
            options = categories,
            selectedOption = selectedCategory,
            onSelectionChange = { selectedCategory = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(courses[selectedCategory] ?: emptyList()) { course ->
                CourseCard(course, progress[course] ?: 0) {
                    progress = progress.toMutableMap().apply { put(course, (progress[course] ?: 0) + 10) }
                }
            }
        }
    }
}

@Composable
fun CourseCard(courseName: String, progress: Int, onProgressIncrease: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(courseName, style = MaterialTheme.typography.bodyLarge, color = PrimaryColor)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = progress / 100f, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            CustomButton("Continue Learning") { onProgressIncrease() }
        }
    }
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, contentColor = Color.White),
        modifier = Modifier.fillMaxWidth().padding(5.dp)
    ) {
        Text(text)
    }
}

@Composable
fun DropdownMenuComponent(
    label: String,
    options: List<String>,
    selectedOption: String,
    onSelectionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = PrimaryColor)
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedOption)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = PrimaryColor) },
                    onClick = {
                        onSelectionChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ELearnAppPreview() {
    ELearnTheme {
        ELearnApp()
    }
}

@Composable
fun ELearnTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryColor,
            onPrimary = Color.White,
            background = BackgroundColor,
            onBackground = Color.Black
        ),
        content = content
    )
}
