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

import org.etsi.uri.gcm.util.GCM;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.proactive.core.body.exceptions.BodyTerminatedRequestException;
import org.objectweb.proactive.extra.component.fscript.model.GCMComponentNode;


public class TestGCMComponentNode extends CommonSetup {
    private Component comp;
    private GCMComponentNode node;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        comp = composite;
        node = new GCMComponentNode(model, comp);
    }

    @Test(expected = NullPointerException.class)
    public void createNullComponent() {
        new GCMComponentNode(model, null);
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
    public void readComponentName() throws NoSuchInterfaceException {
        String realName = GCM.getNameController(comp).getFcName();
        assertEquals(realName, node.getName());
        assertEquals(realName, node.getProperty("name"));
    }

    @Test
    public void writeComponentName() throws NoSuchInterfaceException {
        String newName = "NewComponentName";
        node.setName(newName);
        assertEquals(newName, node.getName());
        String realName = GCM.getNameController(comp).getFcName();
        assertEquals(newName, realName);
        newName = "NewComponentName2";
        node.setProperty("name", newName);
        assertEquals(newName, node.getProperty("name"));
        realName = GCM.getNameController(comp).getFcName();
        assertEquals(newName, realName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNullComponentName() {
        node.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteNullComponentName() {
        node.setProperty("name", null);
    }

    @Test
    public void readComponentState() throws NoSuchInterfaceException {
        String realState = GCM.getGCMLifeCycleController(comp).getFcState();
        assertEquals(realState, node.getState());
        assertEquals(realState, node.getProperty("state"));
    }

    @Test(expected = BodyTerminatedRequestException.class)
    public void writeComponentState() throws NoSuchInterfaceException {
        String newState = "STARTED";
        node.setState(newState);
        assertEquals(newState, node.getState());
        String realState = GCM.getGCMLifeCycleController(comp).getFcState();
        assertEquals(newState, realState);
        newState = "STOPPED";
        node.setState(newState);
        assertEquals(newState, node.getState());
        realState = GCM.getGCMLifeCycleController(comp).getFcState();
        assertEquals(newState, realState);
        newState = "KILLED";
        node.setState(newState);
        node.getState();
    }

    @Test(expected = BodyTerminatedRequestException.class)
    public void genericWriteComponentState() throws NoSuchInterfaceException {
        String newState = "STARTED";
        node.setProperty("state", newState);
        assertEquals(newState, node.getProperty("state"));
        String realState = GCM.getGCMLifeCycleController(comp).getFcState();
        assertEquals(newState, realState);
        newState = "STOPPED";
        node.setProperty("state", newState);
        assertEquals(newState, node.getProperty("state"));
        realState = GCM.getGCMLifeCycleController(comp).getFcState();
        assertEquals(newState, realState);
        newState = "KILLED";
        node.setProperty("state", newState);
        node.getProperty("state");
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNullComponentState() {
        node.setState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteNullComponentState() {
        node.setProperty("state", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeInvalidComponentState() {
        node.setState("INVALID");
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteInvalidComponentState() {
        node.setProperty("state", "INVALID");
    }

    @Test
    public void readComponentMonitoringState() throws NoSuchInterfaceException {
        String realMonitoringState = getMonitoringState(comp);
        assertEquals(realMonitoringState, node.getState());
        assertEquals(realMonitoringState, node.getProperty("monitoring"));
    }

    @Test
    public void writeComponentMonitoringState() throws NoSuchInterfaceException {
        String newMonitoringState = "STARTED";
        node.setMonitoringState(newMonitoringState);
        assertEquals(newMonitoringState, node.getMonitoringState());
        String realMonitoringState = getMonitoringState(comp);
        assertEquals(newMonitoringState, realMonitoringState);
        newMonitoringState = "STOPPED";
        node.setMonitoringState(newMonitoringState);
        assertEquals(newMonitoringState, node.getState());
        realMonitoringState = getMonitoringState(comp);
        assertEquals(newMonitoringState, realMonitoringState);
        newMonitoringState = "RESET";
        node.setMonitoringState(newMonitoringState);
        newMonitoringState = "STARTED";
        node.setProperty("monitoring", newMonitoringState);
        assertEquals(newMonitoringState, node.getProperty("monitoring"));
        realMonitoringState = getMonitoringState(comp);
        assertEquals(newMonitoringState, realMonitoringState);
        newMonitoringState = "STOPPED";
        node.setProperty("monitoring", newMonitoringState);
        assertEquals(newMonitoringState, node.getProperty("monitoring"));
        realMonitoringState = getMonitoringState(comp);
        assertEquals(newMonitoringState, realMonitoringState);
        newMonitoringState = "RESET";
        node.setProperty("monitoring", newMonitoringState);
    }

    private String getMonitoringState(Component comp) throws NoSuchInterfaceException {
        boolean isMonitoringStarted = GCM.getMonitorController(comp).isGCMMonitoringStarted();
        return isMonitoringStarted ? "STARTED" : "STOPPED";
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNullComponentMonitoringState() {
        node.setMonitoringState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteNullComponentMonitoringState() {
        node.setProperty("monitoring", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeInvalidComponentMonitioringState() {
        node.setState("INVALID");
    }

    @Test(expected = IllegalArgumentException.class)
    public void genericWriteInvalidComponentMonitioringState() {
        node.setProperty("monitoring", "INVALID");
    }
}
