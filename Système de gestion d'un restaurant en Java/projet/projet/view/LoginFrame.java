package view;

import controller.AuthController;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de connexion - première interface du projet.
 */
public class LoginFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnConnexion;
    private JLabel         lblErreur;
    private AuthController authCtrl = new AuthController();

    public LoginFrame() {
        super("Restaurant - Connexion");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal avec fond
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Titre
        JLabel lblTitre = new JLabel("🍽 Gestion Restaurant", JLabel.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitre.setForeground(new Color(180, 70, 30));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitre, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nom d'utilisateur
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Utilisateur :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtUsername = new JTextField(15);
        formPanel.add(txtUsername, gbc);

        // Mot de passe
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPassword = new JPasswordField(15);
        formPanel.add(txtPassword, gbc);

        // Message d'erreur
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        lblErreur = new JLabel(" ");
        lblErreur.setForeground(Color.RED);
        lblErreur.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(lblErreur, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Bouton connexion
        btnConnexion = new JButton("Se connecter");
        btnConnexion.setBackground(new Color(180, 70, 30));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFont(new Font("Arial", Font.BOLD, 13));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConnexion.addActionListener(e -> handleLogin());
        txtPassword.addActionListener(e -> handleLogin());
        mainPanel.add(btnConnexion, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblErreur.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (authCtrl.login(username, password)) {
            Utilisateur user = authCtrl.getUtilisateurConnecte();
            lblErreur.setText("");
            this.dispose();
            ouvrirDashboard(user);
        } else {
            lblErreur.setText("Identifiants incorrects. Réessayez.");
            txtPassword.setText("");
        }
    }

    private void ouvrirDashboard(Utilisateur user) {
        switch (user.getRole()) {
            case "CLIENT"    -> new DashboardClientFrame(user);
            case "SERVEUSE"  -> new DashboardServeuseFrame(user);
            case "CUISINIER" -> new DashboardCuisinierFrame(user);
        }
    }

    public static void main(String[] args) {
        // Thème Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus non disponible, utilisation du thème par défaut.");
        }
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
