/**
 * Distributed Art-Gallery
 *
 *  @author Frederico Martins Biber Sampaio
 *
 * The MIT License (MIT)
 * 
 * Copyright (C) 2013  Frederico Martins Biber Sampaio
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. 
*/

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import distributed.ArtGalleryFactory;


@SuppressWarnings("serial")
public class Configuration extends JDialog {
    
    private final JPanel contentPanel = new JPanel();
    public int testCase = 0;
    public int algorithm = 0;
    public boolean ok = false;
    public String[] testCases = {"Gallery 1", "Simple Gallery", "Gallery 2"};
    public String fileName = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            Configuration dialog = new Configuration();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create the dialog.
     */
    public Configuration() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setAlwaysOnTop(true);
        setTitle("Distributed Art Gallery");
        setBounds(100, 100, 326, 237);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {30, 120, 120, 30};
        gridBagLayout.rowHeights = new int[] {10, 10, 10, 10, 10, 10, 10};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        getContentPane().setLayout(gridBagLayout);
        {
            JLabel lblNewLabel = new JLabel("Configure the simulation");
            GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
            gbc_lblNewLabel.gridwidth = 2;
            gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
            gbc_lblNewLabel.gridx = 1;
            gbc_lblNewLabel.gridy = 0;
            getContentPane().add(lblNewLabel, gbc_lblNewLabel);
        }
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        GridBagConstraints gbc_contentPanel = new GridBagConstraints();
        gbc_contentPanel.anchor = GridBagConstraints.NORTH;
        gbc_contentPanel.insets = new Insets(0, 0, 5, 5);
        gbc_contentPanel.gridwidth = 2;
        gbc_contentPanel.gridx = 0;
        gbc_contentPanel.gridy = 1;
        getContentPane().add(contentPanel, gbc_contentPanel);
        {
            final JComboBox<String> comboBox = new JComboBox<String>();
            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    testCase = comboBox.getSelectedIndex();
                }
            });
            {
                JLabel lblChooseScenario = new JLabel("Scenario");
                GridBagConstraints gbc_lblChooseScenario = new GridBagConstraints();
                gbc_lblChooseScenario.anchor = GridBagConstraints.EAST;
                gbc_lblChooseScenario.insets = new Insets(0, 0, 5, 5);
                gbc_lblChooseScenario.gridx = 1;
                gbc_lblChooseScenario.gridy = 2;
                getContentPane().add(lblChooseScenario, gbc_lblChooseScenario);
            }
            GridBagConstraints gbc_comboBox = new GridBagConstraints();
            gbc_comboBox.anchor = GridBagConstraints.WEST;
            gbc_comboBox.insets = new Insets(0, 0, 5, 5);
            gbc_comboBox.gridx = 2;
            gbc_comboBox.gridy = 2;
            getContentPane().add(comboBox, gbc_comboBox);
            comboBox.setModel(new DefaultComboBoxModel<String>(testCases));
        }
        {
            JLabel lblChooseAlgorithm = new JLabel("Algorithm");
            GridBagConstraints gbc_lblChooseAlgorithm = new GridBagConstraints();
            gbc_lblChooseAlgorithm.anchor = GridBagConstraints.EAST;
            gbc_lblChooseAlgorithm.insets = new Insets(0, 0, 5, 5);
            gbc_lblChooseAlgorithm.gridx = 1;
            gbc_lblChooseAlgorithm.gridy = 3;
            getContentPane().add(lblChooseAlgorithm, gbc_lblChooseAlgorithm);
        }
        {
            final JComboBox<String> comboBox = new JComboBox<String>();
            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    algorithm = comboBox.getSelectedIndex();
                }
            });
            GridBagConstraints gbc_comboBox = new GridBagConstraints();
            gbc_comboBox.anchor = GridBagConstraints.WEST;
            gbc_comboBox.insets = new Insets(0, 0, 5, 5);
            gbc_comboBox.gridx = 2;
            gbc_comboBox.gridy = 3;
            getContentPane().add(comboBox, gbc_comboBox);
            comboBox.setModel(new DefaultComboBoxModel<String>(ArtGalleryFactory.algorithms));
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            GridBagConstraints gbc_buttonPane = new GridBagConstraints();
            gbc_buttonPane.anchor = GridBagConstraints.NORTH;
            gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
            gbc_buttonPane.gridwidth = 2;
            gbc_buttonPane.gridx = 1;
            gbc_buttonPane.gridy = 5;
            getContentPane().add(buttonPane, gbc_buttonPane);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        ok = true;
                        setVisible(false);
                    }
                });
                {
                    JButton btnFile = new JButton("File");
                    btnFile.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                            final JFileChooser fc = new JFileChooser("."); 
                            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            int returnVal = fc.showOpenDialog(fc);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                fileName = fc.getSelectedFile().getPath();
                                ok = true;
                                testCase = 99;
                                setVisible(false);
                            }
                        }
                    });
                    buttonPane.add(btnFile);
                }
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ok = false;
                        setVisible(false);
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }
    
}
