package com.lucwaw.takeday.repository

import com.lucwaw.takeday.data.Medicine
import com.lucwaw.takeday.data.services.MedicineAPI
import javax.inject.Inject

class MedicineRepository @Inject constructor(private val medicinesAPI: MedicineAPI) {

    suspend fun getMedicines() = medicinesAPI.getMedicines()

    suspend fun addMedicine(medicineName: String) =
        medicinesAPI.addMedicine(Medicine(medicineName))

    suspend fun deleteMedicine(medicineName: String) =
        medicinesAPI.deleteMedicine(medicineName)

    suspend fun updateMedicine(oldName: String, newName: String) =
        medicinesAPI.updateMedicine(oldName, Medicine(newName))
}