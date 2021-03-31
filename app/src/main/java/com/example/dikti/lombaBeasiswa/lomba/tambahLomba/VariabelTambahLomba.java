package com.example.dikti.lombaBeasiswa.lomba.tambahLomba;

import java.util.List;

public class VariabelTambahLomba {
    private String deadlineBulan,foto,deskripsi,nama,queryJenis,queryNama,peserta,jenis,link,pengirim;
    private Long biaya,deadline,deadlineTahun,deadlineTanggal;
    private Boolean favorit;
    List<String> save;

    public VariabelTambahLomba(String deadlineBulan, String foto, String deskripsi, String nama, String queryJenis, String queryNama, String peserta, String jenis, String link, String pengirim, Long biaya, Long deadline, Long deadlineTahun, Long deadlineTanggal, Boolean favorit, List<String> save) {
        this.deadlineBulan = deadlineBulan;
        this.foto = foto;
        this.deskripsi = deskripsi;
        this.nama = nama;
        this.queryJenis = queryJenis;
        this.queryNama = queryNama;
        this.peserta = peserta;
        this.jenis = jenis;
        this.link = link;
        this.pengirim = pengirim;
        this.biaya = biaya;
        this.deadline = deadline;
        this.deadlineTahun = deadlineTahun;
        this.deadlineTanggal = deadlineTanggal;
        this.favorit = favorit;
        this.save = save;
    }

    public Boolean getFavorit() {
        return favorit;
    }

    public void setFavorit(Boolean favorit) {
        this.favorit = favorit;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public List<String> getSave() {
        return save;
    }

    public void setSave(List<String> save) {
        this.save = save;
    }

    public VariabelTambahLomba(){}

    public String getDeadlineBulan() {
        return deadlineBulan;
    }

    public void setDeadlineBulan(String deadlineBulan) {
        this.deadlineBulan = deadlineBulan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getQueryJenis() {
        return queryJenis;
    }

    public void setQueryJenis(String queryJenis) {
        this.queryJenis = queryJenis;
    }

    public String getQueryNama() {
        return queryNama;
    }

    public void setQueryNama(String queryNama) {
        this.queryNama = queryNama;
    }

    public String getPeserta() {
        return peserta;
    }

    public void setPeserta(String peserta) {
        this.peserta = peserta;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getBiaya() {
        return biaya;
    }

    public void setBiaya(Long biaya) {
        this.biaya = biaya;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public Long getDeadlineTahun() {
        return deadlineTahun;
    }

    public void setDeadlineTahun(Long deadlineTahun) {
        this.deadlineTahun = deadlineTahun;
    }

    public Long getDeadlineTanggal() {
        return deadlineTanggal;
    }

    public void setDeadlineTanggal(Long deadlineTanggal) {
        this.deadlineTanggal = deadlineTanggal;
    }
}
