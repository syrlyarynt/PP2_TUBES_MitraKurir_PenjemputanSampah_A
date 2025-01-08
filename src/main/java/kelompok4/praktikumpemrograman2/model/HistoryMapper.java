package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface HistoryMapper {
    @Select("SELECT h.*, pa.* FROM history h " +
            "LEFT OUTER JOIN pickup_assignments pa ON h.pickup_assignment_id = pa.id")
    @Results(id = "historyResultMap", value = {
            @Result(property = "idRiwayat", column = "idRiwayat"),
            @Result(property = "waktuSelesai", column = "waktuSelesai"),
            @Result(property = "lokasi", column = "lokasi"),
            @Result(property = "kategoriSampah", column = "kategoriSampah"),
            @Result(property = "beratSampah", column = "beratSampah"),
            @Result(property = "harga", column = "harga"),
            @Result(property = "statusPenyelesaian", column = "statusPenyelesaian"),
            @Result(property = "pickupAssignmentId", column = "pickup_assignment_id")
    })
    List<History> getAllHistory();

    @Select("SELECT * FROM history WHERE idRiwayat = #{id}")
    @ResultMap("historyResultMap")
    History getHistoryById(@Param("id") int id);

    @Insert("INSERT INTO history (pickup_assignment_id, waktuSelesai, lokasi, " +
            "kategoriSampah, beratSampah, harga, statusPenyelesaian) " +
            "VALUES (#{pickupAssignmentId}, #{waktuSelesai}, #{lokasi}, " +
            "#{kategoriSampah}, #{beratSampah}, #{harga}, #{statusPenyelesaian})")
    @Options(useGeneratedKeys = true, keyProperty = "idRiwayat")
    void insertHistory(History history);

    @Update("UPDATE history SET pickup_assignment_id = #{pickupAssignmentId}, " +
            "waktuSelesai = #{waktuSelesai}, lokasi = #{lokasi}, " +
            "kategoriSampah = #{kategoriSampah}, beratSampah = #{beratSampah}, " +
            "harga = #{harga}, statusPenyelesaian = #{statusPenyelesaian} " +
            "WHERE idRiwayat = #{idRiwayat}")
    void updateHistory(History history);

    @Delete("DELETE FROM history WHERE idRiwayat = #{id}")
    void deleteHistory(@Param("id") int id);
}