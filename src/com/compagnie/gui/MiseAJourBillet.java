package com.compagnie.gui;

import com.compagnie.dao.BilletDAO;
import com.compagnie.dao.PassagerDAO;
import com.compagnie.models.Billet;
import com.compagnie.models.Passager;
import com.compagnie.models.ClasseBillet;
import com.compagnie.models.StatutBillet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MiseAJourBillet extends JFrame {

    private BilletDAO billetDAO = new BilletDAO();
    private PassagerDAO passagerDAO = new PassagerDAO();

    // Champs de Saisie
    private JTextField txtNumBillet = new JTextField(20);
    private JComboBox<PassagerComboItem> cbIdentifiantPassager = new JComboBox<>();
    private JTextField txtNumVol = new JTextField(15);
    private JTextField txtNumSiege = new JTextField(10);
    private JComboBox<ClasseBillet> cbClasse = new JComboBox<>(ClasseBillet.values());
    private JTextField txtTarif = new JTextField(10);
    private JComboBox<StatutBillet> cbStatut = new JComboBox<>(StatutBillet.values());
    private JTextField txtDateEmission = new JTextField("AAAA-MM-JJ", 10);

    // NOUVEAUX ÉLÉMENTS GUI pour l'affichage de la table
    private JTable billetTable;
    private BilletTableModel tableModel;

    public MiseAJourBillet() {
        setTitle("Mise à jour Billet (CRUD) et Affichage 🎟️");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // MODIFICATION: Utilisation de BorderLayout pour intégrer la table
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(null);
        getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- 1. Panneau de Saisie (FORMULAIRE) ---
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 8));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Détails du Billet"),
                new EmptyBorder(5, 5, 5, 5)
        ));

        formPanel.add(new JLabel("Numéro Billet (Clé primaire) :"));
        formPanel.add(txtNumBillet);
        formPanel.add(new JLabel("Passager (Nom + ID) :"));
        formPanel.add(cbIdentifiantPassager);
        formPanel.add(new JLabel("Numéro Vol :"));
        formPanel.add(txtNumVol);
        formPanel.add(new JLabel("Numéro Siège :"));
        formPanel.add(txtNumSiege);
        formPanel.add(new JLabel("Classe :"));
        formPanel.add(cbClasse);
        formPanel.add(new JLabel("Tarif (Ex: 150.50) :"));
        formPanel.add(txtTarif);
        formPanel.add(new JLabel("Statut :"));
        formPanel.add(cbStatut);
        formPanel.add(new JLabel("Date Emission (AAAA-MM-JJ) :"));
        formPanel.add(txtDateEmission);

        // --- 2. Panneau de Boutons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnChercher = new JButton("Chercher");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

        // Style des boutons
        btnAjouter.setBackground(new Color(50, 150, 50));
        btnAjouter.setForeground(Color.WHITE);
        btnModifier.setBackground(new Color(255, 193, 7));
        btnModifier.setForeground(Color.BLACK);
        btnSupprimer.setBackground(new Color(220, 53, 69));
        btnSupprimer.setForeground(Color.WHITE);
        btnChercher.setBackground(new Color(108, 117, 125));
        btnChercher.setForeground(Color.WHITE);

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnChercher);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        // Panneau de contrôle combiné (Formulaire + Boutons)
        JPanel controlsPanel = new JPanel(new BorderLayout(0, 10));
        controlsPanel.add(formPanel, BorderLayout.CENTER);
        controlsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- 3. JTable pour l'affichage (Nouvel ajout) ---
        billetTable = new JTable();
        billetTable.setFillsViewportHeight(true);
        billetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        billetTable.getTableHeader().setBackground(new Color(50, 50, 50));

        JScrollPane scrollPane = new JScrollPane(billetTable);
        scrollPane.setBorder(new TitledBorder("Liste des Billets Enregistrés (Cliquez pour éditer)"));

        // Ajout de l'événement de sélection de ligne
        billetTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = billetTable.getSelectedRow();
                if (selectedRow != -1) {
                    Billet b = tableModel.getBilletAt(billetTable.convertRowIndexToModel(selectedRow));
                    fillFormWithBillet(b);
                }
            }
        });

        // --- 4. Ajout des composants à la fenêtre ---
        add(controlsPanel, BorderLayout.NORTH); // Formulaire et boutons en haut
        add(scrollPane, BorderLayout.CENTER);   // Table en bas

        // --- 5. Initialisation et actions ---
        loadPassagerComboBox();
        loadBillets(); // Chargement initial des billets

        btnAjouter.addActionListener(e -> handleAjouter());
        btnChercher.addActionListener(e -> handleChercher());
        btnModifier.addActionListener(e -> handleModifier());
        btnSupprimer.addActionListener(e -> handleSupprimer());

        pack();
        setSize(1000, 650); // Taille ajustée pour le tableau
    }

    // NOUVELLE MÉTHODE : Chargement/rafraîchissement des données de la table des Billets
    private void loadBillets() {
        try {
            List<Billet> billets = billetDAO.listerTous();
            tableModel = new BilletTableModel(billets);
            billetTable.setModel(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des billets: " + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPassagerComboBox() {
        cbIdentifiantPassager.removeAllItems();
        try {
            List<Passager> passagers = passagerDAO.listerTous();
            for (Passager p : passagers) {
                cbIdentifiantPassager.addItem(new PassagerComboItem(p));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des passagers: " + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAjouter() {
        try {
            Billet b = createBilletFromForm();
            if (billetDAO.chercher(b.getNumBillet()) != null) {
                JOptionPane.showMessageDialog(this, "Ce numéro de billet existe déjà. Veuillez utiliser Modifier.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (billetDAO.ajouter(b)) {
                JOptionPane.showMessageDialog(this, "Billet " + b.getNumBillet() + " ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadBillets(); // Rafraîchissement
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'ajout.", "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleChercher() {
        try {
            String numBillet = txtNumBillet.getText().trim();
            if (numBillet.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer le Numéro Billet à chercher.", "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Billet b = billetDAO.chercher(numBillet);
            if (b != null) {
                fillFormWithBillet(b);
                JOptionPane.showMessageDialog(this, "Billet trouvé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Billet non trouvé.", "Non trouvé", JOptionPane.WARNING_MESSAGE);
                clearForm();
                txtNumBillet.setText(numBillet);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur BD: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleModifier() {
        try {
            Billet b = createBilletFromForm();
            if (billetDAO.modifier(b)) {
                JOptionPane.showMessageDialog(this, "Billet " + b.getNumBillet() + " modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadBillets(); // Rafraîchissement
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la modification (Numéro Billet non trouvé ou erreur BD).", "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSupprimer() {
        try {
            String numBillet = txtNumBillet.getText().trim();
            if (numBillet.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer le Numéro Billet à supprimer.", "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer le Billet " + numBillet + "?", "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (billetDAO.supprimer(numBillet)) {
                    JOptionPane.showMessageDialog(this, "Billet " + numBillet + " supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadBillets(); // Rafraîchissement
                } else {
                    JOptionPane.showMessageDialog(this, "Échec de la suppression (Numéro Billet non trouvé).", "Erreur BD", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur BD: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Billet createBilletFromForm() throws Exception {
        Billet b = new Billet();

        b.setNumBillet(txtNumBillet.getText().trim());

        PassagerComboItem selectedPassager = (PassagerComboItem) cbIdentifiantPassager.getSelectedItem();
        if (selectedPassager == null || selectedPassager.getId() == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un passager valide.");
        }
        b.setIdentificateurPassager(selectedPassager.getId());

        try {
            b.setTarif(Double.parseDouble(txtTarif.getText().trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le Tarif doit être un nombre valide.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateE = dateFormat.parse(txtDateEmission.getText().trim());
        b.setDateEmission(dateE);

        b.setNumVol(txtNumVol.getText());
        b.setNumSiege(txtNumSiege.getText());
        b.setClasse((ClasseBillet) cbClasse.getSelectedItem());
        b.setStatut((StatutBillet) cbStatut.getSelectedItem());

        return b;
    }

    private void fillFormWithBillet(Billet b) {
        txtNumBillet.setText(b.getNumBillet());
        txtNumVol.setText(b.getNumVol());
        txtNumSiege.setText(b.getNumSiege());
        cbClasse.setSelectedItem(b.getClasse());
        txtTarif.setText(String.format("%.2f", b.getTarif()));
        cbStatut.setSelectedItem(b.getStatut());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        txtDateEmission.setText(dateFormat.format(b.getDateEmission()));

        try {
            Passager passagerLie = passagerDAO.chercher(b.getIdentificateurPassager());
            if (passagerLie != null) {
                PassagerComboItem item = new PassagerComboItem(passagerLie);
                cbIdentifiantPassager.setSelectedItem(item);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur de recherche du passager lié: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtNumBillet.setText("");
        txtNumVol.setText("");
        txtNumSiege.setText("");
        txtTarif.setText("");
        txtDateEmission.setText("AAAA-MM-JJ");
        cbClasse.setSelectedIndex(0);
        cbStatut.setSelectedIndex(0);
        if(cbIdentifiantPassager.getItemCount() > 0) {
            cbIdentifiantPassager.setSelectedIndex(0);
        }
    }
}