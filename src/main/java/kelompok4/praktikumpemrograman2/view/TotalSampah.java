package kelompok4.praktikumpemrograman2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TotalSampah {

    public JPanel getPanel() {
        // Panel utama dengan BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 240)); // Warna krem muda

        // Panel untuk judul di atas
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("Total Sampah", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0)); // Warna merah gelap
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Menambahkan jarak atas dan bawah
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabel data di tengah
        String[] columnNames = {"Tanggal", "Jenis Sampah", "Berat (kg)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setBackground(new Color(255, 239, 213)); // Warna latar tabel oranye muda
        table.setForeground(Color.BLACK); // Warna teks hitam
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Header tabel dengan warna khusus
        table.getTableHeader().setBackground(new Color(255, 160, 122)); // Warna salmon
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Pewarnaan baris ganjil-genap dan penyelarasan tengah
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(255, 250, 240) : new Color(255, 239, 213)); // Pewarnaan alternatif
                }
                setHorizontalAlignment(SwingConstants.CENTER); // Menyelaraskan teks ke tengah
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel bawah untuk filter dan total berat
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(255, 250, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin antar komponen

        // Label filter
        JLabel filterLabel = new JLabel("Filter By:");
        filterLabel.setForeground(new Color(139, 0, 0)); // Warna merah gelap
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        bottomPanel.add(filterLabel, gbc);

        // ComboBox untuk filter
        String[] filterOptions = {"Semua", "Harian", "Mingguan", "Bulanan"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setBackground(new Color(255, 250, 240)); // Warna krem muda
        filterComboBox.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(filterComboBox, gbc);

        // Label total berat
        JLabel totalLabel = new JLabel("Total Berat: 0.0 kg");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(139, 0, 0)); // Warna merah gelap
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(totalLabel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Listener untuk filter
        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            tableModel.setRowCount(0); // Reset data tabel

            switch (selectedFilter) {
                case "Harian" -> {
                    tableModel.addRow(new Object[]{"2024-12-22", "Komputer", 3.0});
                    totalLabel.setText("Total Berat: 3.0 kg");
                }
                case "Mingguan" -> {
                    tableModel.addRow(new Object[]{"2024-12-20", "Elektronik", 1.2});
                    tableModel.addRow(new Object[]{"2024-12-21", "Baterai", 0.8});
                    tableModel.addRow(new Object[]{"2024-12-22", "Komputer", 3.0});
                    totalLabel.setText("Total Berat: 5.0 kg");
                }
                case "Bulanan" -> {
                    tableModel.addRow(new Object[]{"2024-12-01", "Plastik", 2.0});
                    tableModel.addRow(new Object[]{"2024-12-15", "Kertas", 4.5});
                    tableModel.addRow(new Object[]{"2024-12-22", "Komputer", 3.0});
                    totalLabel.setText("Total Berat: 9.5 kg");
                }
                default -> {
                    tableModel.addRow(new Object[]{"2024-12-20", "Elektronik", 1.2});
                    tableModel.addRow(new Object[]{"2024-12-21", "Baterai", 0.8});
                    tableModel.addRow(new Object[]{"2024-12-22", "Komputer", 3.0});
                    totalLabel.setText("Total Berat: 5.0 kg");
                }
            }
        });

        return mainPanel;
    }
}
