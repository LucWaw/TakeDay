package com.lucwaw.takeday.domain.model

import com.lucwaw.takeday.data.Medicine

data class Medicinal(val name: String, val medicines: List<String> = listOf()) { /// Have to replace it with a stranger key room
    init {
        require(medicines.contains(name))
    }
    fun toDAO(): Medicine {
        return Medicine(name)
    }
}