package com.larlf.general.i18n;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 为了配合Dom4J，对*.i18n.xml进行验证时，从本地取出DTD文件的类
 * @author larlf
 *
 */
public class I18nEntityResolver implements EntityResolver
{
	protected Logger log=Logger.getLogger(this.getClass());

	public InputSource resolveEntity(String publicId,String systemId) throws SAXException,IOException
	{

		if(systemId.endsWith("i18n.dtd"))
		{
			InputStream is=this.getClass().getClassLoader().getResourceAsStream("com/opesoft/framework/i18n/i18n.dtd");
			InputSource source=new InputSource(is);
			return source;
		}

		return null;
	}

}
