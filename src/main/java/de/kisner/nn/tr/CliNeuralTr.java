package de.kisner.nn.tr;

import javax.naming.NamingException;

import org.apache.commons.configuration.Configuration;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tipprunde.api.rest.PersonalRest;
import org.tipprunde.api.rest.StatisticRest;
import org.tipprunde.factory.txt.liga.TxtMatchFactory;
import org.tipprunde.model.xml.liga.Match;
import org.tipprunde.model.xml.liga.Matches;
import org.tipprunde.model.xml.liga.Round;
import org.tipprunde.processor.statistic.MatchBalanceProcessor;
import org.tipprunde.server.factory.txt.liga.TxtRoundFactory;

import de.kisner.nn.NnBootstrap;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.exception.processing.UtilsProcessingException;

public class CliNeuralTr
{
	final static Logger logger = LoggerFactory.getLogger(CliNeuralTr.class);

	private StatisticRest restStatistic;
	private PersonalRest restPersonal;
	
	public CliNeuralTr(Configuration config)
	{
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.register(new BasicAuthentication(config.getString("net.rest.tr.user"), config.getString("net.rest.tr.pwd")));
		ResteasyWebTarget restTarget = client.target(config.getString("net.rest.tr.url"));
		restStatistic = restTarget.proxy(StatisticRest.class);
		restPersonal = restTarget.proxy(PersonalRest.class);
	}
	
	public void test() throws UtilsProcessingException
	{
		Round round = restPersonal.round(352);
		
		OfxVectorTable ofx = new OfxVectorTable(round);
		
		logger.info(TxtRoundFactory.round(round));
		for(Match match : round.getMatch())
		{
			Matches matches = restStatistic.matches(match.getLeft().getOpponent().getId(),match.getRight().getOpponent().getId());
			double balance = MatchBalanceProcessor.relBalance(matches.getMatch(), match.getLeft().getOpponent());
			ofx.addData(balance);
			logger.info(TxtMatchFactory.opponents(match)+" "+balance);
		}
		ofx.addVector("Balance");
		ofx.debug();
	}
	
	public static void main(String[] args) throws UtilsNotFoundException, NamingException, UtilsProcessingException
	{
		Configuration config = NnBootstrap.init();
		CliNeuralTr cli = new CliNeuralTr(config);
		cli.test();
	}
}