/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2013 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package functionalTests.component.fscript.model;

import static org.junit.Assert.assertEquals;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.util.AttributesHelper;
import org.objectweb.proactive.extra.component.fscript.model.GCMAttributeNode;


public class TestGCMAttributeNode extends CommonSetup {
    private AttributesHelper attrHelper;
    private String attrName;
    private GCMAttributeNode node;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        attrHelper = new AttributesHelper(serverWebService);
        attrName = "separator";
        node = new GCMAttributeNode(model, attrHelper, attrName);
    }

    @Test(expected = NullPointerException.class)
    public void createNullAttribute() {
        new GCMAttributeNode(model, attrHelper, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createInvalidAttribute() {
        new GCMAttributeNode(model, attrHelper, "invalid");
    }

    @Test(expected = NoSuchElementException.class)
    public void readInvalidProperty() {
        node.getProperty("invalid");
    }

    @Test(expected = NoSuchElementException.class)
    public void writeInvalidProperty() {
        node.setProperty("invalid", null);
    }

    @Test
    public void readAttributeName() {
        String realAttributeName = attrName;
        assertEquals(realAttributeName, node.getName());
        assertEquals(realAttributeName, node.getProperty("name"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyAttributeName() {
        node.setProperty("name", "none");
    }

    @Test
    public void readAttributeType() {
        String realType = attrHelper.getAttributeType(attrName).getName();
        assertEquals(realType, node.getType());
        assertEquals(realType, node.getProperty("type"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyAttributeType() {
        node.setProperty("type", "none");
    }

    @Test
    public void readAttributeValue() {
        Object realValue = attrHelper.getAttribute(attrName);
        assertEquals(realValue, node.getValue());
        assertEquals(realValue, node.getProperty("value"));
    }

    @Test
    public void writeAttributeValue() {
        Object newValue = "-";
        node.setValue(newValue);
        assertEquals(newValue, node.getValue());
        Object realValue = attrHelper.getAttribute(attrName);
        assertEquals(newValue, realValue);
        newValue = "--";
        node.setProperty("value", newValue);
        assertEquals(newValue, node.getProperty("value"));
        realValue = attrHelper.getAttribute(attrName);
        assertEquals(newValue, realValue);
    }

    @Test
    public void readAttributeIsReadable() {
        assertEquals(true, node.isReadable());
        assertEquals(true, node.getProperty("readable"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyAttributeIsReadable() {
        node.setProperty("readable", true);
    }

    @Test
    public void readAttributeIsWritable() {
        assertEquals(true, node.isWritable());
        assertEquals(true, node.getProperty("writable"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyAttributeIsWritable() {
        node.setProperty("writable", true);
    }
}
