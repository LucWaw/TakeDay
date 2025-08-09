package com.lucwaw.takeday.domain.model

import com.lucwaw.takeday.data.Take


data class Take(val medicine: Medicine) {
    var wasTaken: Boolean = false

    fun setIsTaken(value: Boolean) { wasTaken = value }

    fun toDAO(): Take {
        return Take(medicine.toDAO(), wasTaken)
    }
}