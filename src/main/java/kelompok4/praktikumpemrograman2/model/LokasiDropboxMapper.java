package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface LokasiDropboxMapper {
    @Select("SELECT * FROM lokasi_dropbox")
    List<LokasiDropbox> getAllDropbox();

    @Select("SELECT * FROM lokasi_dropbox WHERE id = #{id}")
    LokasiDropbox getDropboxById(@Param("id") int id);

    @Insert("INSERT INTO lokasi_dropbox (nama_dropbox, alamat, jarak) " +
            "VALUES (#{namaDropbox}, #{alamat}, #{jarak})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertDropbox(LokasiDropbox dropbox);

    @Update("UPDATE lokasi_dropbox SET nama_dropbox = #{namaDropbox}, " +
            "alamat = #{alamat}, jarak = #{jarak} WHERE id = #{id}")
    void updateDropbox(LokasiDropbox dropbox);

    @Delete("DELETE FROM lokasi_dropbox WHERE id = #{id}")
    void deleteDropbox(@Param("id") int id);
}