package com.lucwaw.takeday.data.services

import android.util.Log
import com.lucwaw.takeday.data.Medicine

class MedicineFakeAPI : MedicineAPI {

    private val medicines = mutableListOf<Medicine>(
        Medicine("Aspirin"),
        Medicine("Ibuprofen"),
        Medicine("Paracetamol"),
        Medicine("Amoxicillin"),
        Medicine("Metformin")
    )

    override suspend fun getMedicines(): List<Medicine> {
        return medicines
    }

    override suspend fun getMedicine(name: String): Medicine? {
        return medicines.find { it.name == name }
    }

    override suspend fun addMedicine(medicine: Medicine): Medicine {
        if (medicines.any { it.name == medicine.name }) {
            throw IllegalArgumentException("Medicine with name ${medicine.name} already exists.")
        }
        medicines.add(medicine)
        return medicine
    }

    override suspend fun updateMedicine(oldName: String, medicine: Medicine): Medicine {
        val index = medicines.indexOfFirst { oldName == medicine.name }
        if (index == -1) {
            throw NoSuchElementException("Medicine with name ${medicine.name} not found.")
        }
        medicines[index] = medicine
        return medicine
    }

    override suspend fun deleteMedicine(name: String) {
        val medicine = medicines.find { it.name == name }
        if (medicine == null) {
            throw NoSuchElementException("Medicine with name $name not found.")
        }
        medicines.remove(medicine)
    }

    override suspend fun deleteAllMedicines() {
        medicines.clear()
    }


}