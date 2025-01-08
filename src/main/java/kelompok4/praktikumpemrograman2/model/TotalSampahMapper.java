package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TotalSampahMapper {

    @Results(id = "totalSampahResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "tanggal", column = "tanggal"),
            @Result(property = "jenisSampah", column = "jenis_sampah"),
            @Result(property = "totalBerat", column = "total_berat"),
            @Result(property = "totalHarga", column = "total_harga"),
            @Result(property = "beratKg", column = "berat_kg"),
            @Result(property = "permintaanId", column = "permintaan_id"),
            @Result(property = "namaJenis", column = "nama_jenis"),
            @Result(property = "namaPelanggan", column = "namaPelanggan"),
            @Result(property = "alamat", column = "alamat"),
            @Result(property = "kategoriSampah", column = "kategoriSampah"),
            @Result(property = "waktuPermintaan", column = "waktuPermintaan"),
            @Result(property = "status", column = "status"),
            @Result(property = "totalBiaya", column = "total_biaya")
    })
    @Select("SELECT ts.*, jk.nama as nama_jenis, p.namaPelanggan, p.alamat, p.kategoriSampah, " +
            "p.waktuPermintaan, p.status, p.total_biaya " +
            "FROM total_sampah ts " +
            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = jk.id " +
            "LEFT JOIN permintaanpenjemputan p ON ts.permintaan_id = p.IDpermintaan")
    List<TotalSampah> getAll();

    @ResultMap("totalSampahResultMap")
    @Select("SELECT ts.*, jk.nama as nama_jenis, p.namaPelanggan, p.alamat, p.kategoriSampah, " +
            "p.waktuPermintaan, p.status, p.total_biaya " +
            "FROM total_sampah ts " +
            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = jk.id " +
            "LEFT JOIN permintaanpenjemputan p ON ts.permintaan_id = p.IDpermintaan " +
            "WHERE DATE(ts.tanggal) = DATE(#{tanggal})")
    List<TotalSampah> getTotalSampahByTanggal(@Param("tanggal") String tanggal);

    @ResultMap("totalSampahResultMap")
    @Select("SELECT ts.*, jk.nama as nama_jenis, p.namaPelanggan, p.alamat, p.kategoriSampah, " +
            "p.waktuPermintaan, p.status, p.total_biaya " +
            "FROM total_sampah ts " +
            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = jk.id " +
            "LEFT JOIN permintaanpenjemputan p ON ts.permintaan_id = p.IDpermintaan " +
            "WHERE ts.tanggal BETWEEN #{startDate} AND #{endDate}")
    List<TotalSampah> getTotalSampahByWeek(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @ResultMap("totalSampahResultMap")
    @Select("SELECT ts.*, jk.nama as nama_jenis, p.namaPelanggan, p.alamat, p.kategoriSampah, " +
            "p.waktuPermintaan, p.status, p.total_biaya " +
            "FROM total_sampah ts " +
            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = jk.id " +
            "LEFT JOIN permintaanpenjemputan p ON ts.permintaan_id = p.IDpermintaan " +
            "WHERE ts.tanggal BETWEEN #{startDate} AND #{endDate}")
    List<TotalSampah> getTotalSampahByMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);
}