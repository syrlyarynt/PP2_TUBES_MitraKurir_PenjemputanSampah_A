package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface JenisKategoriMapper {
    @Select("SELECT * FROM jenis_kategori")
    List<JenisKategori> getAllKategori();

    @Select("SELECT * FROM jenis_kategori WHERE id = #{id}")
    JenisKategori getKategoriById(@Param("id") int id);

    @Insert("INSERT INTO jenis_kategori (nama, icon) VALUES (#{nama}, #{icon})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertKategori(JenisKategori kategori);

    @Update("UPDATE jenis_kategori SET nama = #{nama}, icon = #{icon} WHERE id = #{id}")
    void updateKategori(JenisKategori kategori);

    @Delete("DELETE FROM jenis_kategori WHERE id = #{id}")
    void deleteKategori(@Param("id") int id);
}
