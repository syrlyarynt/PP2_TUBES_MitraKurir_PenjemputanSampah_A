package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface HistoryMapper {
    @Select("SELECT * FROM history")
    @Results({
            @Result(property = "idRiwayat", column = "idRiwayat"),
            @Result(property = "waktuSelesai", column = "waktuSelesai"),
            @Result(property = "lokasi", column = "lokasi"),
            @Result(property = "kategoriSampah", column = "kategoriSampah"),
            @Result(property = "beratSampah", column = "beratSampah"),
            @Result(property = "harga", column = "harga"),
            @Result(property = "statusPenyelesaian", column = "statusPenyelesaian")
    })
    List<History> getAllHistory();

    @Select("SELECT * FROM permintaanpenjemputan WHERE IDpermintaan = #{id}")
    @Results({
            @Result(property = "idRiwayat", column = "IDpermintaan"),
            @Result(property = "waktuSelesai", column = "waktuPermintaan"),
            @Result(property = "lokasi", column = "alamat"),
            @Result(property = "kategoriSampah", column = "kategoriSampah"),
            @Result(property = "beratSampah", column = "berat"),
            @Result(property = "harga", column = "harga"),
            @Result(property = "statusPenyelesaian", column = "status")
    })
    History getHistoryById(@Param("id") int id);

    @Insert("INSERT INTO permintaanpenjemputan (waktuPermintaan, alamat, kategoriSampah, berat, harga, status) " +
            "VALUES (#{waktuSelesai}, #{lokasi}, #{kategoriSampah}, #{beratSampah}, #{harga}, #{statusPenyelesaian})")
    @Options(useGeneratedKeys = true, keyProperty = "idRiwayat", keyColumn = "IDpermintaan")
    void insertHistory(History history);

    @Update("UPDATE permintaanpenjemputan SET waktuPermintaan = #{waktuSelesai}, alamat = #{lokasi}, " +
            "kategoriSampah = #{kategoriSampah}, berat = #{beratSampah}, " +
            "harga = #{harga}, status = #{statusPenyelesaian} " +
            "WHERE IDpermintaan = #{idRiwayat}")
    void updateHistory(History history);

    @Delete("DELETE FROM permintaanpenjemputan WHERE IDpermintaan = #{id}")
    void deleteHistory(@Param("id") int id);
}