package com.lucwaw.takeday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucwaw.takeday.ui.AddMedicine
import com.lucwaw.takeday.ui.Home
import com.lucwaw.takeday.ui.SelectMedicines
import com.lucwaw.takeday.ui.selectMedicines.SelectMedicines
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
                                    navController.navigate(
                                        AddMedicine
                                    )
                                },
                            goToSelectMedicines = {
                                    navController.navigate(SelectMedicines)
                            }
                        )
                    }
                    composable<AddMedicine> {
                        AddMedicineScreenRoot(
                            goBack = { navController.popBackStack() }
                        )
                    }
                    composable<SelectMedicines> {
                        SelectMedicines(
                            goBack = { navController.popBackStack() },
                            goToAddMedicine = {
                                navController.navigate(AddMedicine)
                            }
                        )
                    }
                }

            }
        }
    }
}