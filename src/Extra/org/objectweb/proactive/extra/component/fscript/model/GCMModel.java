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

import static org.objectweb.fractal.fscript.types.PrimitiveType.BOOLEAN;
import static org.objectweb.fractal.fscript.types.PrimitiveType.STRING;

import java.util.ArrayList;
import java.util.List;

import org.etsi.uri.gcm.api.type.GCMInterfaceType;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fscript.model.ComposedAxis;
import org.objectweb.fractal.fscript.model.Model;
import org.objectweb.fractal.fscript.model.Property;
import org.objectweb.fractal.fscript.model.ReflectiveAxis;
import org.objectweb.fractal.fscript.model.TransitiveAxis;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;
import org.objectweb.fractal.fscript.procedures.NativeLibrary;
import org.objectweb.fractal.util.AttributesHelper;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.gcmdeployment.GCMApplication;
import org.objectweb.proactive.gcmdeployment.GCMVirtualNode;


/**
 * This class represents the GCM component model in terms of the {@link Model} APIs. It describes which
 * kinds of nodes are present in a GCM architecture, what properties these nodes have, and what axes can
 * connect these nodes together to form a complete GCM architecture. It also provides all the
 * introspection and reconfiguration procedures implied by this description (through its {@link NativeLibrary}
 * interface).
 * <br>
 * This is an extension of the {@link FractalModel} to take care of GCM specificities.
 *
 * @author The ProActive Team
 */
public class GCMModel extends FractalModel implements GCMNodeFactory, BindingController {
    /** The GCM factory name. */
    public final static String GCM_FACTORY_NAME = "gcm-factory";
    /** The GCM ADL Factory used by actions which create new GCM components. */
    private Factory gcmFactory;

    /**
     * Contributes the different kinds of nodes used to represent GCM architectures.
     */
    @Override
    protected void createNodeKinds() {
        super.createNodeKinds();

        addKind("gcmapplication", new Property("descriptor-path", STRING, false), new Property("state",
            STRING, true));
        addKind("gcmvirtualnode", new Property("name", STRING, false), new Property("state", STRING, false));
        addKind("gcmnode", new Property("name", STRING, false), new Property("url", STRING, false));

        // Following node kinds override Fractal node kinds to add GCM properties
        addKind("component", new Property("name", STRING, true), new Property("state", STRING, true),
                new Property("monitoring", STRING, true));
        addKind("interface", new Property("name", STRING, false), new Property("internal", BOOLEAN, false),
                new Property("signature", STRING, false), new Property("collection", BOOLEAN, false),
                new Property("optional", BOOLEAN, false), new Property("client", BOOLEAN, false),
                new Property("singleton", BOOLEAN, false), new Property("multicast", BOOLEAN, false),
                new Property("gathercast", BOOLEAN, false));
    }

    /**
     * Contributes the axes which can be used to connect GCM nodes in order to represent the structure of
     * a GCM architecture.
     */
    @Override
    protected void createAxes() {
        super.createAxes();

        // Base axes
        addAxis(new GCMVirtualNodeAxis(this));
        addAxis(new GCMNodeAxis(this));
        addAxis(new GCMComponentAxis(this));
        addAxis(new GCMChildAxis(this));
        addAxis(new GCMParentAxis(this));
        addAxis(new GCMBindingAxis(this));
        addAxis(new GCMAttributeAxis(this));

        // Following axes override Fractal axes to take into account GCM base axes
        // Transitive axes
        addAxis(new TransitiveAxis(getAxis("child"), "descendant"));
        addAxis(new TransitiveAxis(getAxis("parent"), "ancestor"));
        // Composed axes
        addAxis(new ComposedAxis("sibling", getAxis("parent"), getAxis("child")));
        // Reflective axes
        addAxis(new ReflectiveAxis(getAxis("child")));
        addAxis(new ReflectiveAxis(getAxis("parent")));
        addAxis(new ReflectiveAxis(getAxis("descendant")));
        addAxis(new ReflectiveAxis(getAxis("ancestor")));
        addAxis(new ReflectiveAxis(getAxis("sibling")));
    }

