package kelompok4.praktikumpemrograman2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.apache.ibatis.session.SqlSession;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import kelompok4.praktikumpemrograman2.model.*;
import kelompok4.praktikumpemrograman2.services.PermintaanPenjemputanService;
import kelompok4.praktikumpemrograman2.controller.PermintaanPenjemputanController;
import kelompok4.praktikumpemrograman2.helper.*;;

public class PermintaanPenjemputanView extends JFrame 
implements OnPermintaanPenjemputanSaved,OnJenisKategoriSaved {
    private JPanel Panel;
    private JTable pickupTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterComboBox;
    private JComboBox<String> selectKategori;
    private List<PermintaanPenjemputan> records;
    private List<JenisKategori> kategoris;
    public PermintaanPenjemputanView() {
        setTitle("Daftar Pickup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel utama dengan BorderLayout
        Panel = new JPanel(new BorderLayout());
        Panel.setBackground(new Color(255, 250, 240)); // Background warna cream muda

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 250, 240)); // Background cream muda
        kategoris = getKategori();
        // Membuat panel pembungkus untuk memberi jarak pada label
        JPanel titleWrapperPanel = new JPanel();
        titleWrapperPanel.setBackground(new Color(255, 250, 240)); // Background sama dengan header
        titleWrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Memberikan margin atas 10px
        Locale locale = new Locale("id", "ID");      
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        currencyFormatter.setGroupingUsed(true);
        // Label judul
        JLabel lblTitle = new JLabel("Daftar Pickup", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(new Color(139, 0, 0));
        
        selectKategori = new JComboBox<String>();
        for(JenisKategori item: kategoris){
            selectKategori.addItem(item.getNama());
        }
        // Menambahkan label ke dalam wrapper
        titleWrapperPanel.add(lblTitle);

        // Menambahkan titleWrapperPanel ke headerPanel
        headerPanel.add(titleWrapperPanel, BorderLayout.CENTER);

        // Panel Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFilter = new JLabel("Filter by:");
        lblFilter.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblFilter.setForeground(new Color(139, 0, 0));
        filterComboBox = new JComboBox<>(new String[]{"Terdekat", "Jenis Sampah"});
        filterComboBox.setBackground(new Color(255, 160, 122));
        filterPanel.add(lblFilter);
        filterPanel.add(filterComboBox);
        filterPanel.setBackground(new Color(255, 250, 240)); // Background cream muda

        // Panel Kontainer untuk Filter dan Tabel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(255, 250, 240));

        // Tabel Pickup
        String[] columnNames = {"Nama", "Alamat", "Jenis Sampah","Berat", "Harga"};
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

        // Mengatur renderer untuk setiap kolom dengan alignment dan font
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER); // Align ke tengah
        renderer.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Set font
        DefaultTableCellRenderer numbered = new DefaultTableCellRenderer();
        numbered.setHorizontalAlignment(SwingConstants.RIGHT); // Align ke tengah
        numbered.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Mengatur renderer untuk setiap kolom
        pickupTable.getColumnModel().getColumn(0).setCellRenderer(renderer); // Kolom "Nama"
        pickupTable.getColumnModel().getColumn(1).setCellRenderer(renderer); // Kolom "Alamat"
        pickupTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selectKategori)); // Kolom "Jenis Sampah"
        pickupTable.getColumnModel().getColumn(3).setCellRenderer(numbered); // Kolom "Berat"
        pickupTable.getColumnModel().getColumn(4).setCellRenderer(numbered); //Harga
        // Mengubah latar belakang tabel menjadi oranye muda
        pickupTable.setBackground(new Color(255, 239, 213)); // Latar belakang tabel oranye muda

        pickupTable.getTableHeader().setBackground(new Color(255, 160, 122)); // Background header tabel oranye muda
        pickupTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));
        pickupTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane tableScrollPane = new JScrollPane(pickupTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 150)); // Ukuran tabel lebih kecil

        // Panel untuk tombol Add, Update, Delete, dan Pickup
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Posisikan tombol di tengah
        JButton addButton = new JButton("Add Data");
        JButton updateButton = new JButton("Update Data");
        JButton deleteButton = new JButton("Delete Data");
        JButton pickupButton = new JButton("Pickup");
        JButton refreshButton = new JButton("Refresh");
        int centerX = 600;
        int centerY = 200;
        addButton.addActionListener(e->{
            FormPermintaan formPermintaan = new FormPermintaan(this);
            formPermintaan.setLocation(new Point(centerX,centerY));
            formPermintaan.setVisible(true);
        });
        refreshButton.addActionListener(e->{
            refreshData();
        });
        updateButton.addActionListener(e->{
            int index = pickupTable.getSelectedRow();
            if(index<0){
                showPopUp("Maaf", "Anda belum memilih baris data yang ada!");
            } else {
                PermintaanPenjemputan permintaan = records.get(index);
                String[] values = new String[pickupTable.getColumnCount()];
                for(int i=0;i<pickupTable.getColumnCount();i++){
                    values[i] = (String)tableModel.getValueAt(index, i);
                }
                permintaan.setNamaPelanggan(values[0]);
                permintaan.setAlamat(values[1]);
                permintaan.setKategoriSampah(values[2]);
                try {
                    Number berat = currencyFormatter.parse(values[3]);
                    Number harga = currencyFormatter.parse(values[4]);
                    permintaan.setBerat(new BigDecimal(berat.toString()));
                    permintaan.setHarga(new BigDecimal(harga.toString()));
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                updatePermintaan(permintaan);
                refreshData();
                showPopUp("Selamat", "Update data permintaan Sukses!");
                 
            }
            
            
        });
        deleteButton.addActionListener(e->{
            int index = pickupTable.getSelectedRow();
            if(index<0){
                showPopUp("Maaf", "Anda belum memilih baris data yang ada!");
                 
            } else {
                PermintaanPenjemputan permintaan = records.get(index);
                deletePermintaan(permintaan.getIdPermintaan());
                refreshData();
                showPopUp("Informasi", "Hapus Permintaan Sukses!");
                
            }
            
            
        });
        // Atur warna dan font tombol
        addButton.setBackground(new Color(255, 160, 122));
        updateButton.setBackground(new Color(255, 160, 122));
        deleteButton.setBackground(new Color(255, 160, 122));
        pickupButton.setBackground(new Color(255, 160, 122));
        refreshButton.setBackground(new Color(255, 160, 122));

        addButton.setForeground(Color.WHITE);
        updateButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        pickupButton.setForeground(Color.WHITE);

        // Tambahkan tombol ke panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(pickupButton); // Pickup tetap di tengah
        buttonPanel.add(refreshButton);

        
        // Tambahkan filterPanel ke atas centerPanel
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH); // Menambahkan panel tombol ke bagian bawah tabel

        // Tambahkan komponen ke panel utama
        Panel.add(headerPanel, BorderLayout.NORTH);
        Panel.add(centerPanel, BorderLayout.CENTER);

        // Tambahkan panel utama ke frame
        setContentPane(Panel);

        // Sesuaikan ukuran frame
        pack();
        setLocationRelativeTo(null); // Posisikan frame di tengah layar

        // Data dummy untuk ditampilkan di tabel

        // String[][] dataDummy = {
        //     {"John Doe", "Jl. Merdeka No. 1", "Organik", "Rp 50.000"},
        //     {"Jane Smith", "Jl. Pahlawan No. 2", "Anorganik", "Rp 30.000"},
        //     {"Andi Wijaya", "Jl. Raya No. 3", "Bahan Keras", "Rp 75.000"},
        //     {"Dewi Lestari", "Jl. Kebangsaan No. 4", "Organik", "Rp 45.000"},
        //     {"Budi Santoso", "Jl. Taman No. 5", "Elektronik", "Rp 100.000" }
        // };
        refreshData();
         // Menampilkan data dummy di tabel
    }
    private void showPopUp(String title,String message){
        int centerX = 600;
        int centerY = 200;
        MessageBox messageBox = new MessageBox(title, message);
                messageBox.setLocation(new Point(centerX,centerY));
                messageBox.setVisible(true);
    }
    private void updatePermintaan(PermintaanPenjemputan permintaan){
        SqlSession session = MyBatisUtil.getSqlSession();
        try {
            PermintaanPenjemputanService service = new PermintaanPenjemputanService(session);
            PermintaanPenjemputanController controller = new PermintaanPenjemputanController(service);
            controller.updatePermintaan(permintaan);
            session.commit();
        } finally {
            session.close();
        }
    }
    private void deletePermintaan(int id){
        SqlSession session = MyBatisUtil.getSqlSession();
        try {
            PermintaanPenjemputanService service = new PermintaanPenjemputanService(session);
            PermintaanPenjemputanController controller = new PermintaanPenjemputanController(service);
            controller.deletePermintaan(id);
            session.commit();
        } finally {
            session.close();
        }
    }
    public static List<JenisKategori> getKategori(){
        SqlSession session = MyBatisUtil.getSqlSession();
        try {
            JenisKategoriMapper mapper = session.getMapper(JenisKategoriMapper.class);
            // Correct List from java.util
            List<JenisKategori> records = mapper.getAllKategori();
            return records; 
        } finally {
            session.close();
        }
    }
    
    public void refreshData(){
        SqlSession session = MyBatisUtil.getSqlSession();
        try {
            PermintaanPenjemputanMapper mapper = session.getMapper(PermintaanPenjemputanMapper.class);
            records = mapper.getAllPermintaan();
            String[][] datas = new String[records.size()][5];
            int i=0;
            Locale locale = new Locale("id", "ID");      
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            for (PermintaanPenjemputan entity : records) {
                // System.out.println(entity.getId() + " - " + entity.getName() + " - " + entity.getDescription());
                datas[i][0]=entity.getNamaPelanggan();
                datas[i][1]=entity.getAlamat();
                datas[i][2]=entity.getKategoriSampah();
                datas[i][3]= String.format("%.2f",entity.getBerat());
                datas[i][4]= currencyFormatter.format(entity.getHarga());//String.format("%.2f",entity.getHarga());
                i++;
            }
            setPickupData(datas);
        } finally {
            session.close();
        }
    }
    // Method untuk mengatur data tabel
    public void setPickupData(String[][] data) {
        tableModel.setRowCount(0); // Hapus data lama
        for (String[] row : data) {
            tableModel.addRow(row);
        }
    }

    // Method untuk mendapatkan pilihan filter
    public String getSelectedFilter() {
        return filterComboBox.getSelectedItem().toString();
    }

    public JPanel getPanel() {
        return Panel;
    }
    @Override
    public void onSimpanPermintaan(Boolean saved) {
        if(saved) {
            refreshData();
        }
    }
    @Override
    public void onSimpanKategori(Boolean saved) {
        if(saved){
            kategoris = getKategori();
            selectKategori = new JComboBox<String>();
            for(JenisKategori item: kategoris){
                selectKategori.addItem(item.getNama());
            }
            pickupTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(selectKategori)); 
        }
    }
}
