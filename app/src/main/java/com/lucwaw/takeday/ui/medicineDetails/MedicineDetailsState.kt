package com.lucwaw.takeday.ui.medicineDetails

import com.lucwaw.takeday.domain.model.Medicine

data class MedicineDetailsState(
    val medicine : Medicine,
    val error : Boolean = true,
    val medicines : List<Medicine> = emptyList()
)
