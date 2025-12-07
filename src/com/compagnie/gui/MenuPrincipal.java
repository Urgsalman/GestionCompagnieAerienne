package com.compagnie.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("✈️ Gestion Compagnie Aérienne - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 280);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 25, 25));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel titre = new JLabel("Sélectionnez la gestion à effectuer :", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(new Color(40, 40, 40));

        JButton btnPassager = new JButton(" Gérer les Passagers");
        JButton btnBillet = new JButton(" Gérer les Billets");

        Color blueFlight = new Color(0, 102, 204);
        Color greenTicket = new Color(76, 175, 80);

        btnPassager.setBackground(blueFlight);
        btnPassager.setForeground(Color.WHITE);
        btnPassager.setFont(new Font("Arial", Font.PLAIN, 14));

        btnBillet.setBackground(greenTicket);
        btnBillet.setForeground(Color.WHITE);
        btnBillet.setFont(new Font("Arial", Font.PLAIN, 14));

        btnPassager.addActionListener(e -> new MiseAJourPassager().setVisible(true));
        btnBillet.addActionListener(e -> new MiseAJourBillet().setVisible(true));

        mainPanel.add(titre);
        mainPanel.add(btnPassager);
        mainPanel.add(btnBillet);

        add(mainPanel);
    }
}