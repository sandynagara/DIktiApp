package com.example.dikti.anggota;

public class VariabelFilterDepartemen {
    private String Departemen,Angkatan;

    public VariabelFilterDepartemen(String departemen, String angkatan) {
        Departemen = departemen;
        Angkatan = angkatan;
    }

    public String getAngkatan() {
        return Angkatan;
    }

    public void setAngkatan(String angkatan) {
        Angkatan = angkatan;
    }

    public String getDepartemen() {
        return Departemen;
    }

    public void setDepartemen(String departemen) {

        Departemen = departemen;
    }


    public VariabelFilterDepartemen(){};
}
