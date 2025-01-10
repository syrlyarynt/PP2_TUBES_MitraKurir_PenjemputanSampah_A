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
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class TotalSampahView {
    private TotalSampahController controller;
    private final String[] MONTHS = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> filterComboBox;
    private JLabel totalLabel;

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
        filterComboBox = new JComboBox<>(filterOptions);
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
        totalLabel = new JLabel("Total Berat: 0.0 kg");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(139, 0, 0));
        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(totalLabel, gbc);

        // Export PDF Button
        JButton exportPdfButton = new JButton("Export ke PDF");
        exportPdfButton.setBackground(new Color(70, 130, 180));
        exportPdfButton.setForeground(Color.WHITE);
        exportPdfButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 5;
        bottomPanel.add(exportPdfButton, gbc);

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

        // ActionListener for export PDF button
        exportPdfButton.addActionListener(e -> exportToPDF(tableModel));

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

    private void exportToPDF(DefaultTableModel tableModel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan PDF");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                // Membuat dokumen PDF
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument, com.itextpdf.kernel.geom.PageSize.A4);
                document.setMargins(36, 36, 36, 36);

                // Menambahkan font
                PdfFont font = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
                document.setFont(font);

                // Judul dokumen
                document.add(new Paragraph("Laporan Total Sampah")
                        .setFont(font)
                        .setFontSize(16)
                        .setBold()
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

                // Add filter information
                String filterInfo = "";
                String selectedFilter = filterComboBox.getSelectedItem().toString();
                switch (selectedFilter) {
                    case "Harian" -> filterInfo = "Filter: Harian - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                    case "Mingguan" -> {
                        LocalDate today = LocalDate.now();
                        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
                        LocalDate endOfWeek = startOfWeek.plusDays(6);
                        filterInfo = "Filter: Mingguan - " + startOfWeek.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) +
                                " sampai " + endOfWeek.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                    }
                    case "Bulanan" -> filterInfo = "Filter: Bulanan - " + MONTHS[monthComboBox.getSelectedIndex()] +
                            " " + yearComboBox.getSelectedItem();
                    default -> filterInfo = "Filter: Semua Data";
                }

                document.add(new Paragraph(filterInfo)
                        .setFont(font)
                        .setFontSize(12)
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.LEFT));

                // Menambahkan tabel ke PDF
                Table table = new Table(new float[]{2, 2, 1});  // Proportion for 3 columns
                table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

                // Header tabel
                String[] columnNames = {"Tanggal", "Jenis Sampah", "Berat (kg)"};
                for (String columnName : columnNames) {
                    table.addHeaderCell(new Cell().add(new Paragraph(columnName).setFont(font).setBold()));
                }

                // Data tabel
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        table.addCell(new Cell().add(new Paragraph(value != null ? value.toString() : "").setFont(font)));
                    }
                }

                document.add(table);

                // Add total at the bottom
                document.add(new Paragraph(totalLabel.getText())
                        .setFont(font)
                        .setFontSize(12)
                        .setBold()
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

                document.close();

                JOptionPane.showMessageDialog(null, "Data berhasil diekspor ke PDF!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal mengekspor data ke PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}