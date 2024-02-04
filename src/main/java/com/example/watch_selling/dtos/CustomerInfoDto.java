package com.example.watch_selling.dtos;

import java.util.Date;

import com.example.watch_selling.model.Customer;

public class CustomerInfoDto {
    private String cmnd;

    private String ho;

    private String ten;

    private String gioitinh;

    private Date ngaysinh;

    private String diachi;

    private String sdt;

    private String email;

    private String masothue;

    private String hinhAnh;

    private String jwt;

    public CustomerInfoDto() {
    }

    public CustomerInfoDto(String cmnd, String ho, String ten, String gioitinh, Date ngaysinh, String diachi, String sdt, String email, String masothue) {
        this.cmnd = cmnd;
        this.ho = ho;
        this.ten = ten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.email = email;
        this.masothue = masothue;
    }

    public CustomerInfoDto(Customer customer) {
        this.cmnd = customer.getCmnd();
        this.ho = customer.getHo();
        this.ten = customer.getTen();
        this.gioitinh = customer.getGioitinh();
        this.ngaysinh = customer.getNgaysinh();
        this.diachi = customer.getDiachi();
        this.sdt = customer.getSdt();
        this.email = customer.getEmail();
        this.masothue = customer.getMasothue();
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

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Customer makeCustomer() {
        Customer newCustomer = new Customer();

        newCustomer.setCmnd(cmnd);
        newCustomer.setEmail(email);
        newCustomer.setHo(ho);
        newCustomer.setTen(ten);
        newCustomer.setGioitinh(gioitinh);
        newCustomer.setNgaysinh(ngaysinh);
        newCustomer.setDiachi(diachi);
        newCustomer.setSdt(sdt);
        newCustomer.setMasothue(masothue);
        newCustomer.setHinhAnh(hinhAnh);

        return newCustomer;
    }
}
