package de.kisner.nn;

import org.apache.commons.configuration.Configuration;
import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.factory.txt.system.io.ssi.TxtSsiCredentialFactory;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tipprunde.server.api.facade.TrFacade;

import net.sf.exlp.exception.ExlpConfigurationException;

public class NnBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(NnBootstrap.class);
	
	public static Configuration init()
	{
		String configFile = "nn/config/nn.xml";
		return init(configFile);
	}
	
	public static Configuration init(String configFile)
	{
		LoggerBootstrap.instance().path("tr/system/io/log").init();
			
		Configuration config = 	initConfig(configFile);		
		logger.debug("Config and Logger initialized");
		return config;
	}
	
	public static Configuration initConfig(String configFile)
	{
		try
		{
			ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(TrFacade.IoSsiSystemCode.tr).jaxb(JaxbUtil.instance());
			ConfigLoader.addFile(ccp.toFile("client"));
		}
		catch (ExlpConfigurationException e)
		{
			logger.debug("No optional config found. "+e.getMessage());
		}
		ConfigLoader.addString(configFile);
		return ConfigLoader.init();
	}
	
	public static JsonSsiCredential trCrendentials(Configuration config)
	{
		JsonSsiCredential json = new JsonSsiCredential();
		json.setUrl(config.getString("net.rest.tr.url"));
		json.setUser(config.getString("net.rest.tr.user"));
		json.setPassword(config.getString("net.rest.tr.pwd"));
		
		logger.info(TxtSsiCredentialFactory.debug(json));
		
		return json;
	}
}