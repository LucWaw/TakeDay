package com.lucwaw.takeday.domain.model

import com.lucwaw.takeday.data.Medicine

data class Medicine(val name: String) {
    fun toDAO(): Medicine {
        return Medicine(name)
    }
}