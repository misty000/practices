/*
 * Copyright 2010-2012 Luca Garulli (l.garulli--at--orientechnologies.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientechnologies.orient.object.iterator.graph;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.object.db.graph.ODatabaseGraphTx;
import com.orientechnologies.orient.object.db.graph.OGraphEdge;

/**
 * Iterator to browse all the edges.
 * 
 * @author Luca Garulli
 * 
 */
public class OGraphEdgeIterator extends OGraphElementIterator<OGraphEdge> {

	public OGraphEdgeIterator(final ODatabaseGraphTx iDatabase, final boolean iPolymorphic) {
		super(iDatabase, OGraphEdge.class.getSimpleName(), iPolymorphic);
	}

	@Override
	public OGraphEdge next(final String iFetchPlan) {
		final ODocument doc = underlying.next();

		OGraphEdge e = (OGraphEdge) database.getUserObjectByRecord(doc, null, !isReuseSameObject());
		if (e != null)
			return e;

		e = getObject();
		e.fromStream(doc);

		return e;
	}

}
