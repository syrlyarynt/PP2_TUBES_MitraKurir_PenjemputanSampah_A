package kelompok4.praktikumpemrograman2.view;

import kelompok4.praktikumpemrograman2.controller.PermintaanPenjemputanController;
import kelompok4.praktikumpemrograman2.controller.PickupAssignmentController;
import kelompok4.praktikumpemrograman2.model.JenisKategori;
import kelompok4.praktikumpemrograman2.model.PermintaanPenjemputan;
import kelompok4.praktikumpemrograman2.model.LokasiDropbox;
import kelompok4.praktikumpemrograman2.model.PickupAssignment;
import kelompok4.praktikumpemrograman2.model.Kurir;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MelihatPermintaanView extends JFrame {
    private JPanel panel;
    private JTable pickupTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterComboBox;
    private PermintaanPenjemputanController controller;
    private PickupAssignmentController assignmentController;

    public MelihatPermintaanView() {
        setTitle("Daftar Permintaan Penjemputan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controller = new PermintaanPenjemputanController();
        assignmentController = new PickupAssignmentController();

        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 250, 240));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 250, 240));

        JLabel lblTitle = new JLabel("Daftar Permintaan Penjemputan", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(139, 0, 0));
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFilter = new JLabel("Filter by:");
        filterComboBox = new JComboBox<>(new String[]{"Semua", "Hari Ini", "Minggu Ini", "Bulan Ini"});
        filterComboBox.setBackground(new Color(255, 160, 122));
        filterPanel.add(lblFilter);
        filterPanel.add(filterComboBox);
        filterPanel.setBackground(new Color(255, 250, 240));
        panel.add(filterPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Nama", "Alamat", "Jenis Sampah", "Berat (kg)", "Harga", "Status", "Lokasi Dropbox", "Kurir"};
        tableModel = new DefaultTableModel(columnNames, 0);
        pickupTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(255, 250, 240)); // Warna krem untuk baris genap
                    } else {
                        c.setBackground(new Color(255, 239, 213)); // Warna oranye muda untuk baris ganjil
                    }
                } else {
                    c.setBackground(getSelectionBackground());
                }
                return c;
            }
        };

        // Warna header tabel
        pickupTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel headerLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                headerLabel.setBackground(new Color(255, 160, 122)); // Warna oranye muda
                headerLabel.setForeground(Color.WHITE);             // Warna teks putih
                headerLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Font tebal
                headerLabel.setHorizontalAlignment(SwingConstants.CENTER); // Teks di tengah
                return headerLabel;
            }
        });
        pickupTable.setBackground(new Color(255, 250, 240));

        JScrollPane tableScrollPane = new JScrollPane(pickupTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        JButton pickupButton = new JButton("Pickup");
        JButton addButton = new JButton("Tambah Permintaan");

        pickupButton.setBackground(new Color(255, 160, 122));
        pickupButton.setForeground(Color.WHITE);
        pickupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        pickupButton.addActionListener(e -> handlePickup());

        addButton.setBackground(new Color(255, 160, 122));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.addActionListener(e -> showAddRequestForm());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(pickupButton);
        buttonPanel.setBackground(new Color(255, 250, 240));
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadPermintaanDataInBackground();

        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void loadPermintaanData() {
        tableModel.setRowCount(0);
        List<PermintaanPenjemputan> permintaanList = controller.getAllPermintaan();
        for (PermintaanPenjemputan permintaan : permintaanList) {
            LokasiDropbox dropbox = permintaan.getLokasiDropbox();
            PickupAssignment assignment = assignmentController.getAssignmentById(permintaan.getIdPermintaan());
            String dropboxName = (dropbox != null) ? dropbox.getNamaDropbox() : "-";
            String kurirName = (assignment != null && assignment.getKurir() != null) ? assignment.getKurir().getNama() : "Belum Ditentukan";

            tableModel.addRow(new Object[]{
                    permintaan.getIdPermintaan(),
                    permintaan.getNamaPelanggan(),
                    permintaan.getAlamat(),
                    permintaan.getKategoriSampah(),
                    permintaan.getBerat(),
                    permintaan.getHarga(),
                    permintaan.getStatus(),
                    dropboxName,
                    kurirName
            });
        }
    }

    private void loadPermintaanDataInBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                loadPermintaanData();
                return null;
            }
        };
        worker.execute();
    }


    private void handlePickup() {
        int selectedRow = pickupTable.getSelectedRow();
        if (selectedRow >= 0) {
            final int idPermintaan = (int) tableModel.getValueAt(selectedRow, 0);
            final PermintaanPenjemputan permintaan = controller.getPermintaanById(idPermintaan);

            if (permintaan != null) {
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            // Pilih Dropbox
                            Integer dropboxId = permintaan.getDropboxId();
                            if (dropboxId == null) {
                                List<LokasiDropbox> dropboxList = controller.getAllDropbox();
                                LokasiDropbox selectedDropbox = (LokasiDropbox) JOptionPane.showInputDialog(
                                        MelihatPermintaanView.this,
                                        "Pilih Dropbox untuk assignment:",
                                        "Pilih Dropbox",
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        dropboxList.toArray(),
                                        dropboxList.get(0)
                                );

                                if (selectedDropbox == null) {
                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                                            MelihatPermintaanView.this,
                                            "Harus memilih dropbox untuk melanjutkan pickup.",
                                            "Peringatan",
                                            JOptionPane.WARNING_MESSAGE
                                    ));
                                    return null;
                                }
                                dropboxId = selectedDropbox.getId();
                            }

                            // Get available couriers only
                            List<Kurir> allKurirList = controller.getAllKurir();
                            List<Kurir> availableKurirList = allKurirList.stream()
                                    .filter(kurir -> "Available".equals(kurir.getStatus()))
                                    .toList();

                            if (availableKurirList.isEmpty()) {
                                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                                        MelihatPermintaanView.this,
                                        "Tidak ada kurir yang tersedia saat ini.",
                                        "Peringatan",
                                        JOptionPane.WARNING_MESSAGE
                                ));
                                return null;
                            }

                            // Custom renderer untuk menampilkan status kurir
                            JComboBox<Kurir> kurirComboBox = new JComboBox<>(
                                    availableKurirList.toArray(new Kurir[0]));
                            kurirComboBox.setRenderer(new DefaultListCellRenderer() {
                                @Override
                                public Component getListCellRendererComponent(
                                        JList<?> list, Object value, int index,
                                        boolean isSelected, boolean cellHasFocus) {
                                    if (value instanceof Kurir kurir) {
                                        value = String.format("%s (%s)",
                                                kurir.getNama(),
                                                kurir.getStatus());
                                    }
                                    return super.getListCellRendererComponent(
                                            list, value, index, isSelected, cellHasFocus);
                                }
                            });

                            // Show dialog with custom combobox
                            int result = JOptionPane.showConfirmDialog(
                                    MelihatPermintaanView.this,
                                    kurirComboBox,
                                    "Pilih Kurir",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            if (result != JOptionPane.OK_OPTION) {
                                return null;
                            }

                            Kurir selectedKurir = (Kurir) kurirComboBox.getSelectedItem();
                            if (selectedKurir == null) {
                                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                                        MelihatPermintaanView.this,
                                        "Harus memilih kurir untuk melanjutkan pickup.",
                                        "Peringatan",
                                        JOptionPane.WARNING_MESSAGE
                                ));
                                return null;
                            }

                            // Buat PickupAssignment
                            PickupAssignment assignment = new PickupAssignment();
                            assignment.setPermintaanId(permintaan.getIdPermintaan());
                            assignment.setDropboxId(dropboxId);
                            assignment.setKurirId(selectedKurir.getId());
                            assignment.setStatus("Assigned");
                            assignment.setPickupDate(LocalDateTime.now());
                            assignment.setTotalWeight(permintaan.getBerat());
                            assignment.setTotalCost(permintaan.getHarga());

                            // Create assignment (courier status will be updated within PickupAssignmentController)
                            assignmentController.createAssignment(assignment, selectedKurir.getId());

                            // Update permintaan status
                            permintaan.setStatus("Dalam Proses");
                            controller.updatePermintaan(permintaan);

                            // Notify success and reload data
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(
                                        MelihatPermintaanView.this,
                                        "Pickup berhasil diproses dengan kurir: " + selectedKurir.getNama(),
                                        "Sukses",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                                loadPermintaanDataInBackground();
                            });

                        } catch (Exception e) {
                            SwingUtilities.invokeLater(() -> {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(
                                        MelihatPermintaanView.this,
                                        "Terjadi kesalahan saat memproses pickup: " + e.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            });
                        }
                        return null;
                    }
                };
                worker.execute();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Data permintaan tidak ditemukan.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih permintaan untuk pickup terlebih dahulu.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void showAddRequestForm() {
        JTextField namaField = new JTextField(20);
        JTextField alamatField = new JTextField(20);
        JTextField beratField = new JTextField(20);

        // Get kategori list and create custom ComboBox
        List<JenisKategori> kategoriList = controller.getAllKategoriSampah();
        DefaultComboBoxModel<JenisKategori> comboModel = new DefaultComboBoxModel<>(
                kategoriList.toArray(new JenisKategori[0])
        );
        JComboBox<JenisKategori> kategoriComboBox = new JComboBox<>(comboModel);

        // Get dropbox list
        List<LokasiDropbox> dropboxList = controller.getAllDropbox();
        DefaultComboBoxModel<LokasiDropbox> dropboxModel = new DefaultComboBoxModel<>(
                dropboxList.toArray(new LokasiDropbox[0])
        );
        JComboBox<LokasiDropbox> dropboxComboBox = new JComboBox<>(dropboxModel);

        // Custom renderer for dropbox to show name and available capacity
        dropboxComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof LokasiDropbox) {
                    LokasiDropbox dropbox = (LokasiDropbox) value;
                    BigDecimal availableCapacity = dropbox.getKapasitasMax()
                            .subtract(dropbox.getKapasitasTerisi());
                    value = String.format("%s (Tersedia: %s kg)",
                            dropbox.getNamaDropbox(),
                            availableCapacity.toString());
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        // Update panel to include dropbox selection
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Nama:"));
        panel.add(namaField);
        panel.add(new JLabel("Alamat:"));
        panel.add(alamatField);
        panel.add(new JLabel("Jenis Sampah:"));
        panel.add(kategoriComboBox);
        panel.add(new JLabel("Berat (kg):"));
        panel.add(beratField);
        panel.add(new JLabel("Lokasi Dropbox:"));
        panel.add(dropboxComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Tambah Permintaan", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validate inputs
                String nama = namaField.getText().trim();
                String alamat = alamatField.getText().trim();
                String beratStr = beratField.getText().trim();
                if (nama.isEmpty() || alamat.isEmpty() || beratStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Semua field harus diisi!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Parse berat and validate
                double berat;
                try {
                    berat = Double.parseDouble(beratStr);
                    if (berat <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Berat harus berupa angka positif!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get selected kategori
                JenisKategori selectedKategori = (JenisKategori) kategoriComboBox.getSelectedItem();
                if (selectedKategori == null) {
                    JOptionPane.showMessageDialog(this,
                            "Silakan pilih jenis sampah!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get and validate selected dropbox
                LokasiDropbox selectedDropbox = (LokasiDropbox) dropboxComboBox.getSelectedItem();
                if (selectedDropbox == null) {
                    JOptionPane.showMessageDialog(this,
                            "Silakan pilih lokasi dropbox!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check dropbox capacity
                BigDecimal newBerat = BigDecimal.valueOf(berat);
                BigDecimal availableCapacity = selectedDropbox.getKapasitasMax()
                        .subtract(selectedDropbox.getKapasitasTerisi());

                if (newBerat.compareTo(availableCapacity) > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Kapasitas dropbox tidak mencukupi! Kapasitas tersedia: "
                                    + availableCapacity + " kg",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create new permintaan
                PermintaanPenjemputan permintaan = new PermintaanPenjemputan(
                        nama,
                        alamat,
                        selectedKategori.getId(),
                        berat
                );
                permintaan.setDropboxId(selectedDropbox.getId());

                // Update dropbox capacity
                selectedDropbox.setKapasitasTerisi(
                        selectedDropbox.getKapasitasTerisi().add(newBerat)
                );
                controller.getDropboxController().updateDropbox(selectedDropbox);

                // Save permintaan
                controller.createPermintaan(
                        permintaan.getNamaPelanggan(),
                        permintaan.getAlamat(),
                        permintaan.getKategoriSampahId(),
                        permintaan.getBerat().doubleValue(),
                        permintaan.getDropboxId(),
                        permintaan.getHarga()
                );

                loadPermintaanData();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan.");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Terjadi kesalahan: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
