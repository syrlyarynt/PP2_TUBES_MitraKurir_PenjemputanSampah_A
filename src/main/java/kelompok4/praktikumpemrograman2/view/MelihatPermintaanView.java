package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MelihatPermintaanView extends JFrame {
    private JPanel Panel;
    private JTable pickupTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterComboBox;

    public MelihatPermintaanView() {
        setTitle("Daftar Pickup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel utama dengan BorderLayout
        Panel = new JPanel(new BorderLayout());
        Panel.setBackground(new Color(255, 250, 240)); // Background warna cream muda

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 250, 240)); // Background cream muda

        // Membuat panel pembungkus untuk memberi jarak pada label
        JPanel titleWrapperPanel = new JPanel();
        titleWrapperPanel.setBackground(new Color(255, 250, 240)); // Background sama dengan header
        titleWrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Memberikan margin atas 10px

        // Label judul
        JLabel lblTitle = new JLabel("Daftar Pickup", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(new Color(139, 0, 0));

        // Menambahkan label ke dalam wrapper
        titleWrapperPanel.add(lblTitle);

        // Menambahkan titleWrapperPanel ke headerPanel
        headerPanel.add(titleWrapperPanel, BorderLayout.CENTER);

        // Panel Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFilter = new JLabel("Filter by:");
        lblFilter.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblFilter.setForeground(new Color(139, 0, 0));
        filterComboBox = new JComboBox<>(new String[]{"Terdekat", "Jenis Sampah"});
        filterComboBox.setBackground(new Color(255, 160, 122));
        filterPanel.add(lblFilter);
        filterPanel.add(filterComboBox);
        filterPanel.setBackground(new Color(255, 250, 240)); // Background cream muda

        // Panel Kontainer untuk Filter dan Tabel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(255, 250, 240));

        // Tabel Pickup
        String[] columnNames = {"Nama", "Alamat", "Jenis Sampah", "Harga"};
        tableModel = new DefaultTableModel(columnNames, 0);
        pickupTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(255, 250, 240)); // Warna krem untuk baris genap
                    } else {
                        c.setBackground(new Color(255, 239, 213)); // Warna oranye muda untuk baris ganjil
                    }
                } else {
                    c.setBackground(getSelectionBackground());
                }
                return c;
            }
        };

        // Mengatur renderer untuk setiap kolom dengan alignment dan font
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER); // Align ke tengah
        renderer.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Set font

        // Mengatur renderer untuk setiap kolom
        pickupTable.getColumnModel().getColumn(0).setCellRenderer(renderer); // Kolom "Nama"
        pickupTable.getColumnModel().getColumn(1).setCellRenderer(renderer); // Kolom "Alamat"
        pickupTable.getColumnModel().getColumn(2).setCellRenderer(renderer); // Kolom "Jenis Sampah"
        pickupTable.getColumnModel().getColumn(3).setCellRenderer(renderer); // Kolom "Harga"

        // Mengubah latar belakang tabel menjadi oranye muda
        pickupTable.setBackground(new Color(255, 239, 213)); // Latar belakang tabel oranye muda

        pickupTable.getTableHeader().setBackground(new Color(255, 160, 122)); // Background header tabel oranye muda
        pickupTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));
        pickupTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane tableScrollPane = new JScrollPane(pickupTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 150)); // Ukuran tabel lebih kecil

        // Panel untuk tombol Pickup
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Posisikan tombol di tengah
        JButton pickupButton = new JButton("Pickup");
        pickupButton.setBackground(new Color(255, 160, 122));
        pickupButton.setForeground(Color.WHITE);
        buttonPanel.add(pickupButton); // Tambahkan tombol ke panel

        // Tambahkan filterPanel ke atas centerPanel
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH); // Menambahkan panel tombol ke bagian bawah tabel

        // Tambahkan komponen ke panel utama
        Panel.add(headerPanel, BorderLayout.NORTH);
        Panel.add(centerPanel, BorderLayout.CENTER);

        // Tambahkan panel utama ke frame
        setContentPane(Panel);

        // Sesuaikan ukuran frame
        pack();
        setLocationRelativeTo(null); // Posisikan frame di tengah layar

        // Data dummy untuk ditampilkan di tabel
        String[][] dataDummy = {
            {"John Doe", "Jl. Merdeka No. 1", "Organik", "Rp 50.000"},
            {"Jane Smith", "Jl. Pahlawan No. 2", "Anorganik", "Rp 30.000"},
            {"Andi Wijaya", "Jl. Raya No. 3", "Bahan Keras", "Rp 75.000"},
            {"Dewi Lestari", "Jl. Kebangsaan No. 4", "Organik", "Rp 45.000"},
            {"Budi Santoso", "Jl. Taman No. 5", "Elektronik", "Rp 100.000"}
        };
        setPickupData(dataDummy); // Menampilkan data dummy di tabel
    }

    // Method untuk mengatur data tabel
    public void setPickupData(String[][] data) {
        tableModel.setRowCount(0); // Hapus data lama
        for (String[] row : data) {
            tableModel.addRow(row);
        }
    }

    // Method untuk mendapatkan pilihan filter
    public String getSelectedFilter() {
        return filterComboBox.getSelectedItem().toString();
    }

    public JPanel getPanel() {
        return Panel;
    }
}
