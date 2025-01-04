package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface DropboxRateMapper {
    @Select("SELECT * FROM dropbox_rates")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "dropboxId", column = "dropbox_id"),
            @Result(property = "minDistance", column = "min_distance"),
            @Result(property = "maxDistance", column = "max_distance"),
            @Result(property = "baseRate", column = "base_rate"),
            @Result(property = "distanceRate", column = "distance_rate"),
            @Result(property = "dropbox", column = "dropbox_id",
                    javaType = LokasiDropbox.class,
                    one = @One(select = "kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper.getDropboxById"))
    })
    List<DropboxRate> getAllRates();

    @Select("SELECT * FROM dropbox_rates WHERE id = #{id}")
    DropboxRate getRateById(@Param("id") int id);

    @Insert("INSERT INTO dropbox_rates (dropbox_id, min_distance, max_distance, base_rate, distance_rate) " +
            "VALUES (#{dropboxId}, #{minDistance}, #{maxDistance}, #{baseRate}, #{distanceRate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertRate(DropboxRate rate);

    @Update("UPDATE dropbox_rates SET dropbox_id = #{dropboxId}, " +
            "min_distance = #{minDistance}, max_distance = #{maxDistance}, " +
            "base_rate = #{baseRate}, distance_rate = #{distanceRate} " +
            "WHERE id = #{id}")
    void updateRate(DropboxRate rate);

    @Delete("DELETE FROM dropbox_rates WHERE id = #{id}")
    void deleteRate(@Param("id") int id);
}