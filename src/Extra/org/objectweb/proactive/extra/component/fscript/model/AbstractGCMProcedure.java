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

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.fscript.model.fractal.FractalModel;


/**
 * Abstract base class to ease the implementation of custom GCM-related procedures, which depend on the GCM model.
 *
 * @author The ProActive Team
 */
public abstract class AbstractGCMProcedure implements GCMProcedure {
    protected FractalModel model;

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindFc(String itfName, Object srvItf) throws NoSuchInterfaceException {
        if (GCMProcedure.MODEL_NAME.equals(itfName)) {
            this.model = (FractalModel) srvItf;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] listFc() {
        return new String[] { GCMProcedure.MODEL_NAME };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object lookupFc(String itfName) throws NoSuchInterfaceException {
        if (GCMProcedure.MODEL_NAME.equals(itfName)) {
            return this.model;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unbindFc(String itfName) throws NoSuchInterfaceException {
        if (GCMProcedure.MODEL_NAME.equals(itfName)) {
            this.model = null;
        } else {
            throw new NoSuchInterfaceException(itfName);
        }
    }
}
