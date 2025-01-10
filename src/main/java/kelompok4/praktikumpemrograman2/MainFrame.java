package kelompok4.praktikumpemrograman2;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.ActionEvent;
import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import kelompok4.praktikumpemrograman2.view.*;
import net.miginfocom.swing.MigLayout;
import kelompok4.praktikumpemrograman2.controller.JenisKategoriController;
import kelompok4.praktikumpemrograman2.services.JenisKategoriService;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class MainFrame {

    private static SqlSession sqlSession;

    public static void main(String[] args) {
        FlatGrayIJTheme.setup();

        JFrame frame = new JFrame("Penjemputan Sampah");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        JPanel cardPanel = new JPanel(new CardLayout());
        JPanel mainMenuPanel = createMainMenuPanel(cardPanel);

        try {
            sqlSession = MyBatisUtil.getSqlSession();
            JenisKategoriService service = new JenisKategoriService(sqlSession);
            JenisKategoriController controller = new JenisKategoriController(service);

            JPanel jenisDanKategoriPanel = createPagePanel("Jenis dan Kategori", new JenisDanKategori(controller).getPanel(), cardPanel, mainMenuPanel);
            JPanel melihatPermintaanPanel = createPagePanel("Permintaan Penjemputan", new MelihatPermintaanView().getPanel(), cardPanel, mainMenuPanel);
            JPanel menerimaPermintaanPanel = createPagePanel("Menerima Permintaan Penjemputan", new MenerimaPermintaanView().getPanel(), cardPanel, mainMenuPanel);
            JPanel lokasiDropboxPanel = createPagePanel("Lokasi Dropbox", new LokasiDropboxView().getPanel(), cardPanel, mainMenuPanel);
            JPanel totalSampahPanel = createPagePanel("Total Sampah", new TotalSampahView().getPanel(), cardPanel, mainMenuPanel);
            JPanel historyPenjemputanPanel = createPagePanel("History Penjemputan", new HistoryPenjemputan().getPanel(), cardPanel, mainMenuPanel);
            JPanel dropboxRatePanel = createPagePanel("Dropbox Rate", new DropboxRateView().getMainPanel(), cardPanel, mainMenuPanel);
            JPanel crudKurirPanel = createPagePanel("CRUD Kurir", new CrudKurirView(), cardPanel, mainMenuPanel);


            // Menambahkan panel ke dalam cardLayout
            cardPanel.add(mainMenuPanel, "MainMenu");
            cardPanel.add(jenisDanKategoriPanel, "JenisDanKategori");
            cardPanel.add(melihatPermintaanPanel, "MelihatPermintaan");
            cardPanel.add(menerimaPermintaanPanel, "MenerimaPermintaan");
            cardPanel.add(lokasiDropboxPanel, "LokasiDropbox");
            cardPanel.add(totalSampahPanel, "TotalSampah");
            cardPanel.add(historyPenjemputanPanel, "HistoryPenjemputan");
            cardPanel.add(dropboxRatePanel, "DropboxRate");
            cardPanel.add(crudKurirPanel, "CrudKurir");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error initializing database connection: " + e.getMessage());
            return;
        }

        frame.add(cardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }));
    }

    private static JPanel createPagePanel(String title, JPanel pageContent, JPanel cardPanel, JPanel mainMenuPanel) {
        JPanel panel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Kembali ke Main Menu");
        backButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) cardPanel.getLayout();
            layout.show(cardPanel, "MainMenu");
        });

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.add(backButton);

        panel.add(new JScrollPane(pageContent), BorderLayout.CENTER);
        panel.add(footerPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static JPanel createMainMenuPanel(JPanel cardPanel) {
        JPanel mainMenuPanel = new JPanel(new MigLayout("align center", "[center]", "200[][][][][][][]"));

        JLabel welcomeLabel = new JLabel("Selamat datang di halaman penjemputan sampah", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeLabel.setForeground(new Color(139, 0, 0));
        mainMenuPanel.add(welcomeLabel, "wrap");
        mainMenuPanel.setBackground(new Color(255, 250, 240));

        JLabel instructionLabel = new JLabel("Pilih menu berikut:", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainMenuPanel.add(instructionLabel, "wrap");

        JButton jenisKategoriButton = buttonStyle("Jenis dan Kategori", cardPanel, "JenisDanKategori");
        JButton permintaanButton = buttonStyle("Permintaan Penjemputan", cardPanel, "MelihatPermintaan");
        JButton menerimaButton = buttonStyle("Menerima Permintaan Penjemputan", cardPanel, "MenerimaPermintaan");
        JButton lokasiButton = buttonStyle("Lokasi Dropbox", cardPanel, "LokasiDropbox");
        JButton totalSampahButton = buttonStyle("Total Sampah", cardPanel, "TotalSampah");
        JButton historyButton = buttonStyle("History Penjemputan", cardPanel, "HistoryPenjemputan");
        JButton dropboxRateButton = buttonStyle("Dropbox Rate", cardPanel, "DropboxRate");
        JButton crudKurirButton = buttonStyle("CRUD Kurir", cardPanel, "CrudKurir");

        mainMenuPanel.add(jenisKategoriButton, "wrap, w 300!, h 30!");
        mainMenuPanel.add(permintaanButton, "wrap, w 300!, h 30!");
        mainMenuPanel.add(menerimaButton, "wrap, w 300!, h 30!");
        mainMenuPanel.add(lokasiButton, "wrap, w 300!, h 30!");
        mainMenuPanel.add(totalSampahButton, "wrap, w 300!, h 30!");
        mainMenuPanel.add(historyButton, "wrap, w 300!, h 30!");
        mainMenuPanel.add(dropboxRateButton, "wrap, w 300!, h 30!");
        mainMenuPanel.add(crudKurirButton, "wrap, w 300!, h 30!");

        return mainMenuPanel;
    }

    private static JButton buttonStyle(String text, JPanel cardPanel, String panelName) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setForeground(new Color(255, 255, 255));
        button.setBackground(new Color(255, 160, 122));
        button.setPreferredSize(new Dimension(300, 40));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> showPanel(cardPanel, panelName));

        return button;
    }

    private static void showPanel(JPanel cardPanel, String panelName) {
        CardLayout layout = (CardLayout) cardPanel.getLayout();
        layout.show(cardPanel, panelName);
    }
}
