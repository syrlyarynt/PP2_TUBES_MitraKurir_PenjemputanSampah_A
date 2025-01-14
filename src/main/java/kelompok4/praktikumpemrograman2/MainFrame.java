package kelompok4.praktikumpemrograman2;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import kelompok4.praktikumpemrograman2.utils.DataCache;
import kelompok4.praktikumpemrograman2.view.*;
import net.miginfocom.swing.MigLayout;
import kelompok4.praktikumpemrograman2.controller.*;
import kelompok4.praktikumpemrograman2.services.*;
import kelompok4.praktikumpemrograman2.model.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class MainFrame {
    private static SqlSession sqlSession;
    private static JFrame mainFrame;
    private static JProgressBar progressBar;
    private static JLabel statusLabel;
    private static int totalLoadingSteps = 8;
    private static int currentStep = 0;

    public static void main(String[] args) {
        FlatGrayIJTheme.setup();
        showLoadingFrame();

        CompletableFuture.runAsync(() -> {
            try {
                initializeApplication();
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error saat inisialisasi: " + e.getMessage());
            }
        }).thenRun(() -> {
            SwingUtilities.invokeLater(() -> {
                hideLoadingFrame();
                showSecondLoadingFrame();
                prepareAndShowMainFrame();
            });
        });
    }

    private static void showLoadingFrame() {
        JFrame loadingFrame = new JFrame("Loading");
        loadingFrame.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        statusLabel = new JLabel("Memuat aplikasi...");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        loadingFrame.add(panel);
        loadingFrame.pack();
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.setVisible(true);
        mainFrame = loadingFrame;
    }

    private static void showSecondLoadingFrame() {
        JFrame loadingFrame = new JFrame("Loading");
        loadingFrame.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JProgressBar secondProgressBar = new JProgressBar();
        secondProgressBar.setIndeterminate(true);

        JLabel secondStatusLabel = new JLabel("Sedang membuka aplikasi...");
        secondStatusLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(secondStatusLabel, BorderLayout.NORTH);
        panel.add(secondProgressBar, BorderLayout.CENTER);

        loadingFrame.add(panel);
        loadingFrame.pack();
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.setVisible(true);
        mainFrame = loadingFrame;
    }

    private static void prepareAndShowMainFrame() {
        CompletableFuture.runAsync(() -> {
            try {
                JFrame frame = new JFrame("Penjemputan Sampah");

                // Ubah ukuran ikon aplikasi
                ImageIcon originalIcon = new ImageIcon(MainFrame.class.getResource("/logo.png"));
                Image scaledImage = originalIcon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                frame.setIconImage(scaledIcon.getImage());

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1000, 800);

                JPanel cardPanel = new JPanel(new CardLayout());
                JPanel mainMenuPanel = createMainMenuPanel(cardPanel);

                // Initialize all services and controllers
                JenisKategoriService kategoriService = new JenisKategoriService(sqlSession);
                JenisKategoriController kategoriController = new JenisKategoriController(kategoriService);

                createAndAddPanels(cardPanel, mainMenuPanel, kategoriController);
                frame.add(cardPanel, BorderLayout.CENTER);

                SwingUtilities.invokeLater(() -> {
                    hideLoadingFrame();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                    mainFrame = frame;
                    setupShutdownHook();
                });
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error saat membuat panel: " + e.getMessage());
            }
        });
    }


    private static void hideLoadingFrame() {
        if (mainFrame != null) {
            mainFrame.dispose();
        }
    }


    private static void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(status));
    }

    private static void updateProgressBar() {
        currentStep++;
        int progress = (int) ((currentStep / (double) totalLoadingSteps) * 100);
        SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
    }

    private static void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private static void initializeApplication() throws Exception {
        updateStatus("Menginisialisasi koneksi database...");
        sqlSession = MyBatisUtil.getSqlSession();
        updateProgressBar();

        // Pre-load all services data
        preloadDropboxRateData();
        preloadHistoryData();
        preloadJenisKategoriData();
        preloadKurirData();
        preloadLokasiDropboxData();
        preloadPermintaanPenjemputanData();
        preloadPickupAssignmentData();
        preloadTotalSampahData();
    }

    private static void preloadDropboxRateData() {
        updateStatus("Memuat data Dropbox Rate...");
        try {
            DropboxRateService service = new DropboxRateService(sqlSession);
            DataCache.put("dropbox_rates", service.getAllRates());
        } catch (Exception e) {
            System.err.println("Error loading Dropbox Rate data: " + e.getMessage());
        }
        updateProgressBar();
    }

    private static void preloadHistoryData() {
        updateStatus("Memuat data History...");
        try {
            HistoryService service = new HistoryService(sqlSession);
            DataCache.put("history_all", service.getAllHistory(sqlSession));
        } catch (Exception e) {
            System.err.println("Error loading History data: " + e.getMessage());
        }
        updateProgressBar();
    }

    private static void preloadJenisKategoriData() {
        updateStatus("Memuat data Jenis Kategori...");
        try {
            JenisKategoriService service = new JenisKategoriService(sqlSession);
            DataCache.put("kategori_all", service.getAllKategori());
        } catch (Exception e) {
            System.err.println("Error loading Jenis Kategori data: " + e.getMessage());
        }
        updateProgressBar();
    }

    private static void preloadKurirData() {
        updateStatus("Memuat data Kurir...");
        try {
            KurirService service = new KurirService(sqlSession);
            DataCache.put("kurir_all", service.getAllKurir());
        } catch (Exception e) {
            System.err.println("Error loading Kurir data: " + e.getMessage());
        }
        updateProgressBar();
    }

    private static void preloadLokasiDropboxData() {
        updateStatus("Memuat data Lokasi Dropbox...");
        try {
            LokasiDropboxService service = new LokasiDropboxService(sqlSession);
            DataCache.put("dropbox_all", service.getAllDropbox());
        } catch (Exception e) {
            System.err.println("Error loading Lokasi Dropbox data: " + e.getMessage());
        }
        updateProgressBar();
    }

    private static void preloadPermintaanPenjemputanData() {
        updateStatus("Memuat data Permintaan Penjemputan...");
        try {
            PermintaanPenjemputanService service = new PermintaanPenjemputanService(sqlSession);
            DataCache.put("permintaan_all", service.getAllPermintaan());
        } catch (Exception e) {
            System.err.println("Error loading Permintaan Penjemputan data: " + e.getMessage());
        }
        updateProgressBar();
    }

    private static void preloadPickupAssignmentData() {
        updateStatus("Memuat data Pickup Assignment...");
        try {
            PickupAssignmentService service = new PickupAssignmentService(sqlSession);
            DataCache.put("assignment_all", service.getAllAssignments());
        } catch (Exception e) {
            System.err.println("Error loading Pickup Assignment data: " + e.getMessage());
        }
        updateProgressBar();
    }

    private static void preloadTotalSampahData() {
        updateStatus("Memuat data Total Sampah...");
        try {
            TotalSampahService service = new TotalSampahService(sqlSession);
            DataCache.put("sampah_all", service.getAll());
        } catch (Exception e) {
            System.err.println("Error loading Total Sampah data: " + e.getMessage());
        }
        updateProgressBar();
    }

    private static void showMainFrame() {
        JFrame frame = new JFrame("Penjemputan Sampah");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        JPanel cardPanel = new JPanel(new CardLayout());
        JPanel mainMenuPanel = createMainMenuPanel(cardPanel);

        try {
            // Initialize all services and controllers
            JenisKategoriService kategoriService = new JenisKategoriService(sqlSession);
            JenisKategoriController kategoriController = new JenisKategoriController(kategoriService);

            DropboxRateService dropboxRateService = new DropboxRateService(sqlSession);
            HistoryService historyService = new HistoryService(sqlSession);
            KurirService kurirService = new KurirService(sqlSession);
            LokasiDropboxService lokasiDropboxService = new LokasiDropboxService(sqlSession);
            PermintaanPenjemputanService permintaanService = new PermintaanPenjemputanService(sqlSession);
            PickupAssignmentService assignmentService = new PickupAssignmentService(sqlSession);
            TotalSampahService totalSampahService = new TotalSampahService(sqlSession);

            createAndAddPanels(cardPanel, mainMenuPanel, kategoriController);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error saat membuat panel: " + e.getMessage());
            return;
        }

        frame.add(cardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        mainFrame = frame;

        setupShutdownHook();
    }

    private static void createAndAddPanels(JPanel cardPanel, JPanel mainMenuPanel, JenisKategoriController controller) {
        // Create panels with cached data
        JPanel jenisDanKategoriPanel = createPagePanel("Jenis dan Kategori",
                new JenisDanKategoriView(controller).getPanel(), cardPanel, mainMenuPanel);
        JPanel melihatPermintaanPanel = createPagePanel("Permintaan Penjemputan",
                new MelihatPermintaanView().getPanel(), cardPanel, mainMenuPanel);
        JPanel menerimaPermintaanPanel = createPagePanel("Menerima Permintaan Penjemputan",
                new MenerimaPermintaanView().getPanel(), cardPanel, mainMenuPanel);
        JPanel lokasiDropboxPanel = createPagePanel("Lokasi Dropbox",
                new LokasiDropboxView().getPanel(), cardPanel, mainMenuPanel);
        JPanel totalSampahPanel = createPagePanel("Total Sampah",
                new TotalSampahView().getPanel(), cardPanel, mainMenuPanel);
        JPanel historyPenjemputanPanel = createPagePanel("History Penjemputan",
                new HistoryPenjemputanView().getPanel(), cardPanel, mainMenuPanel);
        JPanel dropboxRatePanel = createPagePanel("Dropbox Rate",
                new DropboxRateView().getMainPanel(), cardPanel, mainMenuPanel);
        JPanel crudKurirPanel = createPagePanel("CRUD Kurir",
                new CrudKurirView(), cardPanel, mainMenuPanel);

        // Add panels to card layout
        cardPanel.add(mainMenuPanel, "MainMenu");
        cardPanel.add(jenisDanKategoriPanel, "JenisDanKategori");
        cardPanel.add(melihatPermintaanPanel, "MelihatPermintaan");
        cardPanel.add(menerimaPermintaanPanel, "MenerimaPermintaan");
        cardPanel.add(lokasiDropboxPanel, "LokasiDropbox");
        cardPanel.add(totalSampahPanel, "TotalSampah");
        cardPanel.add(historyPenjemputanPanel, "HistoryPenjemputan");
        cardPanel.add(dropboxRatePanel, "DropboxRate");
        cardPanel.add(crudKurirPanel, "CrudKurir");
    }

    private static JPanel createPagePanel(String title, JPanel pageContent, JPanel cardPanel, JPanel mainMenuPanel) {
        JPanel panel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Kembali ke Halaman Utama");
        backButton.setBackground(new Color(255, 160, 122));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
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

        String[][] menuItems = {
                {"Jenis dan Kategori", "JenisDanKategori"},
                {"Permintaan Penjemputan", "MelihatPermintaan"},
                {"Menerima Permintaan Penjemputan", "MenerimaPermintaan"},
                {"Lokasi Dropbox", "LokasiDropbox"},
                {"Total Sampah", "TotalSampah"},
                {"History Penjemputan", "HistoryPenjemputan"},
                {"Dropbox Rate", "DropboxRate"},
                {"CRUD Kurir", "CrudKurir"}
        };

        for (String[] menuItem : menuItems) {
            JButton button = buttonStyle(menuItem[0], cardPanel, menuItem[1]);
            mainMenuPanel.add(button, "wrap, w 300!, h 30!");
        }

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

    private static void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DataCache.clear();
            if (sqlSession != null) {
                sqlSession.close();
            }
        }));
    }
}