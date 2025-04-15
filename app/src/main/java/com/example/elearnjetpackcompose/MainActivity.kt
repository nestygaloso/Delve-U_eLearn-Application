package com.example.elearnjetpackcompose

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

val PrimaryColor = Color(0xFF2DAA9E)
val SecondaryColor = Color(0xFF61827F)
val AccentColor = Color(0xFF0E675E)
val BackgroundColor = Color(0xFFEAEAEA)

data class Course(
    val name: String,
    val imageUrl: String,
    val pdfUrl: String? = null,
    val videoUrl: String? = null
)

val courses = listOf(
    Course(
        name = "Android Development",
        imageUrl = "https://images.pexels.com/photos/799443/pexels-photo-799443.jpeg",
        pdfUrl = "https://developer.android.com/static/codelabs/basic-android-kotlin-compose-first-app/img/3b0b9f9a9c9e5a9e.png?hl=es-419",
        videoUrl = "https://www.youtube.com/watch?v=8YPXv7xKh2w"
    ),
    Course(
        name = "Web Development",
        imageUrl = "https://images.pexels.com/photos/270360/pexels-photo-270360.jpeg",
        pdfUrl = "https://web.dev/static/learn/html/basics/hero-47e7d8a9b9f9a9e9.png",
        videoUrl = "https://www.youtube.com/watch?v=qz0aGYrrlhU"
    ),
    Course(
        name = "Python Programming",
        imageUrl = "https://images.pexels.com/photos/1181671/pexels-photo-1181671.jpeg",
        pdfUrl = "https://wiki.python.org/moin/BeginnersGuide/Programmers",
        videoUrl = "https://www.youtube.com/watch?v=rfscVS0vtbw"
    ),
    Course(
        name = "UI/UX Fundamentals",
        imageUrl = "https://images.pexels.com/photos/196644/pexels-photo-196644.jpeg",
        pdfUrl = "https://www.interaction-design.org/literature/topics/ux-design",
        videoUrl = "https://www.youtube.com/watch?v=Ovj4hFxko7c"
    ),
    Course(
        name = "Graphic Design Basics",
        imageUrl = "https://images.pexels.com/photos/1109541/pexels-photo-1109541.jpeg",
        pdfUrl = "https://design.tutsplus.com/articles/design-in-60-seconds-what-is-graphic-design--cms-28047",
        videoUrl = "https://www.youtube.com/watch?v=dFSia1LZI4Y"
    ),
    Course(
        name = "Illustration",
        imageUrl = "https://images.pexels.com/photos/1591061/pexels-photo-1591061.jpeg",
        pdfUrl = "https://www.skillshare.com/en/classes/Illustration-for-Beginners/1320900930",
        videoUrl = "https://www.youtube.com/watch?v=WwBVG0Si7rs"
    ),
    Course(
        name = "Social Media Marketing",
        imageUrl = "https://images.pexels.com/photos/607812/pexels-photo-607812.jpeg",
        pdfUrl = "https://learndigital.withgoogle.com/digitalgarage/course/social-media",
        videoUrl = "https://www.youtube.com/watch?v=9qk3Q_6lq2Q"
    ),
    Course(
        name = "SEO Strategies",
        imageUrl = "https://images.pexels.com/photos/267350/pexels-photo-267350.jpeg",
        pdfUrl = "https://moz.com/beginners-guide-to-seo",
        videoUrl = "https://www.youtube.com/watch?v=El3IZFGERbM"
    ),
    Course(
        name = "Email Marketing",
        imageUrl = "https://images.pexels.com/photos/6804582/pexels-photo-6804582.jpeg",
        pdfUrl = "https://mailchimp.com/resources/email-marketing-guide/",
        videoUrl = "https://www.youtube.com/watch?v=KqVfJ9QJZq8"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ELearnTheme {
                val navController = rememberNavController()
                MainScaffold(navController)
            }
        }
    }
}

@Composable
fun MainScaffold(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navController, startDestination = Screen.Landing.route) {
                composable(Screen.Landing.route) { LandingScreen(navController) }
                composable(Screen.Courses.route) { ELearnApp(navController) }
                composable(Screen.Profile.route) { ProfileScreen(navController) }
                composable(Screen.Messages.route) { MessagesScreen(navController) }
                composable(
                    route = Screen.CourseDetail.route,
                    arguments = listOf(navArgument("courseName") { type = NavType.StringType }) // ← fix here
                ) {
                    val encoded = it.arguments?.getString("courseName") ?: ""
                    val decoded = java.net.URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
                    CourseDetailScreen(decoded, navController)
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Courses : Screen("courses")
    object Profile : Screen("profile")
    object Messages : Screen("messages")
    object CourseDetail : Screen("course_detail/{courseName}") {
        fun createRoute(courseName: String): String {
            val encoded = URLEncoder.encode(courseName, StandardCharsets.UTF_8.toString())
            return "course_detail/$encoded"
        }
    }
}

@Composable
fun LandingScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to Delve U", style = MaterialTheme.typography.headlineLarge, color = PrimaryColor)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.Courses.route) }) { Text("Browse Courses") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate(Screen.Profile.route) }) { Text("View Profile") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate(Screen.Messages.route) }) { Text("Messages / Contact") }
        }

        Image(
            painter = painterResource(id = R.drawable.footer_banner),
            contentDescription = "Footer",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun ELearnApp(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("Development") }
    val categories = listOf("Development", "Design", "Marketing")
    val courseMap = mapOf(
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
            items(courseMap[selectedCategory] ?: emptyList()) { courseName ->
                val course = courses.find { it.name == courseName } ?: return@items
                CourseCard(course, progress[course.name] ?: 0, navController) {
                    progress = progress.toMutableMap().apply {
                        put(course.name, (progress[course.name] ?: 0) + 10)
                    }
                }
            }
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    progress: Int,
    navController: NavHostController,
    onProgressIncrease: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(course.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "${course.name} image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(course.name, style = MaterialTheme.typography.bodyLarge, color = PrimaryColor)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = progress / 100f, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                onProgressIncrease()
                navController.navigate(Screen.CourseDetail.createRoute(course.name))
            }) {
                Text("Continue Learning")
            }
        }
    }
}

