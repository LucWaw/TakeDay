package com.lucwaw.takeday.repository

import com.lucwaw.takeday.data.database.dao.MedicineDao
import com.lucwaw.takeday.data.database.dao.RowDao
import com.lucwaw.takeday.data.database.entities.MedicineEntity
import com.lucwaw.takeday.data.database.entities.RowEntity
import com.lucwaw.takeday.domain.model.Medicine
import com.lucwaw.takeday.domain.model.Row
import com.lucwaw.takeday.domain.model.TriState
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
        for (row in rows){
            if (row.medicines.containsKey(medicineName)) {
                val updatedMap = row.medicines.toMutableMap()
                updatedMap.remove(medicineName)
                rowDao.upsert(row.copy(medicines = updatedMap))
            }
            // If medicine and time are both null, we can remove the row
            if (row.time == null && row.medicines.isEmpty()) {
                rowDao.delete(row)
            }
        }
    }

    suspend fun insertMedicine(medicine: Medicine) {
        medicineDao.insert(MedicineEntity(0, medicine.name, medicine.isSelected))
    }


    suspend fun updateMedicineSelectionFromId(medicineId: Long, isSelected: Boolean) {
        medicineDao.updateIsSelected(medicineId, isSelected)
    }

    suspend fun updateMedicineNameFromId(medicineId: Long, oldName: String, newName: String) {
        medicineDao.updateName(medicineId, newName)
        //mise a jour des rows :
        val rows = rowDao.getAll()
        for (row in rows) {
            if (row.medicines.containsKey(oldName)) {
                val updatedMedicines = row.medicines.toMutableMap()
                updatedMedicines[newName] = updatedMedicines.remove(oldName) ?: TriState.EMPTY
                rowDao.upsert(row.copy(medicines = updatedMedicines))
            }
        }
    }
}

