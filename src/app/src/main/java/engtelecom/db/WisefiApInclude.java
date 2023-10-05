package engtelecom.db; // Define o pacote onde a classe App está localizada.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane; // Importa a classe JOptionPane para interações com o usuário.

public class WisefiApInclude {
    // Métodos para validar entradas com expressões regulares.
    public static boolean isValidMACAddress(String macAddress) {
        // Expressão regular para validar um endereço MAC
        String regex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        // Compila a expressão regular
        Pattern pattern = Pattern.compile(regex);
        // Compara a string de entrada com a expressão regular
        Matcher matcher = pattern.matcher(macAddress);
        return matcher.matches();
    }

    public static boolean isValidIPv4Address(String ipAddress) {
        // Expressão regular para validar um endereço IPv4
        String regex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        // Compila a expressão regular
        Pattern pattern = Pattern.compile(regex);
        // Compara a string de entrada com a expressão regular
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static boolean isValidVersion(String version) {
        // Expressão regular para validar a versão
        String regex = "^v\\d+\\.\\d+\\.\\d+$";
        // Compila a expressão regular
        Pattern pattern = Pattern.compile(regex);
        // Compara a string de entrada com a expressão regular
        Matcher matcher = pattern.matcher(version);
        return matcher.matches();
    }

    // Método para retornar o modelo com base na escolha do usuário.
    public static String getModelo(int escolha) {
        switch (escolha) {
            case 1:
                return "AP310";
            case 2:
                return "AP360";
            case 3:
                return "AP1250AC-MAX";
            case 4:
                return "AP1350AC";
            case 5:
                return "AP1350-S";
            case 6:
                return "AP1750AC";
            case 7:
                return "BSPRO360";
            case 8:
                return "BPPRO1350AC";
            case 9:
                return "BSPRO1350-S";
            case 10:
                return "AP1250AC-OUTDOOR";
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in); // Cria um objeto Scanner para entrada do teclado.
        String ip, mac, versao;
        int escolha = 0;

        String[] modelos = {
                "AP310",
                "AP360",
                "AP1250AC-MAX",
                "AP1350AC",
                "AP1350-S",
                "AP1750AC",
                "BSPRO360",
                "BPPRO1350AC",
                "BSPRO1350-S",
                "AP1250AC-OUTDOOR"
        };

        do {
            // Solicita ao usuário que insira o IP do AP
            ip = JOptionPane.showInputDialog("Digite o IP do AP:");
            if (ip == null) {
                JOptionPane.showMessageDialog(null, "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            if (!isValidIPv4Address(ip)) {
                JOptionPane.showMessageDialog(null,
                        "Entrada invalida. Por favor, insira um ip valido 0-255", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!isValidIPv4Address(ip));

        do {
            // Solicita ao usuário que insira o MAC do AP no formato correto
            mac = JOptionPane.showInputDialog("Digite o MAC do AP (formato aa:bb:cc:dd:ee:ff):");
            if (mac == null) {
                JOptionPane.showMessageDialog(null, "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            if (!isValidMACAddress(mac)) {
                JOptionPane.showMessageDialog(null,
                        "Entrada invalida. Por favor, insira um mac valido {aa:bb:cc:dd:ee:ff}", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!isValidMACAddress(mac));

        do {
            // Solicita ao usuário que insira a versão do AP
            versao = JOptionPane.showInputDialog("Qual e a versao do AP? (v2.10.15):");
            if (versao == null) {
                JOptionPane.showMessageDialog(null, "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            if (!isValidVersion(versao)) {
                JOptionPane.showMessageDialog(null, "Entrada invalida. Por favor, insira um modelo valido {v2.10.15}",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (!isValidVersion(versao));

        StringBuilder message = new StringBuilder("Qual e o modelo do AP?\n");

        for (int i = 0; i < modelos.length; i++) {
            message.append(i + 1).append(". ").append(modelos[i]).append("\n");
        }

        do {
            // Solicita ao usuário que escolha um modelo de AP a partir de uma lista
            // numerada
            String input = JOptionPane.showInputDialog(null, message.toString() +
                    "\nPor favor, insira o numero correspondente ao modelo desejado:");

            if (input == null) {
                JOptionPane.showMessageDialog(null, "Voce pressionou o botao 'Cancelar'. O programa sera encerrado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            try {
                escolha = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                escolha = 0;
            }
            if (escolha < 1 || escolha > 10) {
                JOptionPane.showMessageDialog(null, "Entrada invalida. Por favor, insira um numero de 1 a 10.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } while (escolha < 1 || escolha > 10);

        keyboard.close(); // Fecha o Scanner.

        int id = 17;
        int port = 443;
        String uptime = "-";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Registrar o driver JDBC para SQLite
            Class.forName("org.sqlite.JDBC");

            // Abrir a conexão com o banco de dados SQLite
            conn = DriverManager.getConnection("jdbc:sqlite:C:/intelbras/WiseFi/web/wisefi/db.sqlite3");

            // Desabilitar o autocommit
            conn.setAutoCommit(false);

            String sql = "INSERT INTO discovery_discovery (id, ip, port, mac, uptime, version, produto) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, ip);
            pstmt.setInt(3, port);
            pstmt.setString(4, mac);
            pstmt.setString(5, uptime);
            pstmt.setString(6, versao);
            pstmt.setString(7, getModelo(escolha));

            pstmt.executeUpdate();

            // Fazer o commit
            conn.commit();
            System.out.println("ID inserido: " + id);
            System.out.println("IP: " + ip);
            System.out.println("Port: " + port);
            System.out.println("MAC: " + mac);
            System.out.println("Versão: " + versao);
            System.out.println("Produto: " + getModelo(escolha));

            System.out.println("Dados inseridos com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    // Reverter a transação em caso de erro
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
