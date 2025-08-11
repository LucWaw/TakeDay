package com.lucwaw.takeday.repository

import com.lucwaw.takeday.data.database.dao.MedicineDao
import com.lucwaw.takeday.data.database.dao.RowDao
import com.lucwaw.takeday.data.database.entities.MedicineEntity
import com.lucwaw.takeday.data.database.entities.RowEntity
import com.lucwaw.takeday.domain.model.Row
import javax.inject.Inject

class TableRepository @Inject constructor(
    private val rowDao: RowDao,
    private val medicineDao: MedicineDao
) {
    suspend fun getAllMedicines(): List<MedicineEntity> = medicineDao.getAll()

    suspend fun getAllRows(): List<Row> = rowDao.getAll().map { entity ->
        Row(entity.date, entity.time, entity.medicines)
    }

    suspend fun upsertRow(row: Row) {
        rowDao.upsert(RowEntity(row.date, row.time, row.medicines))
    }

    suspend fun removeMedicineByName(medicineName: String) {
        medicineDao.deleteByName(medicineName)
        removeMedicineFromRows(medicineName)
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

    suspend fun upsertMedicine(name : String, isSelected: Boolean = true) {
        val medicine = MedicineEntity(name, isSelected)
        medicineDao.upsert(medicine)

    }
}

