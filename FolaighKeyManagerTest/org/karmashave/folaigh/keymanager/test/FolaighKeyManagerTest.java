/*
 * Created on Sep 18, 2005
 * By Terry Lacy
 */
package org.karmashave.folaigh.keymanager.test;

import java.security.Provider;
import java.security.Security;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.karmashave.folaigh.testutils.FolaighTestUtils;

public class FolaighKeyManagerTest extends TestCase {
    private static final String KEYSTORE_FILENAME = "testStore.p12";

    private static String keyStorePath;
    {
        try {
            keyStorePath = FolaighTestUtils
                    .writeKeyStoreToFile(KEYSTORE_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TestFolaighKeyManager keyManager;

    public void testFolaighKeyManager() throws Exception {
        keyManager.show();
    }

    public void testOpenKeyStore() throws Exception {
        keyManager.show();

        class MyTest implements org.netbeans.jemmy.Scenario {

            public int runIt(Object arg0) {
                // TODO Auto-generated method stub
                return 0;
            }
            
        }
        JMenuBar menuBar = keyManager.getJMenuBar();
        JMenuItem openKeyStore = null;
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            for (int j = 0; j < menu.getItemCount(); j++) {
                JMenuItem item = menu.getItem(j);
                if ( item.getText().equals("Open Key Store...") ) {
                    openKeyStore = item;
                    break;
                }
            }
            if ( openKeyStore != null ) {
                break;
            }
        }
        assertNotNull(openKeyStore);
        openKeyStore.doClick();
    }

    protected void setUp() throws Exception {
        super.setUp();
        Properties props = System.getProperties();
        Enumeration e = props.propertyNames();
        for (; e.hasMoreElements();) {
            String name = (String) e.nextElement();
            System.out.println(name);
        }
        if (Security.getProvider("BC") == null) {
            Provider bc = new BouncyCastleProvider();
            Security.insertProviderAt(bc, 5);
        }
        keyManager = new TestFolaighKeyManager();
    }

    protected void tearDown() throws Exception {
        keyManager = null;
        Security.removeProvider("BC");
        super.tearDown();
    }
}
