package de.kisner.nn;

import org.apache.commons.configuration.Configuration;
import org.jeesl.factory.txt.system.io.ssi.TxtSsiCredentialFactory;
import org.jeesl.model.json.system.io.ssi.SsiCrendentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.config.ConfigLoader;
import net.sf.exlp.util.io.ExlpCentralConfigPointer;
import net.sf.exlp.util.io.LoggerInit;

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
		LoggerInit loggerInit = new LoggerInit("log4j.xml");
		loggerInit.addAltPath("nn/config");
		loggerInit.init();
//			System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory"); 
//		JaxbUtil.setNsPrefixMapper(new TrNsPrefixMapper());	
			
		Configuration config = 	initConfig(configFile);		
		logger.debug("Config and Logger initialized");
		return config;
	}
	
	public static Configuration initConfig(String configFile)
	{
		try
		{
			ConfigLoader.add(ExlpCentralConfigPointer.getFile("nn","client").getAbsolutePath());
		}
		catch (ExlpConfigurationException e)
		{
			logger.debug("No optional config found. "+e.getMessage());
		}
		ConfigLoader.add(configFile);
		return ConfigLoader.init();
	}
	
	public static SsiCrendentials trCrendentials(Configuration config)
	{
		SsiCrendentials json = new SsiCrendentials();
		json.setUrl(config.getString("net.rest.tr.url"));
		json.setUser(config.getString("net.rest.tr.user"));
		json.setPassword(config.getString("net.rest.tr.pwd"));
		
		logger.info(TxtSsiCredentialFactory.debug(json));
		
		return json;
	}
}