/**
 * 
 */
package it.geosolutions.opensdi2.wps.rest.plugin;

import java.io.Reader;
import java.io.StringReader;

import org.geotools.xml.EncoderDelegate;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * @author alessio.fabiani
 * 
 */
public class CDATAEncoder implements EncoderDelegate {
	
	String cData;
	
	public CDATAEncoder(String cData) {
		this.cData = cData;
	}
	
	@Override
	public void encode(ContentHandler output) throws Exception {
		((LexicalHandler) output).startCDATA();
		Reader r = new StringReader(cData);
        char[] buffer = new char[1024];
        int read;
        while ((read = r.read(buffer)) > 0) {
            output.characters(buffer, 0, read);
        }
        r.close();
		((LexicalHandler) output).endCDATA();
	}

}
