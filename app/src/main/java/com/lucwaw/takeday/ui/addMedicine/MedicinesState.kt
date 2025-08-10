package com.lucwaw.takeday.ui.addMedicine

data class MedicinesState (
    var error : Boolean = false,
    var isLoading: Boolean = false,
    var medicineName: String = "",
    var medicines: List<String> = emptyList()
)