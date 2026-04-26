import view.FenetreConnexion;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Rendre l'interface plus belle avec le look natif du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lancer l'interface dans le thread Swing
        SwingUtilities.invokeLater(() -> new FenetreConnexion());
    }
}