package view;

import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class DashboardClientFrame extends JFrame {
    private Utilisateur user;

    public DashboardClientFrame(Utilisateur user) {
        super("Espace Client - " + user.getNomComplet());
        this.user = user;
        initUI();
    }

    private void initUI() {
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblBienvenue = new JLabel("Bienvenue, " + user.getNomComplet() + " !", JLabel.CENTER);
        lblBienvenue.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblBienvenue, BorderLayout.NORTH);

        // Boutons du client
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        JButton btnMenu    = new JButton("Parcourir le Menu");
        JButton btnCommande= new JButton("Passer une Commande");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 13));
        btnCommande.setFont(new Font("Arial", Font.BOLD, 13));
        btnMenu.addActionListener(e -> { new GestionMenuFrame(); });
        btnPanel.add(btnMenu);
        btnPanel.add(btnCommande);
        panel.add(btnPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }
}
