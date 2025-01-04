package kelompok4.praktikumpemrograman2.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.apache.ibatis.session.SqlSession;
import java.util.List;
import kelompok4.praktikumpemrograman2.model.LokasiDropboxMapper;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import net.miginfocom.swing.MigLayout;

public class LokasiDropboxView {
    private JPanel panel;

    public LokasiDropboxView() {
        
        // Panel utama menggunakan MigLayout
        panel = new JPanel(new MigLayout("wrap 2", "[grow, fill][grow, fill]", "[]10[]10[]10[]10[]10[]10[grow]"));

        // Menambahkan title di bagian atas panel
        JLabel titleLabel = new JLabel("Lokasi Dropbox", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0)); // Menentukan warna judul
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 250, 240)); 
        panel.add(titleLabel, "span, align center");  // Merentangkan ke dua kolom dan menambahkan penataan tengah

        // Menambahkan elemen informasi penjemputan sampah
        panel.add(new JLabel("Lokasi Penjemputan:"), "cell 0 1");
        JTextField lokasiField = new JTextField("Jl. Contoh No. 123");
        lokasiField.setEditable(false); 
        lokasiField.setBorder(BorderFactory.createLineBorder(new Color(255, 160, 122), 1));
        lokasiField.setMargin(new Insets(10, 15, 10, 15));
        lokasiField.setFont(new Font("SansSerif", Font.PLAIN,14));
        panel.add(lokasiField, "cell 1 1");

        panel.add(new JLabel("Berat Sampah (kg):"), "cell 0 2");
        JTextField beratField = new JTextField("15");
        beratField.setEditable(false); 
        beratField.setBorder(BorderFactory.createLineBorder(new Color(255, 160, 122), 1));
        beratField.setFont(new Font("SansSerif", Font.PLAIN,14));
        panel.add(beratField, "cell 1 2");

        panel.add(new JLabel("Jumlah Sampah:"), "cell 0 3");
        JTextField jumlahField = new JTextField("3");
        jumlahField.setEditable(false); 
        jumlahField.setBorder(BorderFactory.createLineBorder(new Color(255, 160, 122), 1));
        jumlahField.setFont(new Font("SansSerif", Font.PLAIN,14));
        panel.add(jumlahField, "cell 1 3");

        panel.add(new JLabel("Harga Penjemputan:"), "cell 0 4");
        JTextField hargaField = new JTextField("Rp 100,000");
        hargaField.setEditable(false); 
        hargaField.setBorder(BorderFactory.createLineBorder(new Color(255, 160, 122), 1));
        hargaField.setFont(new Font("SansSerif", Font.PLAIN,14));
        panel.add(hargaField, "cell 1 4");
 
        panel.setBackground(new Color(255, 250, 240)); 
        // Panel untuk list dropbox terdekat
        
        // Mengambil data dari database menggunakan MyBatis
        String[][] secondData = getLokasiDropboxData();
        setSecondTableData(secondData);
    }

    private String[][] getLokasiDropboxData() {
        SqlSession session = MyBatisUtil.getSqlSession();
        try {
            LokasiDropboxMapper mapper = session.getMapper(LokasiDropboxMapper.class);
            // Correct List from java.util
            List<kelompok4.praktikumpemrograman2.model.LokasiDropbox> records = mapper.getAllDropbox();
            String[][] data = new String[records.size()][3];

            int i = 0;
            for (kelompok4.praktikumpemrograman2.model.LokasiDropbox entity : records) {
                data[i][0] = entity.getNamaDropbox();
                data[i][1] = entity.getAlamat();
                data[i][2] = String.format("%.1f KM", entity.getJarak());
                i++;
            }

            return data;
        } finally {
            session.close();
        }
    }

    private void setSecondTableData(String[][] data) {
        // Kolom untuk tabel
        String[] columnNames = {"Nama Dropbox", "Alamat", "Jarak (km)"};
        
        JTable tableDropbox = new JTable(data, columnNames);
        
        // Mengatur warna latar belakang tabel (list dropbox) ke warna yang diinginkan
        tableDropbox.setBackground(new Color(255, 239, 213));  // Mengatur warna latar belakang tabel
        tableDropbox.getTableHeader().setBackground(new Color(255, 160, 122));
        tableDropbox.setFont(new Font("SansSerif", Font.PLAIN,14));
        
        JTableHeader header = tableDropbox.getTableHeader();
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        header.setFont(new Font("SansSerif", Font.BOLD,14));
        header.setForeground(new Color(255,255,255));

        // Menyesuaikan perataan teks di setiap kolom ke tengah
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);  // Mengatur agar teks berada di tengah

        // Mengatur renderer untuk setiap kolom
        for (int i = 0; i < tableDropbox.getColumnCount(); i++) {
            tableDropbox.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Mengatur ukuran JScrollPane agar memenuhi ruang yang tersisa
        JScrollPane scrollPane = new JScrollPane(tableDropbox);
        scrollPane.setPreferredSize(new Dimension(1000, 800));  // Atur tinggi scrollpane agar sesuai

        // Mengatur agar tabel tumbuh dengan ukuran
        tableDropbox.setFillsViewportHeight(true);  // Mengisi seluruh tinggi area scroll

        // Menambahkan scroll pane ke dalam panel
        panel.add(scrollPane, "cell 0 6 2 1");  // Merentang dua kolom untuk tabel

        // Panel untuk tombol Terima dan Tolak
        JPanel panelButton = new JPanel(new FlowLayout());
        JButton terimaButton = new JButton("Terima");
        JButton tolakButton = new JButton("Tolak");

        panelButton.setBackground(new Color(255, 250, 240));

        // Menambahkan aksi pada tombol Terima
        terimaButton.addActionListener(e -> {
            // Logika aksi untuk tombol "Terima" (kosongkan atau tambahkan aksi)
            // Misalnya: statusField.setText("Sampah Diterima");
            // untuk sementara dikosongkan
            ////
        });

        // Menambahkan aksi pada tombol Tolak
        tolakButton.addActionListener(e -> {
            // Logika aksi untuk tombol "Tolak" (kosongkan atau tambahkan aksi)
            // Misalnya: statusField.setText("Penjemputan Sampah Ditolak");
            // untuk sementara dikosongkan
            ////
        });

        panelButton.add(terimaButton);
        panelButton.add(tolakButton);

        // Tombol terima/tolak ditambahkan setelah tabel dan mengisi ruang di bawah tabel
        panel.add(panelButton, "cell 0 7 2 1");  // Merentang dua kolom untuk tombol
    }

    public JPanel getPanel() {
        return panel;
    }
}
