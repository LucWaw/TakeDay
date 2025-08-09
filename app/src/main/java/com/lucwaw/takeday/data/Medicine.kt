package com.lucwaw.takeday.data

data class Medicine(val name: String) {
    fun toDomain(): com.lucwaw.takeday.domain.model.Medicine {
        return com.lucwaw.takeday.domain.model.Medicine(name)
    }
}