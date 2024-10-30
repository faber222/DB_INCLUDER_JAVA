package engtelecom.db;

import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author faber222
 */
public class DbIncluder extends javax.swing.JFrame {

    public static boolean isValidMACAddress(final String macAddress) {
        // Expressão regular para validar um endereço MAC
        final String regex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        // Compila a expressão regular
        final Pattern pattern = Pattern.compile(regex);
        // Compara a string de entrada com a expressão regular
        final Matcher matcher = pattern.matcher(macAddress);
        return matcher.matches();
    }

    public static boolean isValidIPv4Address(final String ipAddress) {
        // Expressão regular para validar um endereço IPv4
        final String regex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        // Compila a expressão regular
        final Pattern pattern = Pattern.compile(regex);
        // Compara a string de entrada com a expressão regular
        final Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static boolean isValidVersion(final String version) {
        // Expressão regular para validar a versão
        final String regex = "^v\\d+\\.\\d+\\.\\d+$";
        // Compila a expressão regular
        final Pattern pattern = Pattern.compile(regex);
        // Compara a string de entrada com a expressão regular
        final Matcher matcher = pattern.matcher(version);
        return matcher.matches();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DbIncluder.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>

        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new DbIncluder().setVisible(true);
        });
    }

    private static List<Integer> getExistingIdsFromDatabase() {
        final List<Integer> existingIds = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        try {
            // Registrar o driver JDBC para SQLite
            Class.forName("org.sqlite.JDBC");

            // Abrir a conexão com o banco de dados SQLite
            conn = DriverManager.getConnection("jdbc:sqlite:C:/intelbras/WiseFi/web/wisefi/db.sqlite3");

            final String sql = "SELECT id FROM discovery_discovery";
            pstmt = conn.prepareStatement(sql);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                existingIds.add(resultSet.getInt("id"));
            }

        } catch (final ClassNotFoundException | SQLException e) {
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (final SQLException se) {
            }
        }

        return existingIds;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAtualizar;

    private javax.swing.JButton jButtonCancel;

    private javax.swing.JButton jButtonEnviar;

    private javax.swing.JButton jButtonInserir;

    private javax.swing.JButton jButtonRemoverSelecionado;

    private javax.swing.JButton jButtonRemoverTodos;

    private javax.swing.JComboBox<String> jComboBoxModelo;

    private javax.swing.JFormattedTextField jFormattedMac;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextFieldIp;
    private javax.swing.JTextField jTextFieldVersao;
    private final ImageIcon mainIcon;

    // End of variables declaration//GEN-END:variables
    /**
     * Creates new form dbIncluder
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DbIncluder() {
        initComponents();

        final DefaultTableModel modelo = (DefaultTableModel) jTable.getModel();
        jTable.setRowSorter(new TableRowSorter(modelo));
        final ClassLoader classLoader = DbIncluder.class.getClassLoader();
        this.mainIcon = new ImageIcon(classLoader.getResource("includer.png"));
        this.setIconImage(this.mainIcon.getImage());
    }

    private boolean setDb(final JTable table) {
        List<Integer> existingIds = new ArrayList<>();
        final Random random = new Random();
        Integer id;

        try {
            // Registrar o driver JDBC para SQLite
            Class.forName("org.sqlite.JDBC");

            // Abrir a conexão com o banco de dados SQLite
            try (Connection conn = DriverManager
                    .getConnection("jdbc:sqlite:C:/intelbras/WiseFi/web/wisefi/db.sqlite3")) {
                // Desabilitar o autocommit
                conn.setAutoCommit(false);

                existingIds = getExistingIdsFromDatabase();
                Collections.sort(existingIds);

                // Iterar sobre as linhas da JTable
                for (int row = 0; row < table.getRowCount(); row++) {
                    // Gerar um ID único para cada linha
                    do {
                        id = random.nextInt(500);
                    } while (existingIds.contains(id)); // Garante que o ID é único

                    // Preparar a instrução SQL
                    final String sql = "INSERT INTO discovery_discovery (id, ip, port, mac, uptime, version, produto) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        // Obter os dados da tabela
                        final String ip = (String) table.getValueAt(row, 0); // Supondo que IP está na coluna 0
                        final int port = 443; // Supondo que Port está na coluna 1
                        final String mac = (String) table.getValueAt(row, 1); // Supondo que MAC está na coluna 1
                        final String uptime = "-"; // Supondo que Uptime está na coluna 3
                        final String versao = (String) table.getValueAt(row, 2); // Supondo que Versão está na coluna 2
                        final String produto = (String) table.getValueAt(row, 3); // Supondo que Produto está na coluna
                                                                                  // 3

                        // Configurar os parâmetros da consulta
                        pstmt.setInt(1, id);
                        pstmt.setString(2, ip);
                        pstmt.setInt(3, port);
                        pstmt.setString(4, mac);
                        pstmt.setString(5, uptime);
                        pstmt.setString(6, versao);
                        pstmt.setString(7, produto);

                        System.out.println("ID inserido: " + id);
                        System.out.println("IP: " + ip);
                        System.out.println("Port: " + port);
                        System.out.println("MAC: " + mac);
                        System.out.println("Versao: " + versao);
                        System.out.println("Produto: " + produto);

                        pstmt.executeUpdate();
                    }
                }

                // Fazer o commit
                conn.commit(); // Fazer o commit
                System.out.println("Todos os dados inseridos com sucesso!");
                CustomOptionPane.showMessageDialog(null, "Todos os dados inseridos com sucesso!", "",
                        JOptionPane.INFORMATION_MESSAGE, this.mainIcon);

                return true;
            }
        } catch (final HeadlessException | ClassNotFoundException | SQLException e) {
            // JOptionPane.showMessageDialog(null, "Não foi possível acessar o banco de
            // dados!");
            CustomOptionPane.showMessageDialog(null, "Não foi possível acessar o banco de dados!", "",
                    JOptionPane.WARNING_MESSAGE, this.mainIcon);
            return false; // Retorna falso em caso de erro
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({ "CallToPrintStackTrace" })
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldIp = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxModelo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jFormattedMac = new javax.swing.JFormattedTextField();
        jTextFieldVersao = new javax.swing.JTextField();
        jButtonInserir = new javax.swing.JButton();
        jButtonAtualizar = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonEnviar = new javax.swing.JButton();
        jButtonRemoverSelecionado = new javax.swing.JButton();
        jButtonRemoverTodos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DB-INCLUDER");
        setName("frame"); // NOI18N
        setResizable(false);

        // Adiciona o KeyListener à jTable
        jTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    jTable.clearSelection(); // Limpa a seleção da tabela
                    // Limpa os campos de entrada
                    jTextFieldIp.setText("");
                    jFormattedMac.setText("");
                    jTextFieldVersao.setText("v");
                    jComboBoxModelo.setSelectedIndex(0); // ou o índice padrão desejado
                }
            }
        });
        jTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "IP", "MAC", "VERSAO", "MODELO"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false
            };

            @SuppressWarnings("override")
            public boolean isCellEditable(final int rowIndex, final int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable.getTableHeader().setReorderingAllowed(false);

        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent evt) {
                jTableMouseClicked();
            }
        });
        jTable.addKeyListener(new java.awt.event.KeyAdapter() {
            @SuppressWarnings("override")
            public void keyReleased(final java.awt.event.KeyEvent evt) {
                jTableKeyReleased();
            }
        });
        jScrollPane1.setViewportView(jTable);
        if (jTable.getColumnModel().getColumnCount() > 0) {
            jTable.getColumnModel().getColumn(0).setResizable(false);
            jTable.getColumnModel().getColumn(1).setResizable(false);
            jTable.getColumnModel().getColumn(2).setResizable(false);
            jTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("sansserif", 1, 13))); // NOI18N

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        jLabel1.setText("Digite o IP do AP");

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        jLabel2.setText("Digite o MAC do AP");

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        jLabel3.setText("Versao do AP");

        jComboBoxModelo.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "AP310", "AP360", "AP 1210 AC", "AP 1250 AC Max", "AP 1350 AC", "AP 1350 AC-S",
                        "AP 1750 AC", "BSPRO360", "BSPRO1350", "BSPRO1350-S", "AP 1250 AC Outdoor" }));

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        jLabel4.setText("Modelo do ap");

        try {
            // Cria a máscara com o formato desejado
            final MaskFormatter formatter = new MaskFormatter("HH:HH:HH:HH:HH:HH");
            formatter.setPlaceholderCharacter('_'); // Define o caractere de preenchimento
            jFormattedMac.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
        } catch (final java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jTextFieldIp.setToolTipText("10.0.0.1");
        jFormattedMac.setToolTipText("aa:bb:cc:dd:ee:ff");

        jTextFieldVersao.setText("v");
        jTextFieldVersao.setToolTipText("v2.10.15");
        jTextFieldVersao.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextFieldVersao.setSelectionEnd(2);
        jTextFieldVersao.setSelectionStart(2);

        jButtonInserir.setText("Incluir");
        jButtonInserir.addActionListener((final java.awt.event.ActionEvent evt) -> {
            jButtonInserirActionPerformed();
        });

        jButtonAtualizar.setText("Atualizar");
        jButtonAtualizar.addActionListener((final java.awt.event.ActionEvent evt) -> {
            jButtonAtualizarActionPerformed();
        });

        final javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jTextFieldIp)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                125, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jFormattedMac)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jTextFieldVersao)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jComboBoxModelo, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jButtonInserir)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButtonAtualizar)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel3)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextFieldIp, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxModelo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jFormattedMac, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextFieldVersao, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonInserir)
                                        .addComponent(jButtonAtualizar))
                                .addContainerGap()));

        jButtonCancel.setText("Cancelar");
        jButtonCancel.addActionListener((final java.awt.event.ActionEvent evt) -> {
            jButtonCancelActionPerformed();
        });

        jButtonEnviar.setText("Enviar");
        jButtonEnviar.addActionListener((final java.awt.event.ActionEvent evt) -> {
            jButtonEnviarActionPerformed();
        });

        jButtonRemoverSelecionado.setText("Remover Selecionado");
        jButtonRemoverSelecionado.addActionListener((final java.awt.event.ActionEvent evt) -> {
            jButtonRemoverSelecionadoActionPerformed();
        });

        jButtonRemoverTodos.setText("Remover Todos");
        jButtonRemoverTodos.addActionListener((final java.awt.event.ActionEvent evt) -> {
            jButtonRemoverTodosActionPerformed();
        });

        final javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
                jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanelLayout.createSequentialGroup()
                                                .addGroup(jPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jScrollPane1))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout
                                                .createSequentialGroup()
                                                .addComponent(jButtonRemoverSelecionado)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButtonRemoverTodos)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonEnviar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButtonCancel)))
                                .addContainerGap()));
        jPanelLayout.setVerticalGroup(
                jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10,
                                        Short.MAX_VALUE)
                                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonEnviar)
                                        .addComponent(jButtonCancel)
                                        .addComponent(jButtonRemoverSelecionado)
                                        .addComponent(jButtonRemoverTodos))
                                .addContainerGap()));

        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
        setLocationRelativeTo(null);
    }

    private void jButtonCancelActionPerformed() {
        System.exit(0); // Fecha a aplicação
    }

    private void jButtonRemoverSelecionadoActionPerformed() {
        final DefaultTableModel dtmDispositivos = (DefaultTableModel) jTable.getModel();
        if (jTable.getSelectedRow() != -1) {
            final int confirm = JOptionPane.showConfirmDialog(null,
                    "Tem certeza que deseja remover?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dtmDispositivos.removeRow(jTable.getSelectedRow());
            }
        } else {
            // JOptionPane.showMessageDialog(null, "Selecione um produto para excluir!", "",
            // JOptionPane.INFORMATION_MESSAGE);
            CustomOptionPane.showMessageDialog(null, "Selecione um produto para excluir!", "Aviso",
                    JOptionPane.INFORMATION_MESSAGE, mainIcon);

        }
    }

    private void jButtonRemoverTodosActionPerformed() {
        final DefaultTableModel dtmDispositivos = (DefaultTableModel) jTable.getModel();
        // Verifica se há linhas na tabela
        if (dtmDispositivos.getRowCount() > 0) {
            final int confirm = CustomOptionPane.showConfirmDialog(null,
                    "Tem certeza que deseja remover todos os produtos?", "Confirmação",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, mainIcon);

            if (confirm == JOptionPane.YES_OPTION) {
                dtmDispositivos.setRowCount(0); // Remove todas as linhas da tabela
            }
        } else {
            CustomOptionPane.showMessageDialog(null, "A tabela está vazia!", "", JOptionPane.INFORMATION_MESSAGE,
                    mainIcon);
        }
    }

    private void jButtonAtualizarActionPerformed() {
        if (jTable.getSelectedRow() != -1) {
            if (isValidIPv4Address(jTextFieldIp.getText()) && isValidMACAddress(jFormattedMac.getText())
                    && isValidVersion(
                            jTextFieldVersao.getText())) {
                jTable.getModel().setValueAt(jTextFieldIp.getText(), jTable.getSelectedRow(), 0);
                jTable.getModel().setValueAt(jFormattedMac.getText(), jTable.getSelectedRow(), 1);
                jTable.getModel().setValueAt(jTextFieldVersao.getText(), jTable.getSelectedRow(), 2);
                jTable.getModel().setValueAt(jComboBoxModelo.getSelectedItem(), jTable.getSelectedRow(), 3);
            } else {
                CustomOptionPane.showMessageDialog(null, "Valores inválidos para IP, MAC ou Versão!", "",
                        JOptionPane.ERROR_MESSAGE,
                        mainIcon);
            }
        } else {
            CustomOptionPane.showMessageDialog(null, "Selecione um produto para atualizar!", "",
                    JOptionPane.INFORMATION_MESSAGE,
                    mainIcon);
        }
    }

    private void jTableMouseClicked() {
        if (jTable.getSelectedRow() != -1) {
            jTextFieldIp.setText(jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
            jFormattedMac.setText(jTable.getValueAt(jTable.getSelectedRow(), 1).toString());
            jTextFieldVersao.setText(jTable.getValueAt(jTable.getSelectedRow(), 2).toString());
            jComboBoxModelo.setSelectedItem(jTable.getValueAt(jTable.getSelectedRow(), 3).toString());
        }
    }

    private void jTableKeyReleased() {
        if (jTable.getSelectedRow() != -1) {
            jTextFieldIp.setText(jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
            jFormattedMac.setText(jTable.getValueAt(jTable.getSelectedRow(), 1).toString());
            jTextFieldVersao.setText(jTable.getValueAt(jTable.getSelectedRow(), 2).toString());
            jComboBoxModelo.setSelectedItem(jTable.getValueAt(jTable.getSelectedRow(), 3).toString());
        }
    }

    private void jButtonEnviarActionPerformed() {
        final DefaultTableModel dtmDispositivos = (DefaultTableModel) jTable.getModel();
        // Verifica se há linhas na tabela
        if (dtmDispositivos.getRowCount() > 0) {
            setDb(jTable);
        } else {
            CustomOptionPane.showMessageDialog(null, "Nenhum produto para cadastrar!", "",
                    JOptionPane.INFORMATION_MESSAGE,
                    mainIcon);
        }

    }

    private void jButtonInserirActionPerformed() {
        final DefaultTableModel dtmDispositivos = (DefaultTableModel) jTable.getModel();
        if (isValidIPv4Address(jTextFieldIp.getText()) && isValidMACAddress(jFormattedMac.getText()) && isValidVersion(
                jTextFieldVersao.getText())) {
            final Object[] dados = { jTextFieldIp.getText(), jFormattedMac.getText(), jTextFieldVersao.getText(),
                    jComboBoxModelo.getSelectedItem() };
            dtmDispositivos.addRow(dados);
        } else {
            CustomOptionPane.showMessageDialog(null, "Valores inválidos para IP, MAC ou Versão!", "",
                    JOptionPane.ERROR_MESSAGE,
                    mainIcon);
        }

    }

}
