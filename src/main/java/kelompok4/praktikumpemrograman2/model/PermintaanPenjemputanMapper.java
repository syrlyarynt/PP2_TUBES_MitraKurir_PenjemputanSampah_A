package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface PermintaanPenjemputanMapper {

    // Fetch all permintaan with joined data
    @Select("""
    SELECT 
        p.IDpermintaan, 
        p.namaPelanggan, 
        p.alamat, 
        p.kategoriSampah AS kategoriSampahId,
        k.nama AS kategoriSampah,
        p.berat, 
        p.harga, 
        p.waktuPermintaan, 
        p.status, 
        p.dropbox_id AS dropboxId, 
        d.nama_dropbox, 
        d.kapasitas_max, 
        d.kapasitas_terisi,
        p.total_biaya
    FROM permintaanpenjemputan p
    LEFT JOIN lokasi_dropbox d ON p.dropbox_id = d.id
    LEFT JOIN jenis_kategori k ON p.kategoriSampah = k.id
""")
    @Results(id = "permintaanResultMap", value = {
            @Result(property = "idPermintaan", column = "IDpermintaan"),
            @Result(property = "namaPelanggan", column = "namaPelanggan"),
            @Result(property = "alamat", column = "alamat"),
            @Result(property = "kategoriSampahId", column = "kategoriSampahId"),
            @Result(property = "kategoriSampah", column = "kategoriSampah"),
            @Result(property = "berat", column = "berat"),
            @Result(property = "harga", column = "harga"),
            @Result(property = "waktuPermintaan", column = "waktuPermintaan"),
            @Result(property = "status", column = "status"),
            @Result(property = "dropboxId", column = "dropboxId"),
            @Result(property = "lokasiDropbox", column = "dropboxId",
                    javaType = LokasiDropbox.class,
                    one = @One(select = "kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper.getDropboxById"))
    })
    List<PermintaanPenjemputan> getAllPermintaan();


    // Fetch permintaan by ID
    @Select("""
    SELECT 
        p.IDpermintaan, 
        p.namaPelanggan, 
        p.alamat, 
        p.kategoriSampah AS kategoriSampahId,
        k.nama AS kategoriSampah,
        p.berat, 
        p.harga, 
        p.waktuPermintaan, 
        p.status, 
        p.dropbox_id AS dropboxId, 
        d.nama_dropbox, 
        d.kapasitas_max, 
        d.kapasitas_terisi,
        p.total_biaya
    FROM permintaanpenjemputan p
    LEFT JOIN lokasi_dropbox d ON p.dropbox_id = d.id
    LEFT JOIN jenis_kategori k ON p.kategoriSampah = k.id
    WHERE p.IDpermintaan = #{id}
""")
    @ResultMap("permintaanResultMap")
    PermintaanPenjemputan getPermintaanById(@Param("id") int id);

    // Insert new permintaan
    @Insert("INSERT INTO permintaanpenjemputan " +
            "(namaPelanggan, alamat, kategoriSampah, berat, status, dropbox_id) " +
            "VALUES " +
            "(#{namaPelanggan}, #{alamat}, #{kategoriSampahId}, #{berat}, 'Menunggu', #{dropboxId})")
    @Options(useGeneratedKeys = true, keyProperty = "idPermintaan", keyColumn = "IDpermintaan")
    void insertPermintaan(PermintaanPenjemputan permintaan);


    @Select("SELECT LAST_INSERT_ID()")
    Long getLastInsertId();

    // Update existing permintaan - Hanya update status
    @Update("UPDATE permintaanpenjemputan SET status = #{status} WHERE IDpermintaan = #{idPermintaan}")
    void updatePermintaan(PermintaanPenjemputan permintaan);

    // Delete permintaan by ID
    @Delete("DELETE FROM permintaanpenjemputan WHERE IDpermintaan = #{id}")
    void deletePermintaan(@Param("id") int id);

    // Fetch all kategori sampah
    @Select("SELECT id, nama FROM jenis_kategori")
    List<JenisKategori> getAllKategoriSampah();

    // Check if kategori exists
    @Select("SELECT COUNT(*) FROM jenis_kategori WHERE id = #{id}")
    Integer isKategoriExists(@Param("id") int id);

    // Check if dropbox exists
    @Select("SELECT COUNT(*) FROM lokasi_dropbox WHERE id = #{id}")
    Integer isDropboxExists(@Param("id") int id);
}