package kelompok4.praktikumpemrograman2.view;

import kelompok4.praktikumpemrograman2.controller.HistoryController;
import kelompok4.praktikumpemrograman2.controller.MenerimaPermintaanController;
import kelompok4.praktikumpemrograman2.controller.PermintaanPenjemputanController;
import kelompok4.praktikumpemrograman2.controller.PickupAssignmentController;
import kelompok4.praktikumpemrograman2.model.History;
import kelompok4.praktikumpemrograman2.model.PickupAssignment;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class MenerimaPermintaanView extends JFrame {
    private final JPanel Panel;
    private JTable pickupTable;
    private JTable dropboxTable;
    private DefaultTableModel pickupTableModel;
    private DefaultTableModel dropboxTableModel;
    private final MenerimaPermintaanController controller;
    private LokasiDropbox selectedDropbox;
    private JButton terimaButton;
    private JButton tolakButton;
    private JButton refreshButton;

    // Constructor untuk tab panel
    public MenerimaPermintaanView() {
        this.controller = new MenerimaPermintaanController();
        Panel = new JPanel(new BorderLayout());

        // Get the first pending assignment and set it as current
        List<PickupAssignment> pendingAssignments = controller.getPendingPickups();
        if (!pendingAssignments.isEmpty()) {
            PickupAssignment firstPending = pendingAssignments.get(0);
            controller.setCurrentAssignment(firstPending);
        }

        initializeUI();
        loadAllPendingPickups();
    }


    // Constructor untuk window popup dengan assignment
    public MenerimaPermintaanView(PickupAssignment assignment) {
        this.controller = new MenerimaPermintaanController();
        this.controller.setCurrentAssignment(assignment);
        Panel = new JPanel(new BorderLayout());
        initializeUI();
        loadSpecificPickup();
    }


    private void initializeUI() {
        setTitle("Permintaan Pickup");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Panel.setBackground(new Color(255, 250, 240));

        setupHeaderPanel();
        setupTablesPanel();
        setupButtonPanel();

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

        setupPickupTable(tablePanel);
        setupDropboxTable(tablePanel);

        Panel.add(tablePanel, BorderLayout.CENTER);
    }

    private void setupPickupTable(JPanel tablePanel) {
        String[] columnNames = {"Nama", "Alamat", "Berat(kg)", "Harga", "Status"};  // Tambah kolom Status
        pickupTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pickupTable = createCustomTable(pickupTableModel);

        // Tambah selection listener
        pickupTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = pickupTable.getSelectedRow();
                if (selectedRow >= 0) {
                    List<PickupAssignment> assignments = controller.getAllAssignments();
                    if (selectedRow < assignments.size()) {
                        PickupAssignment selectedAssignment = assignments.get(selectedRow);
                        controller.setCurrentAssignment(selectedAssignment);

                        // Enable button hanya jika status Assigned
                        String status = (String) pickupTableModel.getValueAt(selectedRow, 4);
                        boolean canAct = status.equals("Assigned");
                        enableButtons(canAct);
                    }
                }
            }
        });

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

        dropboxTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = dropboxTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Object namaDropbox = dropboxTable.getValueAt(selectedRow, 0);
                    List<LokasiDropbox> dropboxList = controller.getDropboxController().getAllDropbox();
                    selectedDropbox = dropboxList.stream()
                            .filter(d -> d.getNamaDropbox().equals(namaDropbox))
                            .findFirst()
                            .orElse(null);
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

        terimaButton = new JButton("Terima");
        terimaButton.setBackground(new Color(34, 139, 34));
        terimaButton.setForeground(Color.WHITE);
        terimaButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        terimaButton.addActionListener(e -> handleTerima());

        tolakButton = new JButton("Tolak");
        tolakButton.setBackground(new Color(255, 160, 122));
        tolakButton.setForeground(Color.WHITE);
        tolakButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        tolakButton.addActionListener(e -> handleTolak());

        // Tombol Refresh
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(70, 130, 180)); // Warna biru tema
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        refreshButton.addActionListener(e -> handleRefresh());

        // Always add buttons but enable/disable based on assignment
        buttonPanel.add(terimaButton);
        buttonPanel.add(tolakButton);
        buttonPanel.add(refreshButton);

        // Enable/disable based on assignment
        boolean hasAssignment = controller.hasActiveAssignment();
        terimaButton.setEnabled(hasAssignment);
        tolakButton.setEnabled(hasAssignment);

        Panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void enableButtons(boolean enable) {
        if (terimaButton != null) terimaButton.setEnabled(enable);
        if (tolakButton != null) tolakButton.setEnabled(enable);
    }

    public JButton getTerimaButton() {
        return terimaButton;
    }

    public JButton getTolakButton() {
        return tolakButton;
    }
    private void loadAllPendingPickups() {
        // Mengambil semua data assignments
        List<PickupAssignment> allAssignments = controller.getAllAssignments(); // Ubah ke getAllAssignments()
        pickupTableModel.setRowCount(0);

        for (PickupAssignment assignment : allAssignments) {
            if (assignment != null && assignment.getPermintaan() != null) {
                Object[] row = {
                        assignment.getPermintaan().getNamaPelanggan(),
                        assignment.getPermintaan().getAlamat(),
                        assignment.getTotalWeight().toString(),
                        assignment.getTotalCost().toString(),
                        assignment.getStatus()  // Tambahkan status ke tabel
                };
                pickupTableModel.addRow(row);

                // Set current assignment ke baris pertama jika belum ada yang dipilih
                if (!controller.hasActiveAssignment() && assignment.getStatus().equals("Assigned")) {
                    controller.setCurrentAssignment(assignment);
                }
            }
        }

        loadDropboxLocations();

        // Pilih baris pertama dan aktifkan button sesuai status
        if (pickupTable.getRowCount() > 0) {
            pickupTable.setRowSelectionInterval(0, 0);
            String status = (String) pickupTableModel.getValueAt(0, 4); // Ambil status dari kolom ke-4
            boolean canAct = status.equals("Assigned");  // Enable hanya untuk status Assigned
            enableButtons(canAct);
        } else {
            enableButtons(false);
        }
    }

    private void loadSpecificPickup() {
        PickupAssignment assignment = controller.getCurrentAssignment();
        if (assignment != null && assignment.getPermintaan() != null) {
            Object[] row = {
                    assignment.getPermintaan().getNamaPelanggan(),
                    assignment.getPermintaan().getAlamat(),
                    assignment.getTotalWeight().toString(),
                    assignment.getTotalCost().toString()
            };
            pickupTableModel.setRowCount(0);
            pickupTableModel.addRow(row);
        }
        loadDropboxLocations();
    }

    private void loadDropboxLocations() {
        Object[][] dropboxData = controller.getDropboxTableData();
        dropboxTableModel.setRowCount(0);

        for (Object[] row : dropboxData) {
            dropboxTableModel.addRow(row);
        }
    }

    private void handleTerima() {
        if (!controller.hasActiveAssignment()) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada permintaan yang dipilih.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        PickupAssignment assignment = controller.getCurrentAssignment();
        LokasiDropbox dropbox = selectedDropbox != null ? selectedDropbox : assignment.getDropbox();

        if (dropbox == null) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih lokasi dropbox terlebih dahulu.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (controller.terimaPermintaan(dropbox)) {
            try {
                History history = new History();
                history.setPickupAssignmentId(assignment.getId());
                history.setWaktuSelesai(LocalDateTime.now());
                history.setLokasi(dropbox.getNamaDropbox());
                history.setKategoriSampah(assignment.getPermintaan().getKategoriSampahId());
                history.setBeratSampah(assignment.getTotalWeight());
                history.setHarga(assignment.getTotalCost());
                history.setStatusPenyelesaian("Diterima");

                HistoryController historyController = new HistoryController();
                historyController.insertHistory(history);

                JOptionPane.showMessageDialog(this,
                        "Permintaan pickup telah diterima dan akan segera diproses.",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                handleRefresh(); // Refresh table setelah update
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan ke history: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Gagal memproses permintaan.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleTolak() {
        if (!controller.hasActiveAssignment()) {
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
                PickupAssignment assignment = controller.getCurrentAssignment();
                String rejectionStatus = "Rejected - " + reason;

                PickupAssignmentController pickupController = new PickupAssignmentController();
                boolean updated = pickupController.updateAssignmentStatus(assignment.getId(), rejectionStatus);

                if (updated) {
                    History history = new History();
                    history.setPickupAssignmentId(assignment.getId());
                    history.setWaktuSelesai(LocalDateTime.now());
                    history.setLokasi(assignment.getDropbox().getNamaDropbox());
                    history.setKategoriSampah(assignment.getPermintaan().getKategoriSampahId());
                    history.setBeratSampah(assignment.getTotalWeight());
                    history.setHarga(assignment.getTotalCost());
                    history.setStatusPenyelesaian(rejectionStatus);

                    HistoryController historyController = new HistoryController();
                    historyController.insertHistory(history);

                    JOptionPane.showMessageDialog(this,
                            "Permintaan pickup telah ditolak dan status diperbarui.",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    handleRefresh(); // Refresh table setelah update
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal memperbarui status permintaan.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Gagal memproses penolakan: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Alasan penolakan harus diisi.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
        }
    }




    private void handleRefresh() {
        System.out.println("Memuat ulang data permintaan pickup...");

        // Memuat ulang semua data assignments
        List<PickupAssignment> allAssignments = controller.getAllAssignments();

        // Debug output
        allAssignments.forEach(assignment -> System.out.println("Assignment: " + assignment));

        // Kosongkan model tabel pickup
        pickupTableModel.setRowCount(0);

        // Tambahkan data ke tabel pickup
        for (PickupAssignment assignment : allAssignments) {
            if (assignment != null && assignment.getPermintaan() != null) {
                Object[] row = {
                        assignment.getPermintaan().getNamaPelanggan(),
                        assignment.getPermintaan().getAlamat(),
                        assignment.getTotalWeight().toString(),
                        assignment.getTotalCost().toString(),
                        assignment.getStatus()
                };
                pickupTableModel.addRow(row);
            }
        }

        // Refresh tampilan tabel
        pickupTable.revalidate();
        pickupTable.repaint();

        // Reset tombol jika tidak ada baris
        if (pickupTable.getRowCount() > 0) {
            pickupTable.setRowSelectionInterval(0, 0);
            updateButtonStates();
        } else {
            enableButtons(false);
        }

        // Refresh data dropbox
        loadDropboxLocations();
    }

    private void updateButtonStates() {
        int selectedRow = pickupTable.getSelectedRow();
        if (selectedRow >= 0) {
            String status = (String) pickupTableModel.getValueAt(selectedRow, 4); // Kolom status
            boolean canAct = "Assigned".equalsIgnoreCase(status); // Hanya aktif jika status "Assigned"
            enableButtons(canAct);
        } else {
            enableButtons(false);
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

    public JPanel getPanel() {
        return Panel;
    }
}