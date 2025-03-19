package com.example.baicongdiem05_002_nguyendangkhoa

import android.app.Activity
import android.icu.text.NumberFormat
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.baicongdiem05_002_nguyendangkhoa.DAO.EmployeeDAO
import com.example.baicongdiem05_002_nguyendangkhoa.Source.Employee
import java.util.Locale

class ActivityUpdate : AppCompatActivity() {
    private lateinit var btnLuu: Button
    private lateinit var edtHoVaTen: EditText
    private lateinit var rdbNam: RadioButton
    private lateinit var rdbNu: RadioButton
    private lateinit var edtNgaySinh: EditText
    private lateinit var edtNgayVaoLam: EditText
    private lateinit var edtDienThoai: EditText
    private lateinit var spinnerChucVu: AutoCompleteTextView
    private lateinit var spinnerPhongBan: AutoCompleteTextView
    private lateinit var edtEmployeeId: EditText

    private var employeeId: Int = -1
    private var salaryFactor: Double = 1.0
    private var allowance: Double = 0.0
    private var baseSalary: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        btnLuu = findViewById(R.id.btnLuu2)
        edtHoVaTen = findViewById(R.id.edtHoVaTen23)
        rdbNam = findViewById(R.id.rdbNam23)
        rdbNu = findViewById(R.id.rdbNu23)
        edtNgaySinh = findViewById(R.id.edtNgaySinh23)
        edtNgayVaoLam = findViewById(R.id.edtNgayVaoLam23)
        edtDienThoai = findViewById(R.id.edtDienThoai23)
        spinnerChucVu = findViewById(R.id.spinnerChucVu23)
        spinnerPhongBan = findViewById(R.id.spinnerPhong23)
        edtEmployeeId = findViewById(R.id.edtMaNV23)

        employeeId = intent.getIntExtra("EMPLOYEE_ID", -1)
        Log.d("DEBUG", "Received employeeId: $employeeId") // Kiểm tra ID nhận được

        if (employeeId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID nhân viên!", Toast.LENGTH_LONG).show()
            finish()
            return
        }



        edtEmployeeId.setText(employeeId.toString())
        edtHoVaTen.setText(intent.getStringExtra("NAME"))
        if (intent.getStringExtra("GENDER") == "Nam") rdbNam.isChecked = true else rdbNu.isChecked = true
        edtNgaySinh.setText(intent.getStringExtra("BIRTHDAY"))
        edtNgayVaoLam.setText(intent.getStringExtra("JOIN_DATE"))
        edtDienThoai.setText(intent.getStringExtra("PHONE"))
        spinnerChucVu.setText(intent.getStringExtra("POSITION"), false)
        spinnerPhongBan.setText(intent.getStringExtra("DEPARTMENT"), false)

        salaryFactor = intent.getDoubleExtra("SALARY_FACTOR", 1.0)
        allowance = intent.getDoubleExtra("ALLOWANCE", 0.0)
        baseSalary = intent.getDoubleExtra("BASE_SALARY", 0.0)

        btnLuu.setOnClickListener {
            val updatedEmployee = Employee(
                id = employeeId,
                name = edtHoVaTen.text.toString().trim(),
                gender = if (rdbNam.isChecked) "Nam" else "Nữ",
                birthday = edtNgaySinh.text.toString(),
                joinDate = edtNgayVaoLam.text.toString(),
                phone = edtDienThoai.text.toString(),
                position = spinnerChucVu.text.toString(),
                department = spinnerPhongBan.text.toString(),
                salaryFactor = salaryFactor,
                allowance = allowance,
                salary = baseSalary * salaryFactor + allowance
            )

            val employeeDAO = EmployeeDAO(this)
            if (employeeDAO.updateEmployee(updatedEmployee)) {
                val formatVN = NumberFormat.getInstance(Locale("vi", "VN"))
                Toast.makeText(this, "Lương cập nhật: ${formatVN.format(updatedEmployee.salary)} VNĐ", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
