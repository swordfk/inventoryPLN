package id.example.repositorypln.model;

public class Barang {
    public String key;
    public String nama;
    public String noInventaris;
    public String status;
    public String keterangan;
    public String harga;

    public Barang(String nama, String noInventaris, String status, String keterangan, String harga) {
        this.nama = nama;
        this.noInventaris = noInventaris;
        this.status = status;
        this.keterangan = keterangan;
        this.harga = harga;
    }

    public Barang() {
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoInventaris() {
        return noInventaris;
    }

    public void setNoInventaris(String noInventaris) {
        this.noInventaris = noInventaris;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