    /**
     * Contributes a few custom procedures to manipulate GCM architecture which can not be described and
     * generated in the framework of the {@link Model} APIs.
     */
    @Override
    protected void createAdditionalProcedures() {
        super.createAdditionalProcedures();

        List<GCMProcedure> procedures = new ArrayList<GCMProcedure>();
        procedures.add(new DeployGCMApplicationAction());
        procedures.add(new GCMNewAction());
        procedures.add(new GCMMigrateAction());
        procedures.add(new RemoteExecuteAction());
        procedures.add(new RemoteGetGlobalsAction());
        procedures.add(new RemoteLoadAction());
        procedures.add(new WSBindAction());
        procedures.add(new WSExposeAction());
        procedures.add(new WSUnexposeAction());
        for (GCMProcedure procedure : procedures) {
            try {
                procedure.bindFc(GCMProcedure.MODEL_NAME, this);
            } catch (Exception e) {
                throw new AssertionError("Internal inconsistency with " + procedure.getName() + " procedure");
            }
            addProcedure(procedure);
        }
    }

    /**
     * Creates an {@link InterfaceNode} to represent an interface of a Fractal component or a
     * {@link GCMInterfaceNode} to represent an interface of a GCM component.
     *
     * @param itf An interface of a Fractal or GCM component.
     * @return An {@link InterfaceNode} or a {@link GCMInterfaceNode} representing the interface.
     */
    @Override
    public InterfaceNode createInterfaceNode(Interface itf) {
        if (itf.getFcItfType() instanceof GCMInterfaceType) {
            return new GCMInterfaceNode(this, itf);
        } else {
            return super.createInterfaceNode(itf);
        }
    }

    /**
     * Creates a {@link GCMApplicationNode} to represent a {@link GCMApplication}.
     *
     * @param gcma A {@link GCMApplication}.
     * @return A {@link GCMApplicationNode} representing the {@link GCMApplication}.
     */
    public GCMApplicationNode createGCMApplicationNode(GCMApplication gcma) {
        return new GCMApplicationNode(this, gcma);
    }

    /**
     * Creates a {@link GCMVirtualNodeNode} to represent a {@link GCMVirtualNode}.
     *
     * @param gcmvn A {@link GCMVirtualNode}.
     * @return A {@link GCMVirtualNodeNode} representing the {@link GCMVirtualNode}.
     */
    public GCMVirtualNodeNode createGCMVirtualNodeNode(GCMVirtualNode gcmvn) {
        return new GCMVirtualNodeNode(this, gcmvn);
    }

    /**
     * Creates a {@link GCMNodeNode} to represent a GCM {@link Node}.
     *
     * @param gcmnode A GCM {@link Node}.
     * @return A {@link GCMNodeNode} representing the GCM {@link Node}.
     */
    public GCMNodeNode createGCMNodeNode(Node gcmnode) {
        return new GCMNodeNode(this, gcmnode);
    }

    /**
     * Creates a {@link GCMComponentNode} to represent a GCM component.
     *
     * @param comp A GCM component.
     * @return A {@link GCMComponentNode} representing the GCM component.
     */
    public GCMComponentNode createGCMComponentNode(Component comp) {
        return new GCMComponentNode(this, comp);
    }

    /**
     * Creates a {@link GCMAttributeNode} to represent an attribute of a GCM component.
     *
     * @param comp A GCM component.
     * @param attr The name of the attribute to represent.
     * @return A {@link GCMAttributeNode} representing the attribute of the GCM component.
     */
    public GCMAttributeNode createGCMAttributeNode(Component comp, String attr) {
        return new GCMAttributeNode(this, new AttributesHelper(comp), attr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException {
        if (GCM_FACTORY_NAME.equals(clientItfName)) {
            this.gcmFactory = (Factory) serverItf;
        } else {
            super.bindFc(clientItfName, serverItf);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] listFc() {
        String[] superItfs = super.listFc();
        String[] result = new String[superItfs.length + 1];
        System.arraycopy(superItfs, 0, result, 0, superItfs.length);
        result[result.length - 1] = GCM_FACTORY_NAME;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
        if (GCM_FACTORY_NAME.equals(clientItfName)) {
            return this.gcmFactory;
        } else {
            return super.lookupFc(clientItfName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unbindFc(String clientItfName) throws NoSuchInterfaceException {
        if (GCM_FACTORY_NAME.equals(clientItfName)) {
            this.gcmFactory = null;
        } else {
            super.unbindFc(clientItfName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GCM model";
    }
}
