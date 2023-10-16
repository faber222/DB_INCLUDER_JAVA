package engtelecom.db;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
// import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class WisefiApInclude {
    private String ip;
    private String mac;
    private String versao;
    private String escolha;
    private Integer id;
    private String uptime;
    private Integer port;

    public WisefiApInclude(final String uptime, final Integer port) {
        this.port = port;
        this.uptime = uptime;
    }

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

    public static void mostrarCriador() {
        String nomeCriador = "Faber";
        String githubLink = "https://github.com/faber222"; // Substitua com o link do GitHub do criador

        // Configura as opções de codificação do QR code
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.MARGIN, 2);

        // Gera o QR code
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(githubLink, BarcodeFormat.QR_CODE, 100, 100, hints);

            // Cria uma ImageIcon a partir do QR code
            BufferedImage bufferedImage = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            ImageIcon qrCodeIcon = new ImageIcon(bufferedImage);

            String mensagem = "O criador deste codigo: " + nomeCriador + "\n\n" +
                    "Voce pode encontra-lo no GitHub:";

            JOptionPane.showMessageDialog(
                    null, mensagem, "Criador faber222 e Link do GitHub", JOptionPane.INFORMATION_MESSAGE, qrCodeIcon);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static void saida(ImageIcon saidaIcon) {
        JOptionPane.showMessageDialog(null,
                "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                null, JOptionPane.WARNING_MESSAGE, saidaIcon);
    }

    public static void main(final String[] args) {
        final WisefiApInclude app = new WisefiApInclude("-", 443);
        Object[] options = { "Avancar", "Autor", "Cancelar" };
        ClassLoader classLoader = WisefiApInclude.class.getClassLoader();
        ImageIcon apIcon = new ImageIcon(classLoader.getResource("ap.png"));
        ImageIcon dbIcon = new ImageIcon(classLoader.getResource("db_includer.png"));
        ImageIcon equipamentoIcon = new ImageIcon(classLoader.getResource("equipamento.png"));
        ImageIcon ipIcon = new ImageIcon(classLoader.getResource("ip.png"));
        ImageIcon macIcon = new ImageIcon(classLoader.getResource("mac.png"));
        ImageIcon saidaIcon = new ImageIcon(classLoader.getResource("saida.png"));
        ImageIcon erroIcon = new ImageIcon(classLoader.getResource("erro.png"));
        final String[] modelos = {
                "AP310",
                "AP360",
                "AP 1210 AC",
                "AP 1250 AC Max",
                "AP 1350 AC",
                "AP 1350 AC-S",
                "AP 1750 AC",
                "BSPRO360",
                "BSPRO1350",
                "BSPRO1350-S",
                "AP 1250 AC Outdoor"
        };

        boolean condition = false;
        do {
            int result = JOptionPane.showOptionDialog(null, "Bem Vindo ao DB_Includer!", "faber222",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, dbIcon, options, options[0]);
            switch (result) {
                case 0:
                    condition = true;
                    break;
                case 1:
                    mostrarCriador();
                    break;
                default:
                    saida(saidaIcon);
                    System.exit(0);
                    break;
            }
        } while (!condition);

        do {
            // Solicita ao usuário que insira o IP do AP
            app.setIp(JOptionPane.showInputDialog("Digite o IP do AP:"));
            if (app.getIp() == null) {
                saida(saidaIcon);
                System.exit(0);
            }
            if (!isValidIPv4Address(app.getIp())) {
                JOptionPane.showMessageDialog(null,
                        "Entrada invalida. Por favor, insira um ip valido 0-255", "Erro",
                        JOptionPane.ERROR_MESSAGE, erroIcon);
            }
        } while (!isValidIPv4Address(app.ip));

        do {
            // Solicita ao usuário que insira o MAC do AP no formato correto
            app.setMac(JOptionPane.showInputDialog("Digite o MAC do AP (formato aa:bb:cc:dd:ee:ff):"));
            if (app.getMac() == null) {
                saida(saidaIcon);
                System.exit(0);
            }
            if (!isValidMACAddress(app.getMac())) {
                JOptionPane.showMessageDialog(null,
                        "Entrada invalida. Por favor, insira um mac valido {aa:bb:cc:dd:ee:ff}", "Erro",
                        JOptionPane.ERROR_MESSAGE, erroIcon);
            }
        } while (!isValidMACAddress(app.getMac()));

        do {
            // Solicita ao usuário que insira a versão do AP
            app.setVersao(JOptionPane.showInputDialog("Qual e a versao do AP? (v2.10.15):"));
            if (app.getVersao() == null) {
                saida(saidaIcon);
                System.exit(0);
            }
            if (!isValidVersion(app.getVersao())) {
                JOptionPane.showMessageDialog(null, "Entrada invalida. Por favor, insira um modelo valido {v2.10.15}",
                        "Erro", JOptionPane.ERROR_MESSAGE, erroIcon);
            }
        } while (!isValidVersion(app.getVersao()));

        Object[] aps = modelos;
        String input = new String();
        do {
            // Solicita ao usuário que escolha um modelo de AP a partir de uma lista
            input = (String) JOptionPane.showInputDialog(null, "Por favor, escolha o modelo do ap:", "faber222",
                    JOptionPane.QUESTION_MESSAGE, apIcon, aps, aps[0]);
            if (input == null) {
                saida(saidaIcon);
                System.exit(0);
            }
            try {
                app.setEscolha(input);
            } catch (final NumberFormatException e) {
                app.setEscolha(null);
            }
        } while (app.getEscolha() == null);

        if (setDb(app)) {
            JOptionPane.showInternalMessageDialog(null, "Equipamento adicionado com sucesso!",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível adicionar o equipamento!",
                    "Erro", JOptionPane.ERROR_MESSAGE, erroIcon);
        }
        System.exit(0);
    }

    private static boolean setDb(final WisefiApInclude app) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        List<Integer> existingIds = new ArrayList<>();

        final Random random = new Random();

        app.setId(random.nextInt(200));
        try {
            // Registrar o driver JDBC para SQLite
            Class.forName("org.sqlite.JDBC");

            // Abrir a conexão com o banco de dados SQLite
            conn = DriverManager.getConnection("jdbc:sqlite:C:/intelbras/WiseFi/web/wisefi/db.sqlite3");

            // Desabilitar o autocommit
            conn.setAutoCommit(false);

            existingIds = getExistingIdsFromDatabase();

            Collections.sort(existingIds);

            for (final Integer elemento : existingIds) {
                int x = app.getId();
                if (x == elemento) {
                    app.setId(x++);
                }
            }

            final String sql = "INSERT INTO discovery_discovery (id, ip, port, mac, uptime, version, produto) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, app.getId());
            pstmt.setString(2, app.getIp());
            pstmt.setInt(3, app.getPort());
            pstmt.setString(4, app.getMac());
            pstmt.setString(5, app.getUptime());
            pstmt.setString(6, app.getVersao());
            pstmt.setString(7, app.getEscolha());

            pstmt.executeUpdate();

            // Fazer o commit
            conn.commit();
            System.out.println("ID inserido: " + app.getId());
            System.out.println("IP: " + app.getIp());
            System.out.println("Port: " + app.getPort());
            System.out.println("MAC: " + app.getMac());
            System.out.println("Versao: " + app.getVersao());
            System.out.println("Produto: " + app.getEscolha());

            System.out.println("Dados inseridos com sucesso!");
            return true;

        } catch (final Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    // Reverter a transação em caso de erro
                    conn.rollback();
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (final SQLException se) {
                se.printStackTrace();
            }
        }
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

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (final SQLException se) {
                se.printStackTrace();
            }
        }

        return existingIds;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(final String mac) {
        this.mac = mac;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(final String versao) {
        this.versao = versao;
    }

    public String getEscolha() {
        return escolha;
    }

    public void setEscolha(final String escolha) {
        this.escolha = escolha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(final String uptime) {
        this.uptime = uptime;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }
}
