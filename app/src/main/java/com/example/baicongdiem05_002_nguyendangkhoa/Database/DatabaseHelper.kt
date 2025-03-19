package com.example.baicongdiem05_002_nguyendangkhoa.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "EmployeeDB.db"
        const val DATABASE_VERSION = 2

        const val TABLE_EMPLOYEE = "Employees"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_BIRTHDAY = "birthday"
        const val COLUMN_JOIN_DATE = "join_date"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_POSITION = "position"
        const val COLUMN_DEPARTMENT = "department"
        const val COLUMN_SALARY_FACTOR = "salary_factor"
        const val COLUMN_ALLOWANCE = "allowance"
        const val COLUMN_SALARY = "salary"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
        CREATE TABLE $TABLE_EMPLOYEE (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT,
            $COLUMN_GENDER TEXT,
            $COLUMN_BIRTHDAY TEXT,
            $COLUMN_JOIN_DATE TEXT,
            $COLUMN_PHONE TEXT,
            $COLUMN_POSITION TEXT,
            $COLUMN_DEPARTMENT TEXT,
            $COLUMN_SALARY_FACTOR REAL,
            $COLUMN_ALLOWANCE REAL,
            $COLUMN_SALARY REAL
        )
    """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_EMPLOYEE ADD COLUMN $COLUMN_SALARY REAL DEFAULT 0")
        }
    }
    }
