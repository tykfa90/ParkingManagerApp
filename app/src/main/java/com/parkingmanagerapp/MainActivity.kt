package com.parkingmanagerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.parkingmanagerapp.repository.ParkingSlotRepository
import com.parkingmanagerapp.ui.theme.ParkingManagerAppTheme
import com.parkingmanagerapp.view.ParkingSlotScreen
import com.parkingmanagerapp.viewModel.ParkingSlotViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkingManagerAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }


        }
    }
}

@Composable
fun MainScreen() {
    Text(
        text = "I'm the main activity screen!",
        modifier = Modifier.padding(16.dp)
    )

    ParkingSlotScreen(ParkingSlotViewModel(ParkingSlotRepository()))
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ParkingManagerAppTheme {
        MainScreen()
    }
}

//class MainActivity : ComponentActivity() {
//    @SuppressLint("CoroutineCreationDuringComposition")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            MaterialTheme {
//                // Loading text to display while application loads contents.
//                var text by remember { mutableStateOf("Loading...") }
//
//                // Attempt to fetch data from Firestore
//                lifecycleScope.launch {
//                    val db = FirebaseFirestore.getInstance()
//                    try {
//                        val document =
//                            db.collection("parkingSlots").document("Jx7Ghsq2m9r5UEWRZPpt").get()
//                                .await()
//                        text = if (document.exists()) {
//                            "Document data: ${document.data}"
//                        } else {
//                            "No such document"
//                        }
//                    } catch (e: Exception) {
//                        text = "Error fetching document: ${e.message}"
//                    }
//                }
//
//                // Display the fetched data or status
//                YourContent(text)
//            }
//        }
//    }
//}
//
//@Composable
//fun YourContent(text: String) {
//    Text(text = text)
//}