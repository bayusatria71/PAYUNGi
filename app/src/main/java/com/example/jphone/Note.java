package com.example.jphone;

import java.util.Date;

public class Note {
    private Date tanggalPeminjaman;
    private Date tanggalDikembalikan;
    private int harga;

    public Note(){
        //emmpty;
    }

    public Note(Date tanggalPeminjaman, Date tanggalDikembalikan, int harga){
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalDikembalikan = tanggalDikembalikan;
        this.harga = harga;
    }

    public Date getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }

    public void setTanggalPeminjaman(Date tanggalPeminjaman) {
        this.tanggalPeminjaman = tanggalPeminjaman;
    }

    public Date getTanggalDikembalikan() {
        return tanggalDikembalikan;
    }

    public void setTanggalDikembalikan(Date tanggalDikembalikan) {
        this.tanggalDikembalikan = tanggalDikembalikan;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}
