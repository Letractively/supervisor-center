package utils;

import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.serializer.StringSerializer;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class MyTypeFactory extends TypeFactoryImpl {

    private static final TypeSerializer myStringSerializer =
            new StringSerializer() {
                public void write(ContentHandler pHandler, Object pObject) throws SAXException {
                    write(pHandler, STRING_TAG, pObject.toString());
                }
            };

    public MyTypeFactory(XmlRpcController pController) {
        super(pController);
    }

    public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig,
            Object pObject) throws SAXException {
        if (pObject instanceof String) {
            return myStringSerializer;
        }
        return super.getSerializer(pConfig, pObject);
    }
}
