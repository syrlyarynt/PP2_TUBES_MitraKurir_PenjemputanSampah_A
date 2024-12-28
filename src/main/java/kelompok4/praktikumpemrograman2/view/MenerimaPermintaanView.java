package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MenerimaPermintaanView extends JFrame {
    private JPanel Panel;
    private JTable pickupTable;
    private JTable secondTable;
    private DefaultTableModel tableModel;
    private DefaultTableModel secondTableModel;

    public MenerimaPermintaanView() {
        setTitle("Permintaan Pickup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel utama dengan BorderLayout
        Panel = new JPanel(new BorderLayout());
        Panel.setBackground(new Color(255, 250, 240));

        // Header Panel
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

        // Panel untuk tabel (keduanya vertikal)
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(new Color(255, 250, 240));

        String[] columnNames = {"Nama", "Alamat", "Berat(kg)", "Harga"};
        tableModel = new DefaultTableModel(columnNames, 0);
        pickupTable = createCustomTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(pickupTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 150));
        tableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.add(Box.createVerticalStrut(10));
        tablePanel.add(tableScrollPane);

        // Tabel kedua
        JLabel lblDropbox = new JLabel("Lokasi Dropbox", SwingConstants.LEFT);
        lblDropbox.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblDropbox.setForeground(new Color(139, 0, 0));
        lblDropbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.add(Box.createVerticalStrut(10));
        tablePanel.add(lblDropbox);

        String[] secondColumnNames = {"Nama Dropbox", "Alamat", "Jarak"};
        secondTableModel = new DefaultTableModel(secondColumnNames, 0);
        secondTable = createCustomTable(secondTableModel);

        JScrollPane secondTableScrollPane = new JScrollPane(secondTable);
        secondTableScrollPane.setPreferredSize(new Dimension(400, 150));
        secondTableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.add(Box.createVerticalStrut(10));
        tablePanel.add(secondTableScrollPane);

        Panel.add(tablePanel, BorderLayout.CENTER);

        // Panel untuk tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton terimaButton = new JButton("Terima");
        terimaButton.setBackground(new Color(34, 139, 34));
        terimaButton.setForeground(Color.WHITE);

        JButton tolakButton = new JButton("Tolak");
        tolakButton.setBackground(new Color(255, 160, 122));
        tolakButton.setForeground(Color.WHITE);

        buttonPanel.add(terimaButton);
        buttonPanel.add(tolakButton);
        Panel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(Panel);
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);

        String[][] dataDummy = {
            {"John Doe", "Jl. Merdeka No. 1", "3 Kg", "Rp 50.000"},
        };
        setPickupData(dataDummy);

        String[][] secondDataDummy = {
            {"Dropbox Cipedes", "Jl.Setiabudi Regency", "1.8 KM"},
            {"Dropbox Gegerkalong", "Jl.Gegerkalong Hilir", "2.3 KM"},
            {"Dropbox Ledeng", "Jl.Setiabudi", "5.0 KM"},
        };
        setSecondTableData(secondDataDummy);
    }

    public void setPickupData(String[][] data) {
        tableModel.setRowCount(0);
        for (String[] row : data) {
            tableModel.addRow(row);
        }
    }

    public void setSecondTableData(String[][] data) {
        secondTableModel.setRowCount(0);
        for (String[] row : data) {
            secondTableModel.addRow(row);
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
