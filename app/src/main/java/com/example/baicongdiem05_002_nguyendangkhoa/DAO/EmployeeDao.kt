package com.example.baicongdiem05_002_nguyendangkhoa.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.baicongdiem05_002_nguyendangkhoa.Database.DatabaseHelper
import com.example.baicongdiem05_002_nguyendangkhoa.Source.Employee

class EmployeeDAO(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertEmployee(employee: Employee): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, employee.name)
            put(DatabaseHelper.COLUMN_GENDER, employee.gender)
            put(DatabaseHelper.COLUMN_BIRTHDAY, employee.birthday)
            put(DatabaseHelper.COLUMN_JOIN_DATE, employee.joinDate)
            put(DatabaseHelper.COLUMN_PHONE, employee.phone)
            put(DatabaseHelper.COLUMN_POSITION, employee.position)
            put(DatabaseHelper.COLUMN_DEPARTMENT, employee.department)
            put(DatabaseHelper.COLUMN_SALARY_FACTOR, employee.salaryFactor)
            put(DatabaseHelper.COLUMN_ALLOWANCE, employee.allowance)
            put(DatabaseHelper.COLUMN_SALARY, employee.salary)
        }
        val result = db.insert(DatabaseHelper.TABLE_EMPLOYEE, null, values)
        db.close()
        return result != -1L
    }

    fun getAllEmployees(): List<Employee> {
        val employeeList = mutableListOf<Employee>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_EMPLOYEE}", null)

        if (cursor.moveToFirst()) {
            do {
                val employee = Employee(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                    gender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENDER)),
                    birthday = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIRTHDAY)),
                    joinDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_JOIN_DATE)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE)),
                    position = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POSITION)),
                    department = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTMENT)),
                    salaryFactor = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SALARY_FACTOR)),
                    allowance = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ALLOWANCE)),
                    salary = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SALARY)) // Lấy lương
                )
                employeeList.add(employee)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return employeeList
    }

    fun updateEmployee(employee: Employee): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, employee.name)
            put(DatabaseHelper.COLUMN_GENDER, employee.gender)
            put(DatabaseHelper.COLUMN_BIRTHDAY, employee.birthday)
            put(DatabaseHelper.COLUMN_JOIN_DATE, employee.joinDate)
            put(DatabaseHelper.COLUMN_PHONE, employee.phone)
            put(DatabaseHelper.COLUMN_POSITION, employee.position)
            put(DatabaseHelper.COLUMN_DEPARTMENT, employee.department)
            put(DatabaseHelper.COLUMN_SALARY_FACTOR, employee.salaryFactor)
            put(DatabaseHelper.COLUMN_ALLOWANCE, employee.allowance)
            put(DatabaseHelper.COLUMN_SALARY, employee.salary)
        }
        val result = db.update(DatabaseHelper.TABLE_EMPLOYEE, values, "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(employee.id.toString()))
        db.close()
        return result > 0
    }


    fun deleteEmployee(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val result = db.delete(DatabaseHelper.TABLE_EMPLOYEE, "${DatabaseHelper.COLUMN_ID}=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
}
