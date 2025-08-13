package com.lucwaw.takeday.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 1. Créer une nouvelle table avec l'ID auto-généré
        database.execSQL("""
            CREATE TABLE medicines_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                isSelected INTEGER NOT NULL
            )
        """)

        // 2. Copier les données de l'ancienne table
        database.execSQL("""
            INSERT INTO medicines_new (name, isSelected)
            SELECT name, isSelected FROM medicines
        """)

        // 3. Supprimer l'ancienne table
        database.execSQL("DROP TABLE medicines")

        // 4. Renommer la nouvelle table
        database.execSQL("ALTER TABLE medicines_new RENAME TO medicines")
    }
}
