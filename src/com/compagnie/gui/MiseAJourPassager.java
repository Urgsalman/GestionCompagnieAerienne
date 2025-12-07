package com.compagnie.gui;

import com.compagnie.dao.PassagerDAO;
import com.compagnie.models.Passager;
import com.compagnie.models.TypePassager;
import com.compagnie.models.Genre;

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

public class MiseAJourPassager extends JFrame {

    private PassagerDAO passagerDAO = new PassagerDAO();

    private JTextField txtId = new JTextField(10);
    private JTextField txtNom = new JTextField(20);
    private JTextField txtPrenom = new JTextField(20);
    private JTextField txtDateNaissance = new JTextField("AAAA-MM-JJ", 10);
    private JTextField txtNumPasseport = new JTextField(20);
    private JTextField txtNationalite = new JTextField(20);
    private JComboBox<TypePassager> cbTypePassager = new JComboBox<>(TypePassager.values());
    private JComboBox<Genre> cbGenre = new JComboBox<>(Genre.values());
    private JTextField txtEmail = new JTextField(30);
    private JTextField txtTelephone = new JTextField(15);

    private JTable passagerTable;
    private PassagerTableModel tableModel;

    public MiseAJourPassager() {
        setTitle("Mise à jour Passager (CRUD) et Affichage 👤");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(15, 15));
        getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15));

        txtId.setEditable(true);

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 8));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Détails du Passager"),
                new EmptyBorder(5, 5, 5, 5)
        ));

        formPanel.add(new JLabel("ID Passager :")); formPanel.add(txtId);
        formPanel.add(new JLabel("Nom :")); formPanel.add(txtNom);
        formPanel.add(new JLabel("Prenom :")); formPanel.add(txtPrenom);
        formPanel.add(new JLabel("Date de Naissance (AAAA-MM-JJ) :")); formPanel.add(txtDateNaissance);
        formPanel.add(new JLabel("Numéro Passeport :")); formPanel.add(txtNumPasseport);
        formPanel.add(new JLabel("Nationalité :")); formPanel.add(txtNationalite);
        formPanel.add(new JLabel("Type Passager :")); formPanel.add(cbTypePassager);
        formPanel.add(new JLabel("Genre :")); formPanel.add(cbGenre);
        formPanel.add(new JLabel("Email :")); formPanel.add(txtEmail);
        formPanel.add(new JLabel("Téléphone :")); formPanel.add(txtTelephone);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnChercher = new JButton("Chercher");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

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

        JPanel controlsPanel = new JPanel(new BorderLayout(0, 10));
        controlsPanel.add(formPanel, BorderLayout.CENTER);
        controlsPanel.add(buttonPanel, BorderLayout.SOUTH);

        passagerTable = new JTable();
        passagerTable.setFillsViewportHeight(true);
        passagerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        passagerTable.getTableHeader().setBackground(new Color(220, 220, 255)); // En-tête de tableau stylisé

        JScrollPane scrollPane = new JScrollPane(passagerTable);
        scrollPane.setBorder(new TitledBorder("Liste des Passagers Enregistrés (Cliquez pour éditer)"));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        passagerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = passagerTable.getSelectedRow();
                if (selectedRow != -1) {
                    Passager p = tableModel.getPassagerAt(passagerTable.convertRowIndexToModel(selectedRow));
                    fillFormWithPassager(p);
                }
            }
        });

        add(controlsPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadPassagers();

        btnAjouter.addActionListener(e -> handleAjouter());
        btnChercher.addActionListener(e -> handleChercher());
        btnModifier.addActionListener(e -> handleModifier());
        btnSupprimer.addActionListener(e -> handleSupprimer());

        pack();
        setSize(850, 650);
    }

    private void loadPassagers() {
        try {
            List<Passager> passagers = passagerDAO.listerTous();
            tableModel = new PassagerTableModel(passagers);
            passagerTable.setModel(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données: " + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleAjouter() {
        try {
            Passager p = createPassagerFromForm();
            if (p.getIdentificateurPassager() != null) {
                JOptionPane.showMessageDialog(this, "Utiliser Modifier pour un passager existant.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (passagerDAO.ajouter(p)) {
                JOptionPane.showMessageDialog(this, "Passager ajouté (ID: " + p.getIdentificateurPassager() + ")", "Succès", JOptionPane.INFORMATION_MESSAGE);
                txtId.setText(String.valueOf(p.getIdentificateurPassager()));
                loadPassagers();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'ajout.", "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleChercher() {
        try {
            Integer id = Integer.parseInt(txtId.getText());
            Passager p = passagerDAO.chercher(id);
            if (p != null) {
                fillFormWithPassager(p);
                JOptionPane.showMessageDialog(this, "Passager trouvé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Passager non trouvé.", "Non trouvé", JOptionPane.WARNING_MESSAGE);
                clearForm();
                txtId.setText(String.valueOf(id));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un ID Passager valide.", "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur BD: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleModifier() {
        try {
            Passager p = createPassagerFromForm();
            if (p.getIdentificateurPassager() == null) {
                JOptionPane.showMessageDialog(this, "Veuillez Chercher un ID valide avant de Modifier.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (passagerDAO.modifier(p)) {
                JOptionPane.showMessageDialog(this, "Passager ID " + p.getIdentificateurPassager() + " modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadPassagers();
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la modification (ID non trouvé ou erreur BD).", "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSupprimer() {
        try {
            Integer id = Integer.parseInt(txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer le Passager ID " + id + "?", "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (passagerDAO.supprimer(id)) {
                    JOptionPane.showMessageDialog(this, "Passager ID " + id + " supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadPassagers();
                } else {
                    JOptionPane.showMessageDialog(this, "Échec de la suppression (ID non trouvé).", "Erreur BD", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un ID Passager valide à supprimer.", "Erreur de Saisie", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur BD: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Passager createPassagerFromForm() throws Exception {
        Passager p = new Passager();

        if (!txtId.getText().trim().isEmpty()) {
            p.setIdentificateurPassager(Integer.parseInt(txtId.getText().trim()));
        }

        p.setNom(txtNom.getText());
        p.setPrenom(txtPrenom.getText());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateN = dateFormat.parse(txtDateNaissance.getText().trim());
        p.setDateNaissance(dateN);

        p.setNumPasseport(txtNumPasseport.getText());
        p.setNationalite(txtNationalite.getText());
        p.setTypePassager((TypePassager) cbTypePassager.getSelectedItem());
        p.setGenre((Genre) cbGenre.getSelectedItem());
        p.setEmail(txtEmail.getText());
        p.setTelephone(txtTelephone.getText());

        return p;
    }

    private void fillFormWithPassager(Passager p) {
        txtId.setText(String.valueOf(p.getIdentificateurPassager()));
        txtNom.setText(p.getNom());
        txtPrenom.setText(p.getPrenom());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        txtDateNaissance.setText(dateFormat.format(p.getDateNaissance()));

        txtNumPasseport.setText(p.getNumPasseport());
        txtNationalite.setText(p.getNationalite());
        cbTypePassager.setSelectedItem(p.getTypePassager());
        cbGenre.setSelectedItem(p.getGenre());
        txtEmail.setText(p.getEmail());
        txtTelephone.setText(p.getTelephone());
    }

    private void clearForm() {
        txtId.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtDateNaissance.setText("AAAA-MM-JJ");
        txtNumPasseport.setText("");
        txtNationalite.setText("");
        txtEmail.setText("");
        txtTelephone.setText("");
        cbTypePassager.setSelectedIndex(0);
        cbGenre.setSelectedIndex(0);
    }
}