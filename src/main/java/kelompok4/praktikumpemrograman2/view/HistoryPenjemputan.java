package kelompok4.praktikumpemrograman2.view;

import kelompok4.praktikumpemrograman2.controller.HistoryController;
import kelompok4.praktikumpemrograman2.model.History;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryPenjemputan {
    private final HistoryController historyController;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton refreshButton;
    private JLabel totalLabel;

    public HistoryPenjemputan() {
        this.historyController = new HistoryController();
    }


    public JPanel getPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 240));

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
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        refreshButton.addActionListener(e -> handleRefresh());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 250, 240));
        buttonPanel.add(refreshButton);

        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Setup tabel
        String[] columnNames = {
                "ID Riwayat", "id, pickup assignment", "Waktu Selesai", "Lokasi",
                "Kategori Sampah", "Berat (Kg)", "Harga (Rp)",
                "Status"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        setupTableProperties();

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel bawah untuk total penjemputan
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(255, 250, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        totalLabel = new JLabel("Total Penjemputan: " + tableModel.getRowCount());
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(139, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(totalLabel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Perbarui data tabel dan label total
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

        // Custom Cell Renderer
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Format BigDecimal to String
                if (value instanceof BigDecimal) {
                    value = ((BigDecimal) value).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    setText((String) value);
                }

                // Alternate Row Color
                if (!isSelected) {
                    String status = column == 7 ? (String) table.getValueAt(row, column) : null; // Status column
                    if ("Belum Selesai".equals(status)) {
                        c.setBackground(new Color(255, 200, 200));
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(255, 250, 240) : new Color(255, 239, 213));
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }
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
        System.out.println("Refreshing history data...");
        SwingUtilities.invokeLater(() -> {
            refreshButton.setEnabled(false);
            try {
                updateTableData();
                JOptionPane.showMessageDialog(null,
                        "Data berhasil diperbarui",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                System.err.println("Error refreshing data: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Gagal memperbarui data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                refreshButton.setEnabled(true);
            }
        });
    }
}