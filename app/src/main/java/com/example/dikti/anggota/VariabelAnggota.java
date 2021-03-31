package com.example.dikti.anggota;

public class VariabelAnggota {
    private String namaLengkap,foto,departemen,angkatan;
    private transient String key;

    public VariabelAnggota(String namaLengkap, String foto, String departemen, String angkatan, String key) {
        this.namaLengkap = namaLengkap;
        this.foto = foto;
        this.departemen = departemen;
        this.angkatan = angkatan;
        this.key = key;
    }

    public String getAngkatan() {
        return angkatan;
    }

    public void setAngkatan(String angkatan) {
        this.angkatan = angkatan;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public VariabelAnggota(){}
}
