/*
 * Copyright 2012 Cédric Sougné
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
package utils;

import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.serializer.StringSerializer;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * <p>This TypeFactory is required to communicate with python. 
 * Without this, <string> element are not included into xml transferts.</p> 
 * 
 * <p>Solution found by Marcelo Grassi and seen on https://issues.apache.org/jira/browse/XMLRPC-138</p> 
 * 
 * @author Cédric Sougné
 *
 */
public class MyTypeFactory extends TypeFactoryImpl {

	/**
	 * <p>Type Serializer</p>
	 */
	private static final TypeSerializer myStringSerializer =
	
	/**
	 * <p>Overide xml write to include <string> element</p>
	 * 
	 * @author cso990
	 *
	 */
	new StringSerializer() {
		@Override
		public void write(ContentHandler pHandler, Object pObject)
				throws SAXException {
			write(pHandler, STRING_TAG, pObject.toString());
		}
	};

	/**
	 * Constructor
	 * 
	 * @param pController
	 */
	public MyTypeFactory(XmlRpcController pController) {
		super(pController);
	}

	/* (non-Javadoc)
	 * @see org.apache.xmlrpc.common.TypeFactoryImpl#getSerializer(org.apache.xmlrpc.common.XmlRpcStreamConfig, java.lang.Object)
	 */
	@Override
	public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig,
			Object pObject) throws SAXException {
		if (pObject instanceof String) {
			return myStringSerializer;
		}
		return super.getSerializer(pConfig, pObject);
	}
}
