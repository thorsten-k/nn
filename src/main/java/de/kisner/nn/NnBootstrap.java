package de.kisner.nn;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.jeesl.factory.txt.system.io.ssi.TxtSsiCredentialFactory;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tipprunde.server.api.facade.TrFacade;

public class NnBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(NnBootstrap.class);
	
	public final static String xmlConfig = "nn/system/property/nn.xml";
	
	public static org.exlp.interfaces.system.property.Configuration wrap()
	{
		LoggerBootstrap.instance().path("nn/system/io/log").init();
		
		String configFile = "nn/config/nn.xml";
		ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(TrFacade.IoSsiSystemCode.tr).jaxb(JaxbUtil.instance());
		org.exlp.interfaces.system.property.Configuration config = ConfigBootstrap.instance().add(ccp.toPath("client")).add(configFile).wrap();
		return config;
	}
	
	public static JsonSsiCredential trCrendentials(org.exlp.interfaces.system.property.Configuration config)
	{
		JsonSsiCredential json = new JsonSsiCredential();
		json.setUrl(config.getString("net.rest.tr.url"));
		json.setUser(config.getString("net.rest.tr.user"));
		json.setPassword(config.getString("net.rest.tr.pwd"));
		
		logger.info(TxtSsiCredentialFactory.debug(json));
		
		return json;
	}
}