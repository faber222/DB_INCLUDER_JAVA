package engtelecom.db;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class CustomOptionPane {

    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType,
            ImageIcon icon) {
        // Cria o JOptionPane com o conteúdo e o ícone desejado
        JOptionPane optionPane = new JOptionPane(message, messageType, JOptionPane.DEFAULT_OPTION);

        // Cria um JDialog a partir do JOptionPane
        JDialog dialog = optionPane.createDialog(parentComponent, title);

        // Define o ícone da janela (ícone no canto superior esquerdo)
        dialog.setIconImage(icon.getImage());

        // Exibe o diálogo
        dialog.setVisible(true);
    }

    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType,
            int messageType, ImageIcon icon) {
        // Cria o JOptionPane com o conteúdo, tipo de mensagem e ícone desejado
        JOptionPane optionPane = new JOptionPane(message, messageType, optionType);

        // Cria um JDialog a partir do JOptionPane
        JDialog dialog = optionPane.createDialog(parentComponent, title);

        // Define o ícone da janela (ícone no canto superior esquerdo)
        dialog.setIconImage(icon.getImage());

        // Exibe o diálogo
        dialog.setVisible(true);

        // Retorna a opção selecionada (YES_OPTION, NO_OPTION, etc.)
        Object selectedValue = optionPane.getValue();
        if (selectedValue instanceof Integer) {
            return (Integer) selectedValue;
        }
        return JOptionPane.CLOSED_OPTION;
    }
}
