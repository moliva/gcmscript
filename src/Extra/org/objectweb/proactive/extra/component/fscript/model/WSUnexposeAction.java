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

import static org.objectweb.fractal.fscript.types.VoidType.VOID_TYPE;

import java.util.List;
import java.util.Set;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.types.NodeSetType;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.fractal.fscript.types.UnionType;
import org.objectweb.proactive.extensions.webservices.component.Utils;
import org.objectweb.proactive.extensions.webservices.component.controller.PAWebServicesController;
import org.objectweb.proactive.extensions.webservices.exceptions.WebServicesException;


/**
 * A GCM procedure to implement the <code>ws-unexpose()</code> action, which unexposes as web services one, several
 * or all server interfaces of a GCM component.
 *
 * @author The ProActive Team
 */
public class WSUnexposeAction extends AbstractGCMProcedure {
    /**
     * Returns the name of the procedure.
     *
     * @return The name of the procedure.
     */
    public String getName() {
        return "ws-unexpose";
    }

    /**
     * Indicates that the procedure is an action.
     *
     * @return <code>false</code> to indicate that the procedure is an action.
     */
    public boolean isPureFunction() {
        return false;
    }

    /**
     * Returns the signature of the procedure.
     *
     * @return The signature of the procedure.
     */
    public Signature getSignature() {
        return new Signature(VOID_TYPE, new UnionType(model.getNodeKind("interface")), new NodeSetType(model
                .getNodeKind("interface")), model.getNodeKind("component"));
    }

    /**
     * Unexposes as web services one, several or all server interfaces of a GCM component.
     *
     * @param args The arguments of the procedure call. Must contain as first element a {@link GCMInterfaceNode}
     * representing a server interface if only this server interface must be unexposed as web service. Must contain
     * as first element a set of {@link GCMInterfaceNode} representing a set server interfaces if several server
     * interfaces must be unexposed as web services. Must contain as first element a {@link GCMComponentNode}
     * representing a GCM component if all the server interfaces of this GCM component must be unexposed as web
     * services.
     * @param ctx The execution context in which to execute the procedure.
     * @return <code>null</code>.
     * @throws ScriptExecutionError If any error occurred during the execution of the procedure.
     */
    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        if ((args.get(0) instanceof GCMInterfaceNode) || (args.get(0) instanceof Set<?>) ||
            (args.get(0) instanceof GCMComponentNode)) {
            try {
                Component comp = null;
                String[] itfNames = null;
                if (args.get(0) instanceof GCMInterfaceNode) {
                    Interface itf = ((GCMInterfaceNode) args.get(0)).getInterface();
                    comp = itf.getFcItfOwner();
                    itfNames = new String[] { itf.getFcItfName() };
                } else if (args.get(0) instanceof Set<?>) {
                    GCMInterfaceNode[] itfNodes = ((Set<?>) args.get(0)).toArray(new GCMInterfaceNode[] {});
                    itfNames = new String[itfNodes.length];
                    for (int i = 0; i < itfNodes.length; i++) {
                        itfNames[i] = itfNodes[i].getInterface().getFcItfName();
                        if (comp == null) {
                            comp = itfNodes[i].getInterface().getFcItfOwner();
                        }
                        if (!comp.equals(itfNodes[i].getInterface().getFcItfOwner())) {
                            throw new ScriptExecutionError(
                                Diagnostic
                                        .error(
                                                SourceLocation.UNKNOWN,
                                                "Unable to unexpose component interfaces as web services: given server interfaces do not belong to the same GCM component"));
                        }
                    }
                } else {
                    comp = ((GCMComponentNode) args.get(0)).getComponent();
                }
                PAWebServicesController wsc = Utils.getPAWebServicesController(comp);
                wsc.unExposeComponentAsWebService(GCM.getNameController(comp).getFcName(), itfNames);
            } catch (NoSuchInterfaceException nsie) {
                throw new ScriptExecutionError(Diagnostic.error(SourceLocation.UNKNOWN,
                        "Unable to unexpose component interface(s) as web service(s)"), nsie);
            } catch (WebServicesException wse) {
                throw new ScriptExecutionError(Diagnostic.error(SourceLocation.UNKNOWN,
                        "Unable to unexpose component interface(s) as web service(s)"), wse);
            }
        }
        return null;
    }
}
