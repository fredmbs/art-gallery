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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class Configuration extends JDialog {
    
    private final JPanel contentPanel = new JPanel();
    public int testCase = 0;
    public boolean ok = false;
    public String[] testCases = {"Gallery 1", "Simple Gallery"};

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
        setBounds(100, 100, 305, 211);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {120, 70};
        gridBagLayout.rowHeights = new int[] {30, 30, 30, 30};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
        getContentPane().setLayout(gridBagLayout);
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        GridBagConstraints gbc_contentPanel = new GridBagConstraints();
        gbc_contentPanel.anchor = GridBagConstraints.NORTH;
        gbc_contentPanel.insets = new Insets(0, 0, 5, 5);
        gbc_contentPanel.gridwidth = 2;
        gbc_contentPanel.gridx = 0;
        gbc_contentPanel.gridy = 0;
        getContentPane().add(contentPanel, gbc_contentPanel);
        {
            JLabel lblNewLabel = new JLabel("Configure the simulation");
            contentPanel.add(lblNewLabel);
        }
        {
            JLabel lblChooseScenario = new JLabel("Choose scenario");
            GridBagConstraints gbc_lblChooseScenario = new GridBagConstraints();
            gbc_lblChooseScenario.fill = GridBagConstraints.HORIZONTAL;
            gbc_lblChooseScenario.insets = new Insets(0, 0, 5, 5);
            gbc_lblChooseScenario.gridx = 0;
            gbc_lblChooseScenario.gridy = 1;
            getContentPane().add(lblChooseScenario, gbc_lblChooseScenario);
        }
        {
            final JComboBox<String> comboBox = new JComboBox<String>();
            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    testCase = comboBox.getSelectedIndex();
                }
            });
            GridBagConstraints gbc_comboBox = new GridBagConstraints();
            gbc_comboBox.anchor = GridBagConstraints.NORTHWEST;
            gbc_comboBox.insets = new Insets(0, 0, 5, 5);
            gbc_comboBox.gridx = 1;
            gbc_comboBox.gridy = 1;
            getContentPane().add(comboBox, gbc_comboBox);
            comboBox.setModel(new DefaultComboBoxModel<String>(testCases));
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            GridBagConstraints gbc_buttonPane = new GridBagConstraints();
            gbc_buttonPane.insets = new Insets(0, 0, 5, 5);
            gbc_buttonPane.anchor = GridBagConstraints.NORTH;
            gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
            gbc_buttonPane.gridwidth = 2;
            gbc_buttonPane.gridx = 0;
            gbc_buttonPane.gridy = 3;
            getContentPane().add(buttonPane, gbc_buttonPane);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        ok = true;
                        setVisible(false);
                    }
                });
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
