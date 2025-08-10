package com.lucwaw.takeday

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucwaw.takeday.ui.AddMedicine
import com.lucwaw.takeday.ui.Home
import com.lucwaw.takeday.ui.addMedicine.AddMedicineScreenRoot
import com.lucwaw.takeday.ui.dailies.DailiesScreenRoot
import com.lucwaw.takeday.ui.theme.TakeDayTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TakeDayTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Home) {
                    composable<Home> {
                        DailiesScreenRoot(
                            goToAddScreen =
                                {
                                    Log.d("NAVIGATION", "Navigating to AddMedicine")
                                    navController.navigate(
                                        AddMedicine
                                    )
                                }
                        )
                    }
                    composable<AddMedicine> {
                        AddMedicineScreenRoot(
                            goBack = { navController.popBackStack() }
                        )
                    }
                }

            }
        }
    }
}