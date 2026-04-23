package com.nttdata.ehcos.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nttdata.ehcos.model.ControlMedico

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME    = "ehcos_health.db"
        const val DATABASE_VERSION = 1

        const val TABLE_CONTROL   = "control_medico"
        const val COL_ID          = "id"
        const val COL_NOMBRES     = "nombres"
        const val COL_EDAD        = "edad"
        const val COL_PESO        = "peso"
        const val COL_ALTURA      = "altura"
        const val COL_PRESION     = "presion_arterial"
        const val COL_COMENTARIO  = "comentario"
        const val COL_IMC         = "imc"
        const val COL_FECHA       = "fecha"
        const val COL_HORA        = "hora"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_CONTROL (
                $COL_ID         INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRES    TEXT    NOT NULL,
                $COL_EDAD       INTEGER NOT NULL,
                $COL_PESO       REAL    NOT NULL,
                $COL_ALTURA     REAL    NOT NULL,
                $COL_PRESION    TEXT    NOT NULL,
                $COL_COMENTARIO TEXT,
                $COL_IMC        REAL,
                $COL_FECHA      TEXT    NOT NULL,
                $COL_HORA       TEXT    NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTROL")
        onCreate(db)
    }

    fun insertarControl(control: ControlMedico): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRES,    control.nombres)
            put(COL_EDAD,       control.edad)
            put(COL_PESO,       control.peso)
            put(COL_ALTURA,     control.altura)
            put(COL_PRESION,    control.presionArterial)
            put(COL_COMENTARIO, control.comentario)
            put(COL_IMC,        control.imc)
            put(COL_FECHA,      control.fecha)
            put(COL_HORA,       control.hora)
        }
        val id = db.insert(TABLE_CONTROL, null, values)
        db.close()
        return id
    }

    fun obtenerTodos(): List<ControlMedico> {
        val lista = mutableListOf<ControlMedico>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CONTROL, null, null, null, null, null,
            "$COL_ID DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(cursorToControl(it))
            }
        }
        db.close()
        return lista
    }

    fun obtenerPorId(id: Long): ControlMedico? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CONTROL, null,
            "$COL_ID = ?", arrayOf(id.toString()),
            null, null, null
        )
        val control = if (cursor.moveToFirst()) cursorToControl(cursor) else null
        cursor.close()
        db.close()
        return control
    }

    fun eliminarControl(id: Long): Boolean {
        val db = writableDatabase
        val rows = db.delete(TABLE_CONTROL, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    private fun cursorToControl(cursor: android.database.Cursor): ControlMedico {
        return ControlMedico(
            id             = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
            nombres        = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRES)),
            edad           = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EDAD)),
            peso           = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PESO)),
            altura         = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ALTURA)),
            presionArterial = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRESION)),
            comentario     = cursor.getString(cursor.getColumnIndexOrThrow(COL_COMENTARIO)) ?: "",
            fecha          = cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA)),
            hora           = cursor.getString(cursor.getColumnIndexOrThrow(COL_HORA))
        )
    }
}
