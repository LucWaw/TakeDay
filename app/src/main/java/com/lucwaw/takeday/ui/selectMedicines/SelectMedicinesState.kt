package com.lucwaw.takeday.ui.selectMedicines

import com.lucwaw.takeday.data.database.entities.MedicineEntity

data class SelectMedicinesState(
    var medicines: List<MedicineEntity> = emptyList(),
)