@Composable
fun CourseDetailScreen(courseName: String, navController: NavHostController) {
    val course = courses.find { it.name == courseName } ?: return
    val context = LocalContext.current

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Course: $courseName", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        // PDF Resource Button
        course.pdfUrl?.let { url ->
            Button(
                onClick = { openPdfViewer(url, context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View PDF Course")
            }
            Spacer(Modifier.height(8.dp))
        }

        // Video Resource Button
        course.videoUrl?.let { url ->
            Button(
                onClick = { openVideoPlayer(url, context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Watch Video Course")
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

fun openPdfViewer(pdfUrl: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(pdfUrl)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

fun openVideoPlayer(videoUrl: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(videoUrl)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    val collections = remember {
        listOf(
            courses.find { it.name == "Android Development" }!!,
            courses.find { it.name == "Graphic Design Basics" }!!
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDAF3F0))
            .padding(24.dp)
    ) {
        Text("My Profile", style = MaterialTheme.typography.headlineSmall, color = PrimaryColor)
        Spacer(modifier = Modifier.height(20.dp))
        Image(painter = painterResource(id = R.drawable.man), contentDescription = "Avatar", modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.email), contentDescription = "Email Icon", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("nesty@example.com", color = SecondaryColor)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.book), contentDescription = "Book Icon", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("2 courses completed", color = AccentColor)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("My Collections", style = MaterialTheme.typography.titleMedium, color = AccentColor)
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {
            items(collections) { course ->
                Card(
                    onClick = { navController.navigate(Screen.CourseDetail.createRoute(course.name)) },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(200.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        AsyncImage(
                            model = course.imageUrl,
                            contentDescription = course.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(course.name, modifier = Modifier.padding(8.dp), color = PrimaryColor)
                    }
                }
            }
        }
    }
}

@Composable
fun MessagesScreen(navController: NavHostController) {
    var feedback by remember { mutableStateOf("") }
    var pendingMessages by remember {
        mutableStateOf(
            mutableListOf(
                "I cannot proceed to the next slide when I was clicking the next button on the Android course."
            )
        )
    }
    var resolvedMessages by remember {
        mutableStateOf(
            mutableListOf(
                "The graphic design video is not working when I was using the school wifi – ServiceNow Ticket Closed — Tagged as Resolved",
                "Problem with the completion option – ServiceNow Ticket Closed — Tagged as Resolved"
            )
        )
    }

    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var editingText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Feedback / Contact Us", style = MaterialTheme.typography.headlineSmall, color = PrimaryColor)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = feedback,
            onValueChange = { feedback = it },
            label = { Text("Your Message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (feedback.isNotBlank()) {
                pendingMessages.add(feedback)
                feedback = ""
            }
        }) {
            Text("Submit")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pending Section
        Text("Pending", style = MaterialTheme.typography.titleLarge, color = AccentColor)
        Spacer(modifier = Modifier.height(8.dp))

        pendingMessages.forEachIndexed { index, message ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (editingIndex == index) {
                        OutlinedTextField(
                            value = editingText,
                            onValueChange = { editingText = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            TextButton(onClick = {
                                pendingMessages[index] = editingText
                                editingIndex = null
                            }) {
                                Text("Save")
                            }
                            TextButton(onClick = {
                                editingIndex = null
                            }) {
                                Text("Cancel")
                            }
                        }
                    } else {
                        Text(message)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = {
                                editingIndex = index
                                editingText = message
                            }) {
                                Text("Edit")
                            }
                            TextButton(onClick = {
                                // "Open" simulated by showing a dialog in the future
                            }) {
                                Text("Open")
                            }
                            TextButton(onClick = {
                                pendingMessages.removeAt(index)
                            }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resolved Section
        Text("Resolved", style = MaterialTheme.typography.titleLarge, color = SecondaryColor)
        Spacer(modifier = Modifier.height(8.dp))

        resolvedMessages.forEachIndexed { index, message ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(message)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = {
                            // "Open" simulated (could show dialog or toast)
                        }) {
                            Text("Open")
                        }
                        TextButton(onClick = {
                            resolvedMessages.removeAt(index)
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FeedbackCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            message,
            modifier = Modifier
                .padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = navController.currentDestination?.route == Screen.Landing.route,
            onClick = { navController.navigate(Screen.Landing.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Courses") },
            label = { Text("Courses") },
            selected = navController.currentDestination?.route == Screen.Courses.route,
            onClick = { navController.navigate(Screen.Courses.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = navController.currentDestination?.route == Screen.Profile.route,
            onClick = { navController.navigate(Screen.Profile.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Email, contentDescription = "Messages") },
            label = { Text("Messages") },
            selected = navController.currentDestination?.route == Screen.Messages.route,
            onClick = { navController.navigate(Screen.Messages.route) }
        )
    }
}

@Composable
fun DropdownMenuComponent(label: String, options: List<String>, selectedOption: String, onSelectionChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = PrimaryColor)
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedOption)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelectionChange(it)
                        expanded = false
                    }
                )
            }
        }
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