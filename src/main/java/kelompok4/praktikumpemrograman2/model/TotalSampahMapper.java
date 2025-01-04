package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TotalSampahMapper {

//    @Select("SELECT ts.*, jk.nama as nama_jenis, ts.berat_kg as berat_kg " +
//            "FROM total_sampah ts " +
//            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = CAST(jk.id AS VARCHAR)")
//
//    List<TotalSampah> getAll();

    @Results(id = "totalSampahResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "tanggal", column = "tanggal"),
            @Result(property = "jenisSampah", column = "jenis_sampah"),
            @Result(property = "totalBerat", column = "total_berat"),
            @Result(property = "totalHarga", column = "total_harga"),
            @Result(property = "beratKg", column = "berat_kg"),
            @Result(property = "permintaanId", column = "permintaan_id"),
            @Result(property = "namaJenis", column = "nama_jenis")
    })
    @Select("SELECT ts.*, jk.nama as nama_jenis " +
            "FROM total_sampah ts " +
            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = jk.id")
    List<TotalSampah> getAll();

    @Select("SELECT ts.*, jk.nama as nama_jenis, ts.berat_kg as berat_kg " +
            "FROM total_sampah ts " +
            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = CAST(jk.id AS VARCHAR) " +
            "WHERE ts.tanggal = #{tanggal}")
    List<TotalSampah> getTotalSampahByTanggal(@Param("tanggal") String tanggal);

    @Select("SELECT ts.*, jk.nama as nama_jenis, ts.berat_kg as berat_kg " +
            "FROM total_sampah ts " +
            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = CAST(jk.id AS VARCHAR) " +
            "WHERE ts.tanggal BETWEEN #{startDate} AND #{endDate}")
    List<TotalSampah> getTotalSampahByWeek(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT ts.*, jk.nama as nama_jenis, ts.berat_kg as berat_kg " +
            "FROM total_sampah ts " +
            "LEFT JOIN jenis_kategori jk ON ts.jenis_sampah = CAST(jk.id AS VARCHAR) " +
            "WHERE ts.tanggal BETWEEN #{startDate} AND #{endDate}")
    List<TotalSampah> getTotalSampahByMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
