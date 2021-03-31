package com.example.dikti.lombaBeasiswa.Beasiswa;

public class VariabelBeasiswa {
    private String nama,deadlineBulan,deskripsi,foto,link,queryNama,pengirim;
    private Long deadline,deadlineTahun,deadlineTanggal;
    private transient String key;
    private Boolean favorit;

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
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


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getQueryNama() {
        return queryNama;
    }

    public void setQueryNama(String queryNama) {
        this.queryNama = queryNama;
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
}
