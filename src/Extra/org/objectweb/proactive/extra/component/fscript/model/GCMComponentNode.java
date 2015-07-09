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
package org.objectweb.proactive.extra.component.fscript.model;

import java.util.NoSuchElementException;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;


/**
 * A {@link Node} which represents a GCM component. Note that in FPath, component nodes are distinct from interface
 * nodes representing the <code>component</code> interface.
 * <br>
 * This is an extension of {@link ComponentNode} to give a GCM personality to the node.
 *
 * @author The ProActive Team
 */
public class GCMComponentNode extends ComponentNode {
    /**
     * Creates a new {@link GCMComponentNode}.
     *
     * @param model The GCM model the node is part of.
     * @param comp The GCM component the new node will represent.
     */
    public GCMComponentNode(FractalModel model, Component comp) {
        super(model, comp);
    }

    /**
     * Returns the current value of one of the node's properties.
     *
     * @param name The name of the property to access.
     * @return The current value of the named property for the node.
     * @throws NoSuchElementException If the node does not have a property of the given name.
     */
    @Override
    public Object getProperty(String name) {
        if ("monitoring".equals(name)) {
            return getMonitoringState();
        } else {
            return super.getProperty(name);
        }
    }

    /**
     * Changes the value of one of the node's properties.
     *
     * @param name The name of the property to change.
     * @param value The new value to set for the property.
     * @throws NoSuchElementException If the node does not have a property of the given name.
     * @throws IllegalArgumentException If the requested value is invalid for the named property.
     * @throws UnsupportedOperationException If the specified property exists, but is not modifiable.
     * @throws RuntimeException If the underlying operation, which applies the change to the component, failed. The
     * initial error is available through <code>getCause()<code>.
     */
    @Override
    public void setProperty(String name, Object value) {
        checkSetRequest(name, value);
        if ("monitoring".equals(name)) {
            setMonitoringState((String) value);
        } else {
            super.setProperty(name, value);
        }
    }

    /**
     * Returns the current monitoring state of the component, using the GCM <code>monitor-controller</code>
     * interface. If the component does not provide the GCM <code>monitor-controller</code> interface the
     * monitoring's state is the empty string.
     *
     * @return The current monitoring state of the component.
     */
    public String getMonitoringState() {
        try {
            if (GCM.getMonitorController(getComponent()).isGCMMonitoringStarted()) {
                return "STARTED";
            } else {
                return "STOPPED";
            }
        } catch (NoSuchInterfaceException nsie) {
            return "";
        }
    }

    /**
     * Sets the lifecycle state of the component, using the GCM or the Fractal <code>lifecycle-controller</code>
     * interface.
     *
     * @param state The requested new state of the component. The only values currently supported are
     * <code>"STARTED"</code>, <code>"STOPPED"</code> and <code>"KILLED"</code>.
     * @throws IllegalArgumentException If the name of the requested state is not supported or if setting this
     * state would cause an invalid state transition.
     * @throws UnsupportedOperationException If the component does not provide the GCM or the Fractal
     * <code>lifecycle-controller</code> interface.
     */
    @Override
    public void setState(String state) {
        try {
            if ("KILLED".equals(state)) {
                GCM.getGCMLifeCycleController(getComponent()).terminateGCMComponent();
            } else {
                super.setState(state);
            }
        } catch (NoSuchInterfaceException nsie) {
            throw new UnsupportedOperationException("Can not change the state of this component", nsie);
        } catch (IllegalLifeCycleException ilce) {
            throw new IllegalArgumentException("Invalid state transition", ilce);
        }
    }

    /**
     * Sets the monitoring state of the component, using the GCM <code>monitor-controller</code> interface.
     *
     * @param monitoringState The requested new monitoring state of the component. The only values currently
     * supported are <code>"STARTED"</code>, <code>"STOPPED"</code> and <code>"RESET"</code>.
     * @throws IllegalArgumentException If the name of the requested monitoring state is not supported.
     * @throws UnsupportedOperationException If the component does not provide the GCM
     * <code>monitor-controller</code> interface.
     */
    public void setMonitoringState(String monitoringState) {
        try {
            if ("STARTED".equals(monitoringState)) {
                GCM.getMonitorController(getComponent()).startGCMMonitoring();
            } else if ("STOPPED".equals(monitoringState)) {
                GCM.getMonitorController(getComponent()).stopGCMMonitoring();
            } else if ("RESET".equals(monitoringState)) {
                GCM.getMonitorController(getComponent()).resetGCMMonitoring();
            } else {
                throw new IllegalArgumentException("Invalid value for 'monitoringState': '" +
                    monitoringState + "'");
            }
        } catch (NoSuchInterfaceException nsie) {
            throw new UnsupportedOperationException("Can not change the monitoring state of this component",
                nsie);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GCMComponentNode) {
            return super.equals(obj);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "#<gcmcomponent: " + getName() + ">";
    }
}
