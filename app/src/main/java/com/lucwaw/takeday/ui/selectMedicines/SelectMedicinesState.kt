package com.lucwaw.takeday.ui.selectMedicines

data class SelectMedicinesState(
    var medicines: Map<String, Boolean> = emptyMap(),
)
