package kelompok4.praktikumpemrograman2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class HistoryPenjemputan {

    public JPanel getPanel() {
        // Panel utama menggunakan layout BorderLayout agar lebih fleksibel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 240)); // Warna krem muda

        // Panel untuk judul di atas
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("History Penjemputan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20)); // Font bold ukuran 20
        titleLabel.setForeground(new Color(139, 0, 0)); // Warna merah gelap
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Menambahkan jarak atas dan bawah
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabel data di tengah
        String[] columnNames = {"ID Riwayat", "Waktu Selesai", "Lokasi", "Kategori Sampah", "Status Penyelesaian"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        JTable table = new JTable(tableModel);
        table.setBackground(new Color(255, 239, 213)); // Warna latar tabel oranye muda
        table.setForeground(Color.BLACK); // Warna teks hitam
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Header tabel dengan warna khusus
        table.getTableHeader().setBackground(new Color(255, 160, 122)); // Warna salmon
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Renderer untuk pewarnaan baris dan pusatkan teks kolom ID Riwayat
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = column == 4 ? (String) table.getValueAt(row, column) : null;
                    if ("Belum Selesai".equals(status)) {
                        c.setBackground(new Color(255, 200, 200)); // Warna merah muda untuk "Belum Selesai"
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(255, 250, 240) : new Color(255, 239, 213)); // Alternating row colors
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER); // Memusatkan teks
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Menambahkan dummy data ke tabel
        Object[][] dummyData = {
            {"1", "2024-12-10 14:30", "Jakarta", "Plastik", "Selesai"},
            {"2", "2024-12-11 10:00", "Bandung", "Kertas", "Belum Selesai"},
            {"3", "2024-12-12 12:45", "Surabaya", "Elektronik", "Selesai"},
            {"4", "2024-12-13 08:30", "Yogyakarta", "Baterai", "Belum Selesai"},
            {"5", "2024-12-14 16:00", "Medan", "Organik", "Selesai"}
        };

        for (Object[] row : dummyData) {
            tableModel.addRow(row);
        }

        // Panel bawah untuk label total penjemputan
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(255, 250, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin antar komponen

        // Label total penjemputan
        JLabel totalLabel = new JLabel("Total Penjemputan: " + tableModel.getRowCount());
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Font bold ukuran 14
        totalLabel.setForeground(new Color(139, 0, 0)); // Warna merah gelap
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(totalLabel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }
}
