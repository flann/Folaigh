/* 
Copyright 2005 Karmashave.org/Terry Lacy

This file is part of Folaigh.

Folaigh is free software; you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation; either version 2.1 of the License, or
(at your option) any later version.

Folaigh is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Folaigh; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
/*
 * Created on Aug 14, 2005
 * By Terry Lacy
 */
package org.karmashave.folaigh.test;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.karmashave.folaigh.test");
        //$JUnit-BEGIN$
        suite.addTestSuite(FolaighTest.class);
        suite.addTestSuite(FolaighKeyStoreTest.class);
        suite.addTestSuite(RSACipherTest.class);
        suite.addTestSuite(AESCipherTest.class);
        suite.addTestSuite(FolaighKeyGeneratorTest.class);
        //$JUnit-END$
        return suite;
    }
}
