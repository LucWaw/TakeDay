package com.lucwaw.takeday.data

data class Medicine(val name: String, val medicines: List<String> = listOf()) {
    fun toDomain(): com.lucwaw.takeday.domain.model.Medicinal {
        return com.lucwaw.takeday.domain.model.Medicinal(name, medicines + name)
    }
}