package com.lucwaw.takeday.ui

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object AddMedicine

@Serializable
object SelectMedicines

@Serializable
data class MedicineDetails(
    val medicineId : Long)

@Serializable
object TemporaryGraphScreen