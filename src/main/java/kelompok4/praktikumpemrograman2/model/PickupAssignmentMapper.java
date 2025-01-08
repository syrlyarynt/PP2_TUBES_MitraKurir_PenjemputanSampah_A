package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface PickupAssignmentMapper {
    @Select("SELECT * FROM pickup_assignments")
    @Results(id = "assignmentResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "permintaanId", column = "permintaan_id"),
            @Result(property = "dropboxId", column = "dropbox_id"),
            @Result(property = "status", column = "status"),
            @Result(property = "pickupDate", column = "pickup_date"),
            @Result(property = "completionDate", column = "completion_date"),
            @Result(property = "totalWeight", column = "total_weight"),
            @Result(property = "beratAktual", column = "berat_aktual"),
            @Result(property = "totalCost", column = "total_cost"),
            @Result(property = "hargaFinal", column = "harga_final"),
            @Result(property = "notes", column = "notes"),
            @Result(property = "permintaan", column = "permintaan_id",
                    javaType = PermintaanPenjemputan.class,
                    one = @One(select = "kelompok4.praktikumpemrograman2.model.PermintaanPenjemputanMapper.getPermintaanById")),
            @Result(property = "dropbox", column = "dropbox_id",
                    javaType = LokasiDropbox.class,
                    one = @One(select = "kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper.getDropboxById"))
    })
    List<PickupAssignment> getAllAssignments();

    @Select("SELECT * FROM pickup_assignments WHERE id = #{id}")
    @ResultMap("assignmentResultMap")
    PickupAssignment getAssignmentById(@Param("id") int id);

    @Select("SELECT * FROM pickup_assignments WHERE permintaan_id = #{permintaanId}")
    @ResultMap("assignmentResultMap")
    PickupAssignment getAssignmentByPermintaanId(@Param("permintaanId") int permintaanId);

    @Insert("INSERT INTO pickup_assignments (permintaan_id, dropbox_id, status, pickup_date, " +
            "completion_date, total_weight, berat_aktual, total_cost, harga_final, notes) " +
            "VALUES (#{permintaanId}, #{dropboxId}, #{status}, #{pickupDate}, " +
            "#{completionDate}, #{totalWeight}, #{beratAktual}, #{totalCost}, #{hargaFinal}, #{notes})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAssignment(PickupAssignment assignment);

    @Update("UPDATE pickup_assignments SET status = #{status}, " +
            "pickup_date = #{pickupDate}, completion_date = #{completionDate}, " +
            "total_weight = #{totalWeight}, berat_aktual = #{beratAktual}, " +
            "total_cost = #{totalCost}, harga_final = #{hargaFinal}, " +
            "notes = #{notes} WHERE id = #{id}")
    void updateAssignment(PickupAssignment assignment);

    @Delete("DELETE FROM pickup_assignments WHERE id = #{id}")
    void deleteAssignment(@Param("id") int id);
}