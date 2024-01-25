package com.example.watch_selling.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "khach_hang")
public class Customer {
    @Id
    @SequenceGenerator(name = "customer_sequence", sequenceName = "customer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cmnd;

    private String ho;

    private String ten;

    private String gioitinh;

    private Date ngaysinh;

    private String diachi;

    private String sdt;

    private String email;

    private String masothue;

    private String password;

    private String salt;

    public Customer() {

    }

    public Customer(Long id, String cmnd, String ho, String ten, String gioitinh, Date ngaysinh, String diachi,
            String sdt, String email, String masothue, String password, String salt) {
        if (!isEmail(email)) {
            throw new ExceptionInInitializerError("Email is not valid");
        }

        this.id = id;
        this.cmnd = cmnd;
        this.ho = ho;
        this.ten = ten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.email = email;
        this.masothue = masothue;
        this.password = password;
        this.salt = salt;
    }

    public Customer(String cmnd, String ho, String ten, String gioitinh, Date ngaysinh, String diachi,
            String sdt, String email, String masothue, String password) {
        if (!isEmail(email)) {
            throw new ExceptionInInitializerError("Email is not valid");
        }

        this.cmnd = cmnd;
        this.ho = ho;
        this.ten = ten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.email = email;
        this.masothue = masothue;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!isEmail(email)) {
            throw new ExceptionInInitializerError("Email is not valid");
        }
        this.email = email;
    }

    public String getMasothue() {
        return masothue;
    }

    public void setMasothue(String masothue) {
        this.masothue = masothue;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    private Boolean isEmail(String email) {
        return email.contains("@");
    }
}