package com.lucwaw.takeday.data

data class Take(val medicine: Medicine, val wasTaken : Boolean){
    fun toDomain(): com.lucwaw.takeday.domain.model.Take {
        return com.lucwaw.takeday.domain.model.Take(medicine.toDomain()).apply {
            setIsTaken(wasTaken)
        }
    }
}