package com.lucwaw.takeday.ui.selectMedicines

import com.lucwaw.takeday.data.database.entities.MedicineEntity
import com.lucwaw.takeday.domain.model.Medicine

data class SelectMedicinesState(
    var medicines: List<Medicine> = emptyList(),
)
