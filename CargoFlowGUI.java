package cargoflow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CargoFlowGUI extends JFrame {
    private PriorityQueueHeap<Kargo> antrianKargo = new PriorityQueueHeap<>(100);
    private DynamicStack<Kargo> logHistory = new DynamicStack<>();
    
    private JTable tabelKargo;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtNama, txtTujuan, txtBerat, txtCari;
    private JComboBox<String> cbPrioritas, cbSort;
    private JLabel lblTotal, lblStack;

    public CargoFlowGUI() {
        setTitle("CargoFlow - Airport Cargo Logistics System v1.0");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Panel Input Form (Kiri)
        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 15));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Registrasi Kargo Baru"));
        
        pnlForm.add(new JLabel("ID Kargo:")); txtId = new JTextField(); pnlForm.add(txtId);
        pnlForm.add(new JLabel("Nama Barang:")); txtNama = new JTextField(); pnlForm.add(txtNama);
        pnlForm.add(new JLabel("Kota Tujuan:")); txtTujuan = new JTextField(); pnlForm.add(txtTujuan);
        pnlForm.add(new JLabel("Berat (Kg):")); txtBerat = new JTextField(); pnlForm.add(txtBerat);
        pnlForm.add(new JLabel("Tingkat Prioritas:")); 
        cbPrioritas = new JComboBox<>(new String[]{"1 - Regular", "2 - High Priority", "3 - Critical"});
        pnlForm.add(cbPrioritas);

        JButton btnAdd = new JButton("Daftarkan Kargo");
        btnAdd.setBackground(new Color(46, 204, 113)); btnAdd.setForeground(Color.WHITE);
        pnlForm.add(btnAdd);

        // Panel Operasi & Fitur Utama (Bawah Kiri)
        JPanel pnlOps = new JPanel(new GridLayout(2, 1, 10, 10));
        pnlOps.setBorder(BorderFactory.createTitledBorder("Kontrol Logistik Pesawat"));
        JButton btnLoad = new JButton("Load Kargo Terpenting ke Pesawat (Dequeue)");
        btnLoad.setBackground(new Color(52, 152, 219)); btnLoad.setForeground(Color.WHITE);
        JButton btnUndo = new JButton("Undo Loading Terakhir (Stack Pop)");
        btnUndo.setBackground(new Color(231, 76, 60)); btnUndo.setForeground(Color.WHITE);
        pnlOps.add(btnLoad); pnlOps.add(btnUndo);

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.add(pnlForm, BorderLayout.NORTH);
        leftPanel.add(pnlOps, BorderLayout.CENTER);

        // Panel Tabel & Pencarian (Kanan)
        JPanel pnlTableSearch = new JPanel(new BorderLayout(10, 10));
        pnlTableSearch.setBorder(BorderFactory.createTitledBorder("Manifes Antrian Kargo Terminal"));

        JPanel pnlSearchSort = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlSearchSort.add(new JLabel("Cari ID:")); txtCari = new JTextField(10); pnlSearchSort.add(txtCari);
        JButton btnCari = new JButton("Cari (Linear)"); pnlSearchSort.add(btnCari);
        
        pnlSearchSort.add(new JLabel(" Urutkan Berat:"));
        cbSort = new JComboBox<>(new String[]{"None", "Berat (Ascending)", "Berat (Descending)"});
        pnlSearchSort.add(cbSort);
        JButton btnSort = new JButton("Sort"); pnlSearchSort.add(btnSort);

        String[] header = {"ID Kargo", "Nama Barang", "Tujuan", "Prioritas", "Berat (Kg)"};
        tableModel = new DefaultTableModel(header, 0);
        tabelKargo = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelKargo);

        pnlTableSearch.add(pnlSearchSort, BorderLayout.NORTH);
        pnlTableSearch.add(scrollPane, BorderLayout.CENTER);

        // Status Bar (Paling Bawah)
        JPanel statusBar = new JPanel(new GridLayout(1, 2));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        lblTotal = new JLabel(" Total Kargo di Gudang: 0 kargo");
        lblStack = new JLabel("Kargo Ter-load di Pesawat: 0", SwingConstants.RIGHT);
        statusBar.add(lblTotal); statusBar.add(lblStack);

        // Main Layout Integration
        setLayout(new BorderLayout(15, 15));
        add(leftPanel, BorderLayout.WEST);
        add(pnlTableSearch, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // --- LOGIKA EVENT HANDLING (KONTROLER) ---
        btnAdd.addActionListener(e -> {
            try {
                String id = txtId.getText();
                String nama = txtNama.getText();
                String tujuan = txtTujuan.getText();
                double berat = Double.parseDouble(txtBerat.getText());
                int prio = cbPrioritas.getSelectedIndex() + 1;

                Kargo k = new Kargo(id, nama, tujuan, prio, berat);
                antrianKargo.insert(k);
                refreshTable();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Validasi Gagal! Periksa inputan angka berat kargo.", "Error Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLoad.addActionListener(e -> {
            if (antrianKargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Antrian gudang kosong!");
                return;
            }
            Kargo loaded = antrianKargo.removeMax();
            logHistory.push(loaded);
            JOptionPane.showMessageDialog(this, "BERHASIL DI-LOAD:\n" + loaded, "Loading Selesai", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        });

        btnUndo.addActionListener(e -> {
            if (logHistory.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tidak ada riwayat kargo pesawat untuk dibatalkan!");
                return;
            }
            Kargo undone = logHistory.pop();
            antrianKargo.insert(undone);
            JOptionPane.showMessageDialog(this, "UNDO BERHASIL:\nKargo " + undone.getIdKargo() + " dikembalikan ke gudang.", "Undo Sukses", JOptionPane.WARNING_MESSAGE);
            refreshTable();
        });

        btnCari.addActionListener(e -> {
            String target = txtCari.getText().trim();
            if (target.isEmpty()) {
                refreshTable();
                return;
            }
            int indexKetemu = -1;
            // IMPLEMENTASI LINEAR SEARCH
            for (int i = 0; i < antrianKargo.getSize(); i++) {
                if (antrianKargo.getElement(i).getIdKargo().equalsIgnoreCase(target)) {
                    indexKetemu = i;
                    break;
                }
            }
            if (indexKetemu != -1) {
                Kargo found = antrianKargo.getElement(indexKetemu);
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{found.getIdKargo(), found.getNamaBarang(), found.getTujuan(), found.getPrioritas(), found.getBerat()});
                JOptionPane.showMessageDialog(this, "Kargo Ditemukan di Manifes Gudang!\n" + found);
            } else {
                JOptionPane.showMessageDialog(this, "Kargo dengan ID tersebut tidak ditemukan!", "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnSort.addActionListener(e -> {
            int tipeSort = cbSort.getSelectedIndex();
            if (tipeSort == 0 || antrianKargo.isEmpty()) return;

            // Copy data heap ke array temporer untuk melakukan pengurutan visual tanpa merusak struktur heap utama
            int size = antrianKargo.getSize();
            Kargo[] arrTemp = new Kargo[size];
            for (int i = 0; i < size; i++) {
                arrTemp[i] = antrianKargo.getElement(i);
            }

            // IMPLEMENTASI SELECTION SORT BERDASARKAN BERAT KARGO
            for (int i = 0; i < size - 1; i++) {
                int targetIndex = i;
                for (int j = i + 1; j < size; j++) {
                    if (tipeSort == 1) { // Ascending
                        if (arrTemp[j].getBerat() < arrTemp[targetIndex].getBerat()) targetIndex = j;
                    } else { // Descending
                        if (arrTemp[j].getBerat() > arrTemp[targetIndex].getBerat()) targetIndex = j;
                    }
                }
                Kargo temp = arrTemp[i];
                arrTemp[i] = arrTemp[targetIndex];
                arrTemp[targetIndex] = temp;
            }

            // Render array hasil sort ke tabel
            tableModel.setRowCount(0);
            for (Kargo k : arrTemp) {
                tableModel.addRow(new Object[]{k.getIdKargo(), k.getNamaBarang(), k.getTujuan(), k.getPrioritas(), k.getBerat()});
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < antrianKargo.getSize(); i++) {
            Kargo k = antrianKargo.getElement(i);
            tableModel.addRow(new Object[]{k.getIdKargo(), k.getNamaBarang(), k.getTujuan(), k.getPrioritas(), k.getBerat()});
        }
        lblTotal.setText(" Total Kargo di Gudang: " + antrianKargo.getSize() + " kargo");
        lblStack.setText("Kargo Ter-load di Pesawat: " + logHistory.getSize() + "  ");
    }

    private void clearForm() {
        txtId.setText(""); txtNama.setText(""); txtTujuan.setText(""); txtBerat.setText("");
        cbPrioritas.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CargoFlowGUI().setVisible(true));
    }
}
