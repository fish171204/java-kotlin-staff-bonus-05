package com.example.baicongdiem05_002_nguyendangkhoa.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.baicongdiem05_002_nguyendangkhoa.R
import com.example.baicongdiem05_002_nguyendangkhoa.Source.Employee

class EmployeeAdapter(
    private var employeeList: List<Employee>,
    private val onItemClick: (Employee) -> Unit
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {


    fun setEmployees(employees: List<Employee>) {
        this.employeeList = employees
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return EmployeeViewHolder(view)
    }


    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employeeList[position]
        holder.bind(employee)
        holder.itemView.setOnClickListener { onItemClick(employee) }


    }

    override fun getItemCount() = employeeList.size

    class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTen: TextView = view.findViewById(R.id.tv_ten)
        private val tvMaNV: TextView = view.findViewById(R.id.tv_MaNV)
        private val tvHoTen: TextView = view.findViewById(R.id.tv_ho_ten)
        private val tvNgaySinh: TextView = view.findViewById(R.id.tv_ngay_sinh)
        private val tvNgayVaoLam: TextView = view.findViewById(R.id.tv_ngay_vao_lam)
        private val tvGioiTinh: TextView = view.findViewById(R.id.tv_gioi_tinh)
        private val tvLuong: TextView = view.findViewById(R.id.tv_luong)
        private val tvDienThoai: TextView = view.findViewById(R.id.tv_dien_thoai)
        private val tvChucVu: TextView = view.findViewById(R.id.tv_chuc_vu)
        private val tvPhongBan: TextView = view.findViewById(R.id.tv_phong_ban)

        fun bind(employee: Employee) {
            tvTen.text = employee.name
            tvMaNV.text = "Mã NV: ${employee.id}"
            tvGioiTinh.text = "Giới tính: ${employee.gender}"
            tvHoTen.text = "Họ tên: ${employee.name}"
            tvNgaySinh.text = "Ngày sinh: ${employee.birthday}"
            tvNgayVaoLam.text = "Ngày vào làm: ${employee.joinDate}"
            tvDienThoai.text = "Điện thoại: ${employee.phone}" //
            tvChucVu.text = "Chức vụ: ${employee.position}" //
            tvPhongBan.text = "Phòng ban: ${employee.department}" //
            tvLuong.text = "Lương: ${employee.salary}" //
        }


    }
}

