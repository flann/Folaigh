/*
 * Created on Sep 16, 2005
 * By Terry Lacy
 */
package org.karmashave.folaigh.keymanager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTree;

public class FolaighKeyManager extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 2112833647487968064L;
    private JPanel jContentPane = null;
    private JMenuBar mainMenu = null;
    private JMenu menuFile = null;
    private JMenu menuHelp = null;
    private JMenuItem menuAbout = null;
    private JMenuItem menuOpen = null;
    private JMenuItem menuExit = null;
    private JTree treeKeys = null;
    /**
     * This method initializes mainMenu	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getMainMenu() {
        if (mainMenu == null) {
            mainMenu = new JMenuBar();
            mainMenu.add(getMenuFile());
            mainMenu.add(getMenuHelp());
        }
        return mainMenu;
    }

    /**
     * This method initializes menuFile	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getMenuFile() {
        if (menuFile == null) {
            menuFile = new JMenu();
            menuFile.setName("");
            menuFile.setText("File");
            menuFile.add(getMenuOpen());
            menuFile.add(getMenuExit());
        }
        return menuFile;
    }

    /**
     * This method initializes menuHelp	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getMenuHelp() {
        if (menuHelp == null) {
            menuHelp = new JMenu();
            menuHelp.setText("Help");
            menuHelp.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
            menuHelp.add(getMenuAbout());
        }
        return menuHelp;
    }

    /**
     * This method initializes menuAbout	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getMenuAbout() {
        if (menuAbout == null) {
            menuAbout = new JMenuItem();
            menuAbout.setText("About...");
        }
        return menuAbout;
    }

    /**
     * This method initializes menuOpen	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getMenuOpen() {
        if (menuOpen == null) {
            menuOpen = new JMenuItem();
            menuOpen.setText("Open Key Store...");
        }
        return menuOpen;
    }

    /**
     * This method initializes menuExit	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getMenuExit() {
        if (menuExit == null) {
            menuExit = new JMenuItem();
            menuExit.setText("Exit");
            menuExit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doExit();
                }
            });
        }
        return menuExit;
    }

    /**
     * This method initializes treeKeys	
     * 	
     * @return javax.swing.JTree	
     */
    private JTree getTreeKeys() {
        if (treeKeys == null) {
            treeKeys = new JTree();
        }
        return treeKeys;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        FolaighKeyManager mgr = new FolaighKeyManager();
        mgr.show();
    }

    protected void doExit() {
        exit();
    }
    
    /**
     * This is the default constructor
     */
    public FolaighKeyManager() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(535, 310);
        this.setJMenuBar(getMainMenu());
        this.setContentPane(getJContentPane());
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                exit();
            }
        });
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.gridx = 0;
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(getTreeKeys(), gridBagConstraints);
        }
        return jContentPane;
    }

    protected void exit() {
        System.exit(0);
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
