package com.example.jphone;

import java.util.Date;

public class Note {
    private Date tanggalPeminjaman;
    private Date tanggalDikembalikan;
    private int price;

    public Note(){
        //
    }

    public Note(Date tanggalPeminjaman, Date tanggalDikembalikan, int price){
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalDikembalikan = tanggalDikembalikan;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
