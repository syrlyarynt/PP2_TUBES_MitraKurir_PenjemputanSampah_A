package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface KurirMapper {
    @Options(useCache = false)
    @Select("SELECT id, nama, nomor_telepon AS nomorTelepon, alamat_kurir AS alamatKurir, status FROM kurir")
    List<Kurir> getAllKurir();

    @Options(useCache = false)
    @Select("SELECT id, nama, nomor_telepon AS nomorTelepon, alamat_kurir AS alamatKurir, status FROM kurir WHERE id = #{id}")
    Kurir getKurirById(@Param("id") int id);

    @Insert("INSERT INTO kurir (nama, nomor_telepon, alamat_kurir, status) VALUES (#{nama}, #{nomorTelepon}, #{alamatKurir}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id", useCache = false)
    void insertKurir(Kurir kurir);

    @Options(useCache = false)
    @Update("UPDATE kurir SET nama = #{nama}, nomor_telepon = #{nomorTelepon}, alamat_kurir = #{alamatKurir}, status = #{status} WHERE id = #{id}")
    void updateKurir(Kurir kurir);

    @Options(useCache = false)
    @Delete("DELETE FROM kurir WHERE id = #{id}")
    void deleteKurir(@Param("id") int id);
}
