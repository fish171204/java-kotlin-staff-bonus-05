package com.example.baicongdiem05_002_nguyendangkhoa

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baicongdiem05_002_nguyendangkhoa.Adapter.EmployeeAdapter
import com.example.baicongdiem05_002_nguyendangkhoa.DAO.EmployeeDAO
import com.example.baicongdiem05_002_nguyendangkhoa.Source.Employee

import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var employeeAdapter: EmployeeAdapter
    private lateinit var employeeDAO: EmployeeDAO
    private lateinit var btnThem: MaterialButton
    private lateinit var edtHoVaTen: TextInputEditText
    private lateinit var edtNgaySinh: TextInputEditText
    private lateinit var edtNgayVaoLam: TextInputEditText
    private lateinit var edtPhuCap: TextInputEditText
    private lateinit var edtHeSoLuong: TextInputEditText
    private lateinit var edtThamNien: TextInputEditText
    private lateinit var edtLuong: TextInputEditText
    private lateinit var btnTinhLuong: MaterialButton
    private lateinit var btnXoa: MaterialButton
    private lateinit var btnSua: MaterialButton
    private lateinit var btnThoat: MaterialButton
    private var selectedEmployee: Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerChucVu: MaterialAutoCompleteTextView = findViewById(R.id.spinnerChucVu)
        val spinnerPhongBan: MaterialAutoCompleteTextView = findViewById(R.id.spinnerPhong)

        val chucVuList = listOf("Nhân viên", "Trưởng phòng", "Giám đốc")
        val chucVuAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, chucVuList)
        spinnerChucVu.setAdapter(chucVuAdapter)

        val phongBanList = listOf("Kinh doanh", "Kế toán", "Nhân sự")
        val phongBanAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, phongBanList)
        spinnerPhongBan.setAdapter(phongBanAdapter)


        edtNgaySinh = findViewById(R.id.edtNgaySinh)
        edtNgayVaoLam = findViewById(R.id.edtNgayVaoLam)
        edtPhuCap = findViewById(R.id.edtPhuCap)
        edtHeSoLuong = findViewById(R.id.edtHeSoLuong)
        edtLuong = findViewById(R.id.edtLuong)
        edtThamNien = findViewById(R.id.edtThamNien)
        btnTinhLuong = findViewById(R.id.btnTinhLuong)
        btnXoa = findViewById(R.id.btnXoa)
        btnThoat = findViewById(R.id.btnThoat)
        btnSua = findViewById(R.id.btnSua)



        btnTinhLuong.setOnClickListener {
            tinhLuong()
        }

        edtNgaySinh.setOnClickListener {
            showDatePickerDialog { date -> edtNgaySinh.setText(date) }
        }

        edtNgayVaoLam.setOnClickListener {
            showDatePickerDialog { date -> edtNgayVaoLam.setText(date) }
        }


        employeeDAO = EmployeeDAO(this)

        recyclerView = findViewById(R.id.recyclerView)
        btnThem = findViewById(R.id.btnThem)
        edtHoVaTen = findViewById(R.id.edtHoVaTen)

        recyclerView.layoutManager = LinearLayoutManager(this)
        employeeAdapter = EmployeeAdapter(emptyList()) { employee ->
            selectedEmployee = employee
            Toast.makeText(this, "Đã chọn: ${employee.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = employeeAdapter



        btnSua.setOnClickListener {
            selectedEmployee?.let {
                openUpdateScreen(it)
            } ?: Toast.makeText(this, "Chưa chọn nhân viên để sửa!", Toast.LENGTH_SHORT).show()
        }


        btnXoa.setOnClickListener {
            selectedEmployee?.let { deleteEmployee(it) } ?:
            Toast.makeText(this, "Chưa chọn nhân viên để xóa!", Toast.LENGTH_SHORT).show()
        }

        btnThem.setOnClickListener {
            val name = edtHoVaTen.text?.toString()?.trim()
            if (name.isNullOrEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên nhân viên!", Toast.LENGTH_SHORT).show()
            } else {
                val gender = "Nam" // TODO: Lấy từ RadioButton (nếu có)
                val birthday = edtNgaySinh.text?.toString()?.trim() ?: ""
                val joinDate = edtNgayVaoLam.text?.toString()?.trim() ?: ""
                val phone = findViewById<TextInputEditText>(R.id.edtDienThoai).text?.toString()?.trim() ?: ""
                val position = spinnerChucVu.text?.toString()?.trim() ?: ""
                val department = spinnerPhongBan.text?.toString()?.trim() ?: ""
                val salaryFactor = edtHeSoLuong.text?.toString()?.toDoubleOrNull() ?: 1.0
                val allowance = edtPhuCap.text?.toString()?.toDoubleOrNull() ?: 0.0

                val baseSalary = 1_490_000
                val joinYear = joinDate.takeLast(4).toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
                val experienceYears = (Calendar.getInstance().get(Calendar.YEAR) - joinYear).coerceAtLeast(0)
                val salary = (baseSalary * salaryFactor * 1.25) + 4_500_000 + (experienceYears * 0.01 * baseSalary)

                val employee = Employee(
                    name = name,
                    gender = gender,
                    birthday = birthday,
                    joinDate = joinDate,
                    phone = phone,
                    position = position,
                    department = department,
                    salaryFactor = salaryFactor,
                    allowance = allowance,
                    salary = salary
                )

                if (employeeDAO.insertEmployee(employee)) {
                    Toast.makeText(this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show()
                    loadEmployeeList()
                } else {
                    Toast.makeText(this, "Thêm nhân viên thất bại!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnThoat.setOnClickListener { finish() }


        loadEmployeeList()

    }


    private fun openUpdateScreen(employee: Employee) {
        val intent = Intent(this, ActivityUpdate::class.java).apply {
            putExtra("EMPLOYEE_ID", employee.id)
            putExtra("NAME", employee.name)
            putExtra("GENDER", employee.gender)
            putExtra("BIRTHDAY", employee.birthday)
            putExtra("JOIN_DATE", employee.joinDate)
            putExtra("PHONE", employee.phone)
            putExtra("POSITION", employee.position)
            putExtra("DEPARTMENT", employee.department)
            putExtra("SALARY_FACTOR", employee.salaryFactor)
            putExtra("ALLOWANCE", employee.allowance)
            putExtra("BASE_SALARY", 1_490_000)
        }
        startActivityForResult(intent, REQUEST_CODE_UPDATE)
    }



    companion object {
        private const val REQUEST_CODE_UPDATE = 101
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK) {
            loadEmployeeList()
        }
    }

    private fun loadEmployeeList() {
        val employeeList = employeeDAO.getAllEmployees()
        employeeAdapter = EmployeeAdapter(employeeList) { employee ->
            selectedEmployee = employee
            fillEmployeeData(employee)
            Toast.makeText(this, "Đã chọn: ${employee.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = employeeAdapter
        employeeAdapter.notifyDataSetChanged()
    }

    private fun fillEmployeeData(employee: Employee) {
        edtHoVaTen.setText(employee.name)
        edtNgaySinh.setText(employee.birthday)
        edtNgayVaoLam.setText(employee.joinDate)
        findViewById<TextInputEditText>(R.id.edtDienThoai).setText(employee.phone)
        findViewById<MaterialAutoCompleteTextView>(R.id.spinnerChucVu).setText(employee.position, false)
        findViewById<MaterialAutoCompleteTextView>(R.id.spinnerPhong).setText(employee.department, false)
        edtHeSoLuong.setText(employee.salaryFactor.toString())
        edtPhuCap.setText(employee.allowance.toString())
    }


    private fun deleteEmployee(employee: Employee) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa nhân viên ${employee.name} không?")
            .setPositiveButton("Xóa") { _, _ ->
                if (employeeDAO.deleteEmployee(employee.id)) {
                    Toast.makeText(this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show()
                    loadEmployeeList()
                } else {
                    Toast.makeText(this, "Không tìm thấy nhân viên để xóa!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }



    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                onDateSelected(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun tinhLuong() {
        val luongCoBan = 1_490_000
        val phuCap = edtPhuCap.text.toString().toDoubleOrNull() ?: 0.0
        val heSoLuong = edtHeSoLuong.text.toString().toDoubleOrNull() ?: 0.0
        val namHienTai = Calendar.getInstance().get(Calendar.YEAR)

        val ngayVaoLamText = edtNgayVaoLam.text.toString().trim()
        val namVaoLam = ngayVaoLamText.takeLast(4).toIntOrNull() ?: namHienTai

        val thamNien = (namHienTai - namVaoLam).coerceAtLeast(0)
        edtThamNien.setText(thamNien.toString())

        val luong = (luongCoBan * heSoLuong * 1.25) + 4_500_000 + (thamNien * 0.01 * luongCoBan)

        val formatVN = NumberFormat.getInstance(Locale("vi", "VN"))
        edtLuong.setText(formatVN.format(luong))

        Toast.makeText(this, "Lương nhân viên: ${formatVN.format(luong)} VNĐ", Toast.LENGTH_SHORT).show()
    }


}
