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

import kelompok4.praktikumpemrograman2.controller.TotalSampahController;
import kelompok4.praktikumpemrograman2.model.TotalSampah;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

public class TotalSampahView {
    private TotalSampahController controller;

    public TotalSampahView() {
        controller = new TotalSampahController();
    }

    public JPanel getPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 240));

        // Panel for title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("Total Sampah", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Tanggal", "Jenis Sampah", "Berat (kg)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak bisa diedit
            }
        };

        JTable table = new JTable(tableModel);
        table.setBackground(new Color(255, 239, 213));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Table header customization
        table.getTableHeader().setBackground(new Color(255, 160, 122));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Row alternating colors and center alignment
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(255, 250, 240) : new Color(255, 239, 213));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(255, 250, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel filterLabel = new JLabel("Filter By:");
        filterLabel.setForeground(new Color(139, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        bottomPanel.add(filterLabel, gbc);

        String[] filterOptions = {"Semua", "Harian", "Mingguan", "Bulanan"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setBackground(new Color(255, 250, 240));
        filterComboBox.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(filterComboBox, gbc);

        JLabel totalLabel = new JLabel("Total Berat: 0.0 kg");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(139, 0, 0));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(totalLabel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ActionListener for the filter combo box
        filterComboBox.addActionListener(e -> {
            updateTableData(tableModel, totalLabel, (String) filterComboBox.getSelectedItem());
        });

        // Trigger initial load
        filterComboBox.setSelectedItem("Semua");

        return mainPanel;
    }

    private void updateTableData(DefaultTableModel tableModel, JLabel totalLabel, String selectedFilter) {
        tableModel.setRowCount(0);
        List<TotalSampah> filteredData;
        LocalDate today = LocalDate.now();
        BigDecimal totalWeight = BigDecimal.ZERO;

        switch (selectedFilter) {
            case "Harian" -> {
                String todayString = today.toString();
                filteredData = controller.getTotalSampahByTanggal(todayString);
            }
            case "Mingguan" -> {
                LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                filteredData = controller.getTotalSampahByWeek(startOfWeek.toString(), endOfWeek.toString());
            }
            case "Bulanan" -> {
                LocalDate startOfMonth = today.withDayOfMonth(1);
                LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
                filteredData = controller.getTotalSampahByMonth(startOfMonth.toString(), endOfMonth.toString());
            }
            default -> filteredData = controller.getAll();
        }

        for (TotalSampah sampah : filteredData) {
            BigDecimal beratKg = sampah.getBeratKg();
            if (beratKg == null) {
                beratKg = BigDecimal.ZERO;
                System.out.println("Warning: beratKg is null for sampah id: " + sampah.getId());
            }
            totalWeight = totalWeight.add(beratKg);

            String jenisSampah = sampah.getNamaJenis();
            if (jenisSampah == null || jenisSampah.trim().isEmpty()) {
                jenisSampah = "Kategori " + sampah.getJenisSampah();
                System.out.println("Warning: namaJenis is null for sampah id: " + sampah.getId() +
                        ", jenis_sampah: " + sampah.getJenisSampah());
            }

            tableModel.addRow(new Object[]{
                    sampah.getTanggal(),
                    jenisSampah,
                    beratKg
            });
        }

        totalLabel.setText("Total Berat: " + totalWeight.setScale(2, BigDecimal.ROUND_HALF_UP) + " kg");
    }
}