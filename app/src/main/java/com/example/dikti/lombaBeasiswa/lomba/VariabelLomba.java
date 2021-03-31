package com.example.dikti.lombaBeasiswa.lomba;

public class VariabelLomba {
    private String nama,deadlineBulan,deskripsi,foto,jenis,link,peserta,queryJenis,queryNama;
    private Long biaya,deadline,deadlineTahun,deadlineTanggal;
    private Boolean favorit;
    private transient String key;

    public VariabelLomba(String nama, String deadlineBulan, String deskripsi, String foto, String jenis, String link, String peserta, String queryJenis, String queryNama, Long biaya, Long deadline, Long deadlineTahun, Long deadlineTanggal, Boolean favorit, String key) {
        this.nama = nama;
        this.deadlineBulan = deadlineBulan;
        this.deskripsi = deskripsi;
        this.foto = foto;
        this.jenis = jenis;
        this.link = link;
        this.peserta = peserta;
        this.queryJenis = queryJenis;
        this.queryNama = queryNama;
        this.biaya = biaya;
        this.deadline = deadline;
        this.deadlineTahun = deadlineTahun;
        this.deadlineTanggal = deadlineTanggal;
        this.favorit = favorit;
        this.key = key;
    }

    public Boolean getFavorit() {
        return favorit;
    }

    public void setFavorit(Boolean favorit) {
        this.favorit = favorit;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeadlineBulan() {
        return deadlineBulan;
    }

    public void setDeadlineBulan(String deadlineBulan) {
        this.deadlineBulan = deadlineBulan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getPeserta() {
        return peserta;
    }

    public void setPeserta(String peserta) {
        this.peserta = peserta;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public VariabelLomba(){}
}
