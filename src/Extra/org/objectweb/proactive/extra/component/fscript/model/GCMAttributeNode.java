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

import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.AttributeNode;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;
import org.objectweb.fractal.util.AttributesHelper;


/**
 * A {@link Node} which represents a configuration attribute of a GCM component.
 * <br>
 * This is an extension of {@link AttributeNode} to give a GCM personality to the node.
 *
 * @author The ProActive Team
 */
public class GCMAttributeNode extends AttributeNode {
    /**
     * Creates a new {@link GCMAttributeNode}.
     *
     * @param model The GCM model the node is part of.
     * @param attrHelper The helper to use to access the attribute value.
     * @param attrName The name of the attribute.
     */
    public GCMAttributeNode(FractalModel model, AttributesHelper attrHelper, String attrName) {
        super(model, attrHelper, attrName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GCMAttributeNode) {
            return super.equals(obj);
        } else {
            return false;
        }
    }
}
