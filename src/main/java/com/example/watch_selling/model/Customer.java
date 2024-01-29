package com.example.watch_selling.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "khach_hang")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "makh", nullable = false, unique = true)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String cmnd;

    @Column(nullable = false)
    private String ho;

    @Column(nullable = false)
    private String ten;

    @Column(nullable = false)
    private String gioitinh;

    @Column(nullable = false)
    private Date ngaysinh;

    @Column(nullable = false)
    private String diachi;

    @Column(nullable = false)
    private String sdt;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String masothue;

    @Column(name = "da_xoa")
    private Boolean daXoa = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", referencedColumnName = "id", unique = true, nullable = false)
    private Account account;

    public Customer() {
    }

    public Customer(UUID id, String cmnd, String ho, String ten, String gioitinh, Date ngaysinh, String diachi,
            String sdt, String email, String masothue, Boolean daXoa, Account account) {
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
        this.daXoa = daXoa;
        this.account = account;
    }

    public Customer(String cmnd, String ho, String ten, String gioitinh, Date ngaysinh, String diachi, String sdt,
            String email, String masothue, Boolean daXoa, Account account) {
        this.cmnd = cmnd;
        this.ho = ho;
        this.ten = ten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.email = email;
        this.masothue = masothue;
        this.daXoa = daXoa;
        this.account = account;
    }

    public Customer(String cmnd, String ho, String ten, String gioitinh, Date ngaysinh, String diachi, String sdt,
            String email, String masothue, Boolean daXoa) {
        this.cmnd = cmnd;
        this.ho = ho;
        this.ten = ten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.email = email;
        this.masothue = masothue;
        this.daXoa = daXoa;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
        this.email = email;
    }

    public String getMasothue() {
        return masothue;
    }

    public void setMasothue(String masothue) {
        this.masothue = masothue;
    }

    public Boolean getDaXoa() {
        return daXoa;
    }

    public void setDaXoa(Boolean daXoa) {
        this.daXoa = daXoa;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}