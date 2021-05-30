package id.example.repositorypln.model;

public class Permintaan {
    public String key;
    public String nama;
    public String tempat;
    public String tanggal;
    public String statusPermintaan;
    public String namaB;
    public String keyB;
    public String hargaB;
    public String noInventarisB;
    public String statusB;
    public String keteranganB;

    public Permintaan() {
    }

    public Permintaan(String tanggal, String nama, String tempat, String statusPermintaan, String namaB, String keyB, String noInventarisB, String statusB, String keteranganB, String hargaB) {
        this.nama = nama;
        this.tempat = tempat;
        this.statusPermintaan = statusPermintaan;
        this.namaB = namaB;
        this.tanggal = tanggal;
        this.keyB = keyB;
        this.noInventarisB = noInventarisB;
        this.statusB = statusB;
        this.keteranganB = keteranganB;
        this.hargaB = hargaB;
    }

    public String getHargaB() {
        return hargaB;
    }

    public void setHargaB(String hargaB) {
        this.hargaB = hargaB;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getStatusPermintaan() {
        return statusPermintaan;
    }

    public void setStatusPermintaan(String statusPermintaan) {
        this.statusPermintaan = statusPermintaan;
    }

    public String getNamaB() {
        return namaB;
    }

    public void setNamaB(String namaB) {
        this.namaB = namaB;
    }

    public String getKeyB() {
        return keyB;
    }

    public void setKeyB(String keyB) {
        this.keyB = keyB;
    }

    public String getNoInventarisB() {
        return noInventarisB;
    }

    public void setNoInventarisB(String noInventarisB) {
        this.noInventarisB = noInventarisB;
    }

    public String getStatusB() {
        return statusB;
    }

    public void setStatusB(String statusB) {
        this.statusB = statusB;
    }

    public String getKeteranganB() {
        return keteranganB;
    }

    public void setKeteranganB(String keteranganB) {
        this.keteranganB = keteranganB;
    }
}
