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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class WisefiApInclude {
    private String ip;
    private String mac;
    private String versao;
    private Integer escolha;
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

    public static String getModelo(final int escolha) {
        switch (escolha) {
            case 1:
                return "AP310";
            case 2:
                return "AP360";
            case 3:
                return "AP 1210 AC";
            case 4:
                return "AP 1250 AC Max";
            case 5:
                return "AP 1350 AC";
            case 6:
                return "AP 1350 S";
            case 7:
                return "AP 1750 AC";
            case 8:
                return "BSPRO360";
            case 9:
                return "BSPRO1350";
            case 10:
                return "BSPRO1350-S";
            case 11:
                return "AP 1250 AC Outdoor";
            default:
                return null;
        }
    }

    public static void mostrarCriador() {
        String nomeCriador = "Faber Bernardo Junior";
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

            String mensagem = "O criador deste codigo e: " + nomeCriador + "\n\n" +
                    "Voce pode encontrar o codigo-fonte no GitHub:";

            JOptionPane.showMessageDialog(
                    null, mensagem, "Criador e Link do GitHub", JOptionPane.INFORMATION_MESSAGE, qrCodeIcon);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final WisefiApInclude app = new WisefiApInclude("-", 443);
        Object[] options = { "Avancar", "Autor", "Cancelar" };
        final String[] modelos = {
                "AP310",
                "AP360",
                "AP1210AC",
                "AP1250ACMAX",
                "AP1350AC",
                "AP1350AC-S",
                "AP1750AC",
                "BSPRO360",
                "BSPRO1350",
                "BSPRO1350-S",
                "AP1250ACOUTDOOR"
        };

        boolean condition = false;
        do {
            int result = JOptionPane.showOptionDialog(null, "Bem Vindo ao DB_Includer!", null,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (result) {
                case 0:
                    condition = true;
                    break;
                case 1:
                    mostrarCriador();

                    break;
                default:
                    JOptionPane.showMessageDialog(null,
                            "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                    break;
            }
        } while (!condition);

        final Scanner keyboard = new Scanner(System.in); // Cria um objeto Scanner para entrada do teclado.
        do {
            // Solicita ao usuário que insira o IP do AP
            app.setIp(JOptionPane.showInputDialog("Digite o IP do AP:"));
            if (app.getIp() == null) {
                JOptionPane.showMessageDialog(null, "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            if (!isValidIPv4Address(app.getIp())) {
                JOptionPane.showMessageDialog(null,
                        "Entrada invalida. Por favor, insira um ip valido 0-255", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!isValidIPv4Address(app.ip));

        do {
            // Solicita ao usuário que insira o MAC do AP no formato correto
            app.setMac(JOptionPane.showInputDialog("Digite o MAC do AP (formato aa:bb:cc:dd:ee:ff):"));
            if (app.getMac() == null) {
                JOptionPane.showMessageDialog(null, "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            if (!isValidMACAddress(app.getMac())) {
                JOptionPane.showMessageDialog(null,
                        "Entrada invalida. Por favor, insira um mac valido {aa:bb:cc:dd:ee:ff}", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!isValidMACAddress(app.getMac()));

        do {
            // Solicita ao usuário que insira a versão do AP
            app.setVersao(JOptionPane.showInputDialog("Qual e a versao do AP? (v2.10.15):"));
            if (app.getVersao() == null) {
                JOptionPane.showMessageDialog(null, "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            if (!isValidVersion(app.getVersao())) {
                JOptionPane.showMessageDialog(null, "Entrada invalida. Por favor, insira um modelo valido {v2.10.15}",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!isValidVersion(app.getVersao()));

        final StringBuilder message = new StringBuilder("Qual e o modelo do AP?\n");

        for (int i = 0; i < modelos.length; i++) {
            message.append(i + 1).append(". ").append(modelos[i]).append("\n");
        }

        do {
            // Solicita ao usuário que escolha um modelo de AP a partir de uma lista
            // numerada
            final String input = JOptionPane.showInputDialog(null, message.toString() +
                    "\nPor favor, insira o numero correspondente ao modelo desejado:");

            if (input == null) {
                JOptionPane.showMessageDialog(null, "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            try {
                app.setEscolha(Integer.parseInt(input));
            } catch (final NumberFormatException e) {
                app.setEscolha(0);
                ;
            }
            if (app.getEscolha() < 1 || app.getEscolha() > modelos.length) {
                JOptionPane.showMessageDialog(null, "Entrada invalida. Por favor, insira um numero de 1 a 10.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (app.getEscolha() < 1 || app.getEscolha() > modelos.length);

        keyboard.close(); // Fecha o Scanner.
        setDb(app);
    }

    private static void setDb(final WisefiApInclude app) {
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
            pstmt.setString(7, getModelo(app.getEscolha()));

            pstmt.executeUpdate();

            // Fazer o commit
            conn.commit();
            System.out.println("ID inserido: " + app.getId());
            System.out.println("IP: " + app.getIp());
            System.out.println("Port: " + app.getPort());
            System.out.println("MAC: " + app.getMac());
            System.out.println("Versao: " + app.getVersao());
            System.out.println("Produto: " + getModelo(app.getEscolha()));

            System.out.println("Dados inseridos com sucesso!");

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

    public Integer getEscolha() {
        return escolha;
    }

    public void setEscolha(final Integer escolha) {
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
