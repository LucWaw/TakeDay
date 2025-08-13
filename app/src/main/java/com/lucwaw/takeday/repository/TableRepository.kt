package com.lucwaw.takeday.repository

import com.lucwaw.takeday.data.database.dao.MedicineDao
import com.lucwaw.takeday.data.database.dao.RowDao
import com.lucwaw.takeday.data.database.entities.MedicineEntity
import com.lucwaw.takeday.data.database.entities.RowEntity
import com.lucwaw.takeday.domain.model.Medicine
import com.lucwaw.takeday.domain.model.Row
import javax.inject.Inject

class TableRepository @Inject constructor(
    private val rowDao: RowDao,
    private val medicineDao: MedicineDao
) {

    suspend fun getAllMedicines(): List<Medicine> = medicineDao.getAll().map {
        Medicine(it.id, it.name, it.isSelected)
    }

    suspend fun getMedicineById(id: Long): Medicine? {
        return medicineDao.getById(id)?.let { entity ->
            Medicine(entity.id, entity.name, entity.isSelected)
        }
    }

    suspend fun getAllRows(): List<Row> = rowDao.getAll().map { entity ->
        Row(entity.date, entity.time, entity.medicines)
    }

    suspend fun upsertRow(row: Row) {
        rowDao.upsert(RowEntity(row.date, row.time, row.medicines))
    }

    suspend fun removeMedicine(medicine: Medicine) {
        medicineDao.delete(MedicineEntity(medicine.id, medicine.name, medicine.isSelected))
        removeMedicineFromRows(medicineName = medicine.name)
    }

    private suspend fun removeMedicineFromRows(medicineName: String) {
        val rows = rowDao.getAll()
        rows.forEach { entity ->
            if (entity.medicines.containsKey(medicineName)) {
                val updatedMap = entity.medicines.toMutableMap()
                updatedMap.remove(medicineName)
                rowDao.upsert(entity.copy(medicines = updatedMap))
            }
            // If medicine and time are both null, we can remove the row
            if (entity.time == null && entity.medicines.isEmpty()) {
                rowDao.delete(entity)
            }
        }
    }

    suspend fun upsertMedicine(medicine: Medicine) {
        medicineDao.upsert(MedicineEntity(medicine.id, medicine.name, medicine.isSelected))
        //mise a jour des row :
    }
}

