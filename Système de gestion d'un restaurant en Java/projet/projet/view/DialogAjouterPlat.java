package view;

import controller.MenuController;
import model.Menu;
import model.Plat;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialogue modal pour ajouter ou modifier un plat.
 * Si platAModifier == null => mode AJOUT, sinon => mode MODIFICATION.
 */
public class DialogAjouterPlat extends JDialog {

    private JTextField    txtNom, txtDescription, txtPrix;
    private JComboBox<String> cbxMenu;
    private JCheckBox     chkDisponible;
    private boolean       confirme = false;
    private MenuController menuCtrl;
    private Plat          platAModifier;
    private List<Menu>    menus;

    public DialogAjouterPlat(JFrame parent, MenuController menuCtrl, Plat platAModifier) {
        super(parent, platAModifier == null ? "Ajouter un plat" : "Modifier le plat", true);
        this.menuCtrl      = menuCtrl;
        this.platAModifier = platAModifier;
        initUI();
        if (platAModifier != null) remplirChamps();
    }

    private void initUI() {
        setSize(420, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(5, 5, 5, 5);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Ligne 0 : Nom
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        form.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNom = new JTextField(15);
        form.add(txtNom, gbc);

        // Ligne 1 : Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        form.add(new JLabel("Description :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtDescription = new JTextField(15);
        form.add(txtDescription, gbc);

        // Ligne 2 : Prix
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        form.add(new JLabel("Prix (TND) :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPrix = new JTextField(15);
        form.add(txtPrix, gbc);

        // Ligne 3 : Menu
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        form.add(new JLabel("Menu :"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        cbxMenu = new JComboBox<>();
        menus = menuCtrl.getAllMenus();
        for (Menu m : menus) cbxMenu.addItem(m.getNom());
        form.add(cbxMenu, gbc);

        // Ligne 4 : Disponible
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        chkDisponible = new JCheckBox("Disponible", true);
        form.add(chkDisponible, gbc);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("Valider");
        JButton btnAnnuler = new JButton("Annuler");
        btnOk.setBackground(new Color(34, 139, 34));
        btnOk.setForeground(Color.WHITE);
        btnOk.setFocusPainted(false);
        btnOk.addActionListener(e -> valider());
        btnAnnuler.addActionListener(e -> dispose());
        btnPanel.add(btnOk);
        btnPanel.add(btnAnnuler);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void remplirChamps() {
        txtNom.setText(platAModifier.getNom());
        txtDescription.setText(platAModifier.getDescription());
        txtPrix.setText(String.valueOf(platAModifier.getPrix()));
        chkDisponible.setSelected(platAModifier.isDisponible());
        // Selectionner le menu correspondant
        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).getId() == platAModifier.getIdMenu()) {
                cbxMenu.setSelectedIndex(i);
                break;
            }
        }
    }

    private void valider() {
        String nom  = txtNom.getText().trim();
        String desc = txtDescription.getText().trim();
        String prixStr = txtPrix.getText().trim();
        if (nom.isEmpty() || prixStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom et le prix sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double prix;
        try {
            prix = Double.parseDouble(prixStr);
            if (prix < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prix invalide. Entrez un nombre positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idMenu = menus.isEmpty() ? 0 : menus.get(cbxMenu.getSelectedIndex()).getId();
        boolean dispo = chkDisponible.isSelected();

        boolean ok;
        if (platAModifier == null) {
            // AJOUT
            Plat p = new Plat(0, nom, desc, prix, dispo, idMenu);
            ok = menuCtrl.ajouterPlat(p);
        } else {
            // MODIFICATION
            platAModifier.setNom(nom);
            platAModifier.setDescription(desc);
            platAModifier.setPrix(prix);
            platAModifier.setDisponible(dispo);
            platAModifier.setIdMenu(idMenu);
            ok = menuCtrl.modifierPlat(platAModifier);
        }

        if (ok) {
            confirme = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.", "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Retourne true si l'utilisateur a valide le formulaire avec succes. */
    public boolean isConfirme() { return confirme; }
}
