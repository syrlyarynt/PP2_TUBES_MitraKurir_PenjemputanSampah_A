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

    public HistoryPenjemputan() {
        this.historyController = new HistoryController();
    }

    public JPanel getPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 240));

        // Panel untuk judul
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("History Penjemputan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

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

        updateTableData();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel totalLabel = new JLabel("Total Penjemputan: " + tableModel.getRowCount());
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(139, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        bottomPanel.add(totalLabel, gbc);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("SansSerif",Font.BOLD,14));
        bottomPanel.add(refreshButton);
        refreshButton.addActionListener(e->actionRefresh());
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    private void actionRefresh(){
        updateTableData();
    }
//    private void setupTableProperties() {
//        table.setBackground(new Color(255, 239, 213));
//        table.setForeground(Color.BLACK);
//        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
//
//        // Header styling
//        table.getTableHeader().setBackground(new Color(255, 160, 122));
//        table.getTableHeader().setForeground(Color.WHITE);
//        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
//
//        // Cell renderer for alternating colors and center alignment
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value,
//                                                           boolean isSelected, boolean hasFocus, int row, int column) {
//                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                if (!isSelected) {
//                    String status = column == 6 ? (String) table.getValueAt(row, column) : null;
//                    if ("Belum Selesai".equals(status)) {
//                        c.setBackground(new Color(255, 200, 200));
//                    } else {
//                        c.setBackground(row % 2 == 0 ? new Color(255, 250, 240) : new Color(255, 239, 213));
//                    }
//                }
//                setHorizontalAlignment(SwingConstants.CENTER);
//                return c;
//            }
//        };
//
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//        }
//    }

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
        System.out.println("\n=== Starting Table Update ===");
        try {
            tableModel.setRowCount(0);
            System.out.println("Table cleared");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            List<History> histories = historyController.getAllHistory();

            System.out.println("Processing " + histories.size() + " records for display");

            for (History history : histories) {
                System.out.println("Processing record ID: " + history.getIdRiwayat());
                Object[] rowData = new Object[]{
                        history.getIdRiwayat(),
                        history.getPickupAssignmentId() != null ? history.getPickupAssignmentId() : "N/A",
                        history.getWaktuSelesai() != null ? history.getWaktuSelesai().format(formatter) : "N/A",
                        history.getLokasi(),
                        history.getKategoriSampah(),
                        history.getBeratSampah(),
                        history.getHarga(),
                        history.getStatusPenyelesaian()
                };
                tableModel.addRow(rowData);
                System.out.println("Added row to table: ID=" + history.getIdRiwayat() +
                        ", Status=" + history.getStatusPenyelesaian());
            }

            System.out.println("Final table row count: " + tableModel.getRowCount());

        } catch (Exception e) {
            System.out.println("ERROR updating table: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== Table Update Complete ===\n");
    }
}