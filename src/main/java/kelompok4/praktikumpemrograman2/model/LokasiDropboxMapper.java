package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface LokasiDropboxMapper {
    @Select("SELECT * FROM lokasi_dropbox")
    @Results(id = "dropboxResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "namaDropbox", column = "nama_dropbox"),
            @Result(property = "alamat", column = "alamat"),
            @Result(property = "jarak", column = "jarak"),
            @Result(property = "kapasitasMax", column = "kapasitas_max"),
            @Result(property = "kapasitasTerisi", column = "kapasitas_terisi")
    })
    List<LokasiDropbox> getAllDropbox();

    @Select("SELECT * FROM lokasi_dropbox WHERE id = #{id}")
    @ResultMap("dropboxResultMap")
    LokasiDropbox getDropboxById(@Param("id") int id);

    @Insert("INSERT INTO lokasi_dropbox (nama_dropbox, alamat, jarak, kapasitas_max, kapasitas_terisi) " +
            "VALUES (#{namaDropbox}, #{alamat}, #{jarak}, #{kapasitasMax}, #{kapasitasTerisi})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertDropbox(LokasiDropbox dropbox);

    @Update("UPDATE lokasi_dropbox SET nama_dropbox = #{namaDropbox}, " +
            "alamat = #{alamat}, jarak = #{jarak}, " +
            "kapasitas_max = #{kapasitasMax}, kapasitas_terisi = #{kapasitasTerisi} " +
            "WHERE id = #{id}")
    void updateDropbox(LokasiDropbox dropbox);

    @Delete("DELETE FROM lokasi_dropbox WHERE id = #{id}")
    void deleteDropbox(@Param("id") int id);
}