package com.example.jphone;

public class Note {
    private String tanggalPeminjaman;
    private String tanggalDikembalikan;
    private int harga;

    public Note(){
        //emmpty;
    }

    public Note(String tanggalPeminjaman, String tanggalDikembalikan){
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalDikembalikan = tanggalDikembalikan;
    }

    public String getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }

    public void setTanggalPeminjaman(String tanggalPeminjaman) {
        this.tanggalPeminjaman = tanggalPeminjaman;
    }

    public String getTanggalDikembalikan() {
        return tanggalDikembalikan;
    }

    public void setTanggalDikembalikan(String tanggalDikembalikan) {
        this.tanggalDikembalikan = tanggalDikembalikan;
    }
}
