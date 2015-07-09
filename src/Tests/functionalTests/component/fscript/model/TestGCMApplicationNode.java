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
import org.objectweb.proactive.extra.component.fscript.model.GCMApplicationNode;


public class TestGCMApplicationNode extends CommonSetup {
    private GCMApplicationNode node;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        node = new GCMApplicationNode(model, gcmaLocal);
    }

    @Test(expected = NullPointerException.class)
    public void createNullGCMApplication() {
        new GCMApplicationNode(model, null);
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
    public void readGCMApplicationDescriptorPath() {
        String realDescriptorPath = gcmaLocal.getDescriptorURL().getPath();
        assertEquals(realDescriptorPath, node.getDescriptorPath());
        assertEquals(realDescriptorPath, node.getProperty("descriptor-path"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void writeReadOnlyGCMApplicationDescriptorPath() {
        node.setProperty("descriptor-path", "none");
    }

    @Test
    public void readGCMApplicationState() {
        assertEquals("UNKNOW", node.getProperty("state"));
    }

    @Test
    public void writeGCMApplicationState() {
        node.setState("KILLED");
    }

    @Test
    public void genericWriteGCMApplicationState() {
        node.setProperty("state", "KILLED");
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNullGCMApplicationState() {
        node.setState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteNullGCMApplicationState() {
        node.setProperty("state", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeInvalidGCMApplicationState() {
        node.setState("INVALID");
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteInvalidGCMApplicationState() {
        node.setProperty("state", "INVALID");
    }
}
