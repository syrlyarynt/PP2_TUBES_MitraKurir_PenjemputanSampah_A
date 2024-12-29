package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface PermintaanPenjemputanMapper {
    @Select("SELECT p.*, d.* FROM permintaanpenjemputan p " +
            "LEFT JOIN lokasi_dropbox d ON p.dropbox_id = d.id")
    @Results({
            @Result(property = "idPermintaan", column = "IDpermintaan"),
            @Result(property = "dropboxId", column = "dropbox_id"),
            @Result(property = "lokasiDropbox", column = "dropbox_id", javaType = LokasiDropbox.class,
                    one = @One(select = "kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper.getDropboxById"))
    })
    List<PermintaanPenjemputan> getAllPermintaan();

    @Select("SELECT * FROM permintaanpenjemputan WHERE IDpermintaan = #{id}")
    @Results({
            @Result(property = "idPermintaan", column = "IDpermintaan"),
            @Result(property = "dropboxId", column = "dropbox_id"),
            @Result(property = "lokasiDropbox", column = "dropbox_id", javaType = LokasiDropbox.class,
                    one = @One(select = "kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper.getDropboxById"))
    })
    PermintaanPenjemputan getPermintaanById(@Param("id") int id);

    @Insert("INSERT INTO permintaanpenjemputan (namaPelanggan, alamat, kategoriSampah, " +
            "berat, harga, waktuPermintaan, status, dropbox_id) " +
            "VALUES (#{namaPelanggan}, #{alamat}, #{kategoriSampah}, #{berat}, " +
            "#{harga}, #{waktuPermintaan}, #{status}, #{dropboxId})")
    @Options(useGeneratedKeys = true, keyProperty = "idPermintaan")
    void insertPermintaan(PermintaanPenjemputan permintaan);

    @Update("UPDATE permintaanpenjemputan SET namaPelanggan = #{namaPelanggan}, " +
            "alamat = #{alamat}, kategoriSampah = #{kategoriSampah}, " +
            "berat = #{berat}, harga = #{harga}, waktuPermintaan = #{waktuPermintaan}, " +
            "status = #{status}, dropbox_id = #{dropboxId} " +
            "WHERE IDpermintaan = #{idPermintaan}")
    void updatePermintaan(PermintaanPenjemputan permintaan);

    @Delete("DELETE FROM permintaanpenjemputan WHERE IDpermintaan = #{id}")
    void deletePermintaan(@Param("id") int id);
}