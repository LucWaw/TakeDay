package com.lucwaw.takeday.ui.addMedicine

import com.lucwaw.takeday.domain.model.Medicine

data class MedicinesState(
    var error: Boolean = false,
    var medicine: Medicine,
    var medicines: List<Medicine> = emptyList()
)