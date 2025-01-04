package kelompok4.praktikumpemrograman2.view;

import kelompok4.praktikumpemrograman2.controller.PickupAssignmentController;
import kelompok4.praktikumpemrograman2.controller.LokasiDropboxController;
import kelompok4.praktikumpemrograman2.model.PickupAssignment;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class MenerimaPermintaanView extends JFrame {
    private JPanel Panel;
    private JTable pickupTable;
    private JTable dropboxTable;
    private DefaultTableModel pickupTableModel;
    private DefaultTableModel dropboxTableModel;
    private PickupAssignmentController pickupController;
    private LokasiDropboxController dropboxController;
    private PickupAssignment currentAssignment;
    private LokasiDropbox selectedDropbox;

    // Constructor untuk tab panel
    public MenerimaPermintaanView() {
        this.pickupController = new PickupAssignmentController();
        this.dropboxController = new LokasiDropboxController();
        initializeUI();
    }

    // Constructor untuk window popup dengan assignment
    public MenerimaPermintaanView(PickupAssignment assignment) {
        this(); // Panggil constructor default
        this.currentAssignment = assignment;
    }

    private void initializeUI() {
        setTitle("Permintaan Pickup");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Panel = new JPanel(new BorderLayout());
        Panel.setBackground(new Color(255, 250, 240));

        setupHeaderPanel();
        setupTablesPanel();

        // Hanya tampilkan button panel jika ada assignment
        if (currentAssignment != null) {
            setupButtonPanel();
        }

        setContentPane(Panel);
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
    }


    private void setupHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 250, 240));

        JPanel titleWrapperPanel = new JPanel();
        titleWrapperPanel.setBackground(new Color(255, 250, 240));
        titleWrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblTitle = new JLabel("Permintaan Pickup", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(new Color(139, 0, 0));
        titleWrapperPanel.add(lblTitle);

        headerPanel.add(titleWrapperPanel, BorderLayout.CENTER);
        Panel.add(headerPanel, BorderLayout.NORTH);
    }

    private void setupTablesPanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(new Color(255, 250, 240));

        // Pickup Table
        setupPickupTable(tablePanel);

        // Dropbox Table
        setupDropboxTable(tablePanel);

        Panel.add(tablePanel, BorderLayout.CENTER);
    }

    private void setupPickupTable(JPanel tablePanel) {
        String[] columnNames = {"Nama", "Alamat", "Berat(kg)", "Harga"};
        pickupTableModel = new DefaultTableModel(columnNames, 0);
        pickupTable = createCustomTable(pickupTableModel);

        JScrollPane tableScrollPane = new JScrollPane(pickupTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 150));
        tableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.add(Box.createVerticalStrut(10));
        tablePanel.add(tableScrollPane);
    }

    private void setupDropboxTable(JPanel tablePanel) {
        JLabel lblDropbox = new JLabel("Lokasi Dropbox", SwingConstants.LEFT);
        lblDropbox.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblDropbox.setForeground(new Color(139, 0, 0));
        lblDropbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.add(Box.createVerticalStrut(10));
        tablePanel.add(lblDropbox);

        String[] columnNames = {"Nama Dropbox", "Alamat", "Jarak", "Kapasitas Tersedia"};
        dropboxTableModel = new DefaultTableModel(columnNames, 0);
        dropboxTable = createCustomTable(dropboxTableModel);

        // Add selection listener untuk dropbox table
        dropboxTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = dropboxTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedDropbox = dropboxController.getDropboxById(
                            Integer.parseInt(dropboxTable.getValueAt(selectedRow, 0).toString())
                    );
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(dropboxTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 150));
        tableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.add(Box.createVerticalStrut(10));
        tablePanel.add(tableScrollPane);
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton terimaButton = new JButton("Terima");
        terimaButton.setBackground(new Color(34, 139, 34));
        terimaButton.setForeground(Color.WHITE);
        terimaButton.addActionListener(e -> handleTerima());

        JButton tolakButton = new JButton("Tolak");
        tolakButton.setBackground(new Color(255, 160, 122));
        tolakButton.setForeground(Color.WHITE);
        tolakButton.addActionListener(e -> handleTolak());

        // Hanya tampilkan tombol jika ada assignment
        if (currentAssignment != null) {
            buttonPanel.add(terimaButton);
            buttonPanel.add(tolakButton);
        }

        Panel.add(buttonPanel, BorderLayout.SOUTH);
    }
    public void setSecondTableData(String[][] data) {
        setDropboxData(data); // Alias untuk metode setDropboxData
    }


    private void handleTerima() {
        if (currentAssignment == null) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada permintaan yang dipilih.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (selectedDropbox != null) {
                currentAssignment.setDropboxId(selectedDropbox.getId());
            }
            currentAssignment.setStatus("In Progress");
            pickupController.updateAssignment(currentAssignment);

            JOptionPane.showMessageDialog(this,
                    "Permintaan pickup telah diterima dan akan segera diproses.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleTolak() {
        if (currentAssignment == null) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada permintaan yang dipilih.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String reason = JOptionPane.showInputDialog(this,
                "Masukkan alasan penolakan:",
                "Alasan Penolakan",
                JOptionPane.QUESTION_MESSAGE);

        if (reason != null && !reason.trim().isEmpty()) {
            try {
                currentAssignment.setStatus("Cancelled");
                currentAssignment.setNotes(reason);
                pickupController.updateAssignment(currentAssignment);

                JOptionPane.showMessageDialog(this,
                        "Permintaan pickup telah ditolak.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Terjadi kesalahan: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setPickupData(String[][] data) {
        pickupTableModel.setRowCount(0);
        for (String[] row : data) {
            pickupTableModel.addRow(row);
        }
    }

    public void setDropboxData(String[][] data) {
        dropboxTableModel.setRowCount(0);
        for (String[] row : data) {
            dropboxTableModel.addRow(row);
        }
    }

    private JTable createCustomTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(255, 250, 240) : new Color(255, 239, 213));
                } else {
                    c.setBackground(getSelectionBackground());
                }
                return c;
            }
        };

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setFont(new Font("SansSerif", Font.PLAIN, 14));
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        table.setBackground(new Color(255, 239, 213));
        table.getTableHeader().setBackground(new Color(255, 160, 122));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));
        table.getTableHeader().setForeground(Color.WHITE);
        return table;
    }

    public JPanel getPanel() {
        return Panel;
    }
}
