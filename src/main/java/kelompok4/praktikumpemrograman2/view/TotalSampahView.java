package kelompok4.praktikumpemrograman2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import kelompok4.praktikumpemrograman2.controller.TotalSampahController;
import kelompok4.praktikumpemrograman2.model.TotalSampah;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.math.BigDecimal;

public class TotalSampahView {
    private TotalSampahController controller;
    private final String[] MONTHS = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;

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
                return false;
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

        // Filter label
        JLabel filterLabel = new JLabel("Filter By:");
        filterLabel.setForeground(new Color(139, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        bottomPanel.add(filterLabel, gbc);

        // Filter combo box
        String[] filterOptions = {"Semua", "Harian", "Mingguan", "Bulanan"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setBackground(new Color(255, 250, 240));
        filterComboBox.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(filterComboBox, gbc);

        // Month selection
        monthComboBox = new JComboBox<>(MONTHS);
        monthComboBox.setBackground(new Color(255, 250, 240));
        monthComboBox.setForeground(Color.BLACK);
        monthComboBox.setEnabled(false);
        gbc.gridx = 2;
        bottomPanel.add(monthComboBox, gbc);

        // Year selection
        Integer[] years = generateYearArray();
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setBackground(new Color(255, 250, 240));
        yearComboBox.setForeground(Color.BLACK);
        yearComboBox.setEnabled(false);
        gbc.gridx = 3;
        bottomPanel.add(yearComboBox, gbc);

        // Total label
        JLabel totalLabel = new JLabel("Total Berat: 0.0 kg");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(139, 0, 0));
        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(totalLabel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ActionListener for the filter combo box
        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            boolean isMonthly = "Bulanan".equals(selectedFilter);
            monthComboBox.setEnabled(isMonthly);
            yearComboBox.setEnabled(isMonthly);
            updateTableData(tableModel, totalLabel, selectedFilter);
        });

        // ActionListeners for month and year selection
        monthComboBox.addActionListener(e -> {
            if ("Bulanan".equals(filterComboBox.getSelectedItem())) {
                updateTableData(tableModel, totalLabel, "Bulanan");
            }
        });

        yearComboBox.addActionListener(e -> {
            if ("Bulanan".equals(filterComboBox.getSelectedItem())) {
                updateTableData(tableModel, totalLabel, "Bulanan");
            }
        });

        // Trigger initial load
        filterComboBox.setSelectedItem("Semua");

        return mainPanel;
    }

    private Integer[] generateYearArray() {
        int currentYear = LocalDate.now().getYear();
        Integer[] years = new Integer[5];
        for (int i = 0; i < 5; i++) {
            years[i] = currentYear - i;
        }
        return years;
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
                int selectedMonth = monthComboBox.getSelectedIndex() + 1;
                int selectedYear = (Integer) yearComboBox.getSelectedItem();
                YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
                LocalDate startOfMonth = yearMonth.atDay(1);
                LocalDate endOfMonth = yearMonth.atEndOfMonth();
                filteredData = controller.getTotalSampahByMonth(startOfMonth.toString(), endOfMonth.toString());
            }
            default -> filteredData = controller.getAll();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

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
                    sampah.getTanggal().format(formatter),
                    jenisSampah,
                    beratKg
            });
        }

        totalLabel.setText("Total Berat: " + totalWeight.setScale(2, RoundingMode.HALF_UP)
                + " kg");
    }
}