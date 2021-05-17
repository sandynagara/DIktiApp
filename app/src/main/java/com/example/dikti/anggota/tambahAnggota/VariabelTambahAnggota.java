package com.example.dikti.anggota.tambahAnggota;

import com.google.firebase.firestore.GeoPoint;

public class VariabelTambahAnggota {

    private String Departemen,pesanToken,Email,Line,Password,status,Username,queryNama,queryangkatan,querycampuran,querydepartemen,NIM,Telepon,Foto,namaLengkap,aboutMe,alamat,Angkatan;
    private GeoPoint Posisi;

    public VariabelTambahAnggota(String departemen, String pesanToken, String email, String line, String password, String status, String username, String queryNama, String queryangkatan, String querycampuran, String querydepartemen, String NIM, String telepon, String foto, String namaLengkap, String aboutMe, String alamat, String angkatan, GeoPoint posisi) {
        Departemen = departemen;
        this.pesanToken = pesanToken;
        Email = email;
        Line = line;
        Password = password;
        this.status = status;
        Username = username;
        this.queryNama = queryNama;
        this.queryangkatan = queryangkatan;
        this.querycampuran = querycampuran;
        this.querydepartemen = querydepartemen;
        this.NIM = NIM;
        Telepon = telepon;
        Foto = foto;
        this.namaLengkap = namaLengkap;
        this.aboutMe = aboutMe;
        this.alamat = alamat;
        Angkatan = angkatan;
        Posisi = posisi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GeoPoint getPosisi() {
        return Posisi;
    }

    public void setPosisi(GeoPoint posisi) {
        Posisi = posisi;
    }

    public String getPesanToken() {
        return pesanToken;
    }

    public void setPesanToken(String pesanToken) {
        this.pesanToken = pesanToken;
    }

    public String getAngkatan() {
        return Angkatan;
    }

    public void setAngkatan(String angkatan) {
        Angkatan = angkatan;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }


    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getTelepon() {
        return Telepon;
    }

    public void setTelepon(String telepon) {
        Telepon = telepon;
    }

    public VariabelTambahAnggota(){
    }

    public String getDepartemen() {
        return Departemen;
    }

    public void setDepartemen(String departemen) {
        Departemen = departemen;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getQueryNama() {
        return queryNama;
    }

    public void setQueryNama(String queryNama) {
        this.queryNama = queryNama;
    }

    public String getQueryangkatan() {
        return queryangkatan;
    }

    public void setQueryangkatan(String queryangkatan) {
        this.queryangkatan = queryangkatan;
    }

    public String getQuerycampuran() {
        return querycampuran;
    }

    public void setQuerycampuran(String querycampuran) {
        this.querycampuran = querycampuran;
    }

    public String getQuerydepartemen() {
        return querydepartemen;
    }

    public void setQuerydepartemen(String querydepartemen) {
        this.querydepartemen = querydepartemen;
    }

    public String getNIM() {
        return NIM;
    }

    public void setNIM(String NIM) {
        this.NIM = NIM;
    }
}
