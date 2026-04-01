package projet.view;

import projet.model.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class DashboardServeuseFrame extends JFrame {
    public DashboardServeuseFrame(Utilisateur user) {
        super("Espace Serveuse - " + user.getNomComplet());
        initUI(user);
    }

    private void initUI(Utilisateur user) {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        String[] btns = {"Parcourir le Menu", "Passer une Commande",
                         "Commandes en cours", "Commandes reçues",
                         "Générer une Facture", "Se déconnecter"};
        for (String t : btns) {
            JButton b = new JButton(t);
            b.setFont(new Font("Arial", Font.BOLD, 13));
            if ("Se déconnecter".equals(t)) {
                b.setBackground(new Color(200, 50, 50));
                b.setForeground(Color.WHITE);
                b.addActionListener(e -> { dispose(); new LoginFrame(); });
            }
            panel.add(b);
        }
        add(panel);
        setVisible(true);
    }
}
