package kelompok4.praktikumpemrograman2.view;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import kelompok4.praktikumpemrograman2.controller.HistoryController;
import kelompok4.praktikumpemrograman2.model.History;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Cell;

public class HistoryPenjemputanView {
    private final HistoryController historyController;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton refreshButton;
    private JButton exportPdfButton;
    private JLabel totalLabel;

    public HistoryPenjemputanView() {
        this.historyController = new HistoryController();
    }

    public JPanel getPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 240));

        // Tambahkan JLabel "History Penjemputan"
        JLabel headerLabel = new JLabel("History Penjemputan", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 0, 0));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Panel untuk judul
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 250, 240));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("History Penjemputan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(255, 160, 122));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        refreshButton.addActionListener(e -> handleRefresh());

        exportPdfButton = new JButton("Export ke PDF");
        exportPdfButton.setBackground(new Color(70, 130, 180));
        exportPdfButton.setForeground(Color.WHITE);
        exportPdfButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exportPdfButton.addActionListener(e -> exportToPDF());
        

        // Setup tabel
        String[] columnNames = {
                "ID Riwayat", "ID Pickup Assignment", "Waktu Selesai", "Lokasi",
                "Kategori Sampah", "Berat (Kg)", "Harga (Rp)",
                "Status"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        setupTableProperties();

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel bawah untuk tombol dan total
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(255, 250, 240));

        // Pengaturan GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Memberikan sedikit jarak antara elemen

        // Tombol di tengah
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(255, 160, 122));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        refreshButton.addActionListener(e -> handleRefresh());

        exportPdfButton = new JButton("Export ke PDF");
        exportPdfButton.setBackground(new Color(70, 130, 180));
        exportPdfButton.setForeground(Color.WHITE);
        exportPdfButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exportPdfButton.addActionListener(e -> exportToPDF());

        // Panel untuk tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 250, 240));
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportPdfButton);

        // Atur tombol di tengah GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER; 
        bottomPanel.add(buttonPanel, gbc);

        // Label total penjemputan
        totalLabel = new JLabel("Total Penjemputan: " + tableModel.getRowCount());
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(139, 0, 0));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Atur label di sebelah kanan
        gbc.gridx = 3; 
        gbc.gridy = 0;
        gbc.gridwidth = 1; 
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(totalLabel, gbc);

        // Tambahkan bottomPanel ke mainPanel
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Perbarui data tabel
        updateTableData();

        return mainPanel;
    }


    private void setupTableProperties() {
        table.setBackground(new Color(255, 239, 213));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Header styling
        table.getTableHeader().setBackground(new Color(255, 160, 122));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Warna bergantian untuk baris tabel
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(new Color(255, 250, 240)); // Krem untuk baris genap
                    } else {
                        cell.setBackground(new Color(255, 239, 213)); // Oranye muda untuk baris ganjil
                    }
                }
                return cell;
            }
        });
    }
    private void updateTableData() {
        try {
            tableModel.setRowCount(0);

            List<History> histories = historyController.getAllHistory();

            for (History history : histories) {
                Object[] rowData = new Object[]{
                        history.getIdRiwayat(),
                        history.getPickupAssignmentId() != null ? history.getPickupAssignmentId() : "N/A",
                        history.getWaktuSelesai() != null ? history.getWaktuSelesai().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A",
                        history.getLokasi(),
                        history.getKategoriSampah(),
                        history.getBeratSampah(),
                        history.getHarga(),
                        history.getStatusPenyelesaian()
                };
                tableModel.addRow(rowData);
            }

            // Update label total
            totalLabel.setText("Total Penjemputan: " + tableModel.getRowCount());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRefresh() {
        SwingUtilities.invokeLater(() -> {
            refreshButton.setEnabled(false);
            try {
                updateTableData();
                JOptionPane.showMessageDialog(null,
                        "Data berhasil diperbarui",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Gagal memperbarui data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                refreshButton.setEnabled(true);
            }
        });
    }

    private void exportToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan PDF");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                // Lokasi file yang dipilih pengguna
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                // Membuat dokumen PDF
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument, com.itextpdf.kernel.geom.PageSize.A4);
                document.setMargins(36, 36, 36, 36); // Margin atas, kanan, bawah, kiri dalam satuan poin

                // Menambahkan font
                PdfFont font = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
                document.setFont(font);

                // Judul dokumen
                document.add(new Paragraph("History Penjemputan")
                        .setFont(font)
                        .setFontSize(16)
                        .setBold()
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

                // Menambahkan tabel ke PDF
                Table table = new Table(new float[]{1, 1, 1, 1, 1, 1, 1, 1});
                table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

                // Header tabel
                String[] columnNames = {
                        "ID Riwayat", "ID Pickup Assignment", "Waktu Selesai", "Lokasi",
                        "Kategori Sampah", "Berat (Kg)", "Harga (Rp)", "Status"
                };

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
                document.close();

                JOptionPane.showMessageDialog(null, "Data berhasil diekspor ke PDF!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal mengekspor data ke PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
