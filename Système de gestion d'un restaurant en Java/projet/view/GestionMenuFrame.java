package projet.view;

import controller.MenuController;
import model.Plat;
import model.Menu;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;


public class GestionMenuFrame extends JFrame {

    private JTable             table;
    private DefaultTableModel  tableModel;
    private JButton            btnAjouter, btnModifier, btnSupprimer,
                               btnEnregistrer, btnFermer;
    private MenuController     menuCtrl = new MenuController();
    private JComboBox<String>  cbxMenuFilter;

    public GestionMenuFrame() {
        super("Gestion des Menus et Plats");
        initUI();
        chargerPlats();
    }

    private void initUI() {
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === Table des plats ===
        String[] colonnes = {"ID", "Nom du plat", "Description", "Prix (TND)", "Disponible", "Menu"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        // Masquer colonne ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);

        // === Filtre par menu ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filtrer par menu :"));
        cbxMenuFilter = new JComboBox<>();
        cbxMenuFilter.addItem("Tous les menus");
        menuCtrl.getAllMenus().forEach(m -> cbxMenuFilter.addItem(m.getNom()));
        cbxMenuFilter.addActionListener(e -> chargerPlats());
        topPanel.add(cbxMenuFilter);

        // === Boutons ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));

        btnAjouter     = creerBouton("Ajouter",      new Color(34, 139, 34));
        btnModifier    = creerBouton("Modifier",      new Color(70, 130, 180));
        btnSupprimer   = creerBouton("Supprimer",     new Color(200, 50, 50));
        btnEnregistrer = creerBouton("Enregistrer",   new Color(180, 120, 0));
        btnFermer      = creerBouton("Fermer",        new Color(100, 100, 100));

        btnPanel.add(btnAjouter);
        btnPanel.add(btnModifier);
        btnPanel.add(btnSupprimer);
        btnPanel.add(btnEnregistrer);
        btnPanel.add(btnFermer);

        // === Actions des boutons ===
        btnAjouter.addActionListener(e -> actionAjouter());
        btnModifier.addActionListener(e -> actionModifier());
        btnSupprimer.addActionListener(e -> actionSupprimer());
        btnEnregistrer.addActionListener(e -> actionEnregistrer());
        btnFermer.addActionListener(e -> dispose());

        // === Layout ===
        setLayout(new BorderLayout(5, 5));
        add(topPanel,   BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel,   BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton("<< " + texte + " >>");
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 34));
        return btn;
    }

    private void chargerPlats() {
        tableModel.setRowCount(0);
        List<Plat> plats;
        String filtreMenuNom = (String) cbxMenuFilter.getSelectedItem();
        if ("Tous les menus".equals(filtreMenuNom)) {
            plats = menuCtrl.getAllPlats();
        } else {
            Menu m = menuCtrl.getMenuByNom(filtreMenuNom);
            plats = (m != null) ? menuCtrl.getPlatsByMenu(m.getId()) : menuCtrl.getAllPlats();
        }
        for (Plat p : plats) {
            tableModel.addRow(new Object[]{
                p.getId(), p.getNom(), p.getDescription(),
                String.format("%.3f", p.getPrix()),
                p.isDisponible() ? "Oui" : "Non",
                p.getNomMenu()
            });
        }
    }

    private void actionAjouter() {
        DialogAjouterPlat dlg = new DialogAjouterPlat(this, menuCtrl, null);
        dlg.setVisible(true);
        if (dlg.isConfirme()) chargerPlats();
    }

    private void actionModifier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un plat à modifier.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int   idPlat  = (int) tableModel.getValueAt(selectedRow, 0);
        Plat  plat    = menuCtrl.getPlatById(idPlat);
        DialogAjouterPlat dlg = new DialogAjouterPlat(this, menuCtrl, plat);
        dlg.setVisible(true);
        if (dlg.isConfirme()) chargerPlats();
    }

    private void actionSupprimer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un plat à supprimer.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String nomPlat = (String) tableModel.getValueAt(selectedRow, 1);
        int    idPlat  = (int)   tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer le plat « " + nomPlat + " » ?",
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (menuCtrl.supprimerPlat(idPlat))
                JOptionPane.showMessageDialog(this, "Plat supprimé.");
            chargerPlats();
        }
    }

    private void actionEnregistrer() {
        JOptionPane.showMessageDialog(this,
            "Données enregistrées dans la base de données.",
            "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
    }
}
