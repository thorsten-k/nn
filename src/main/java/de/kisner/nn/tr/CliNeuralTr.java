package de.kisner.nn.tr;

import javax.naming.NamingException;
import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.configuration.Configuration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.BasicAuthentication;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tipprunde.api.rest.provide.rs.LigaHistoricalRest;
import org.tipprunde.api.rest.provide.rs.PersonalRest;
import org.tipprunde.factory.txt.liga.TxtRoundFactory;
import org.tipprunde.model.xml.liga.Match;
import org.tipprunde.model.xml.liga.Matches;
import org.tipprunde.model.xml.liga.Round;
import org.tipprunde.processor.statistic.MatchBalanceProcessor;
import org.tipprunde.util.filter.xml.XmlMatchesFilter;

import de.kisner.nn.NnBootstrap;

public class CliNeuralTr
{
	final static Logger logger = LoggerFactory.getLogger(CliNeuralTr.class);

	private LigaHistoricalRest restStatistic;
	private PersonalRest restPersonal;
	
	public CliNeuralTr(Configuration config)
	{
		JsonSsiCredential credentials = NnBootstrap.trCrendentials(config);
		ResteasyClient client = (ResteasyClient)ClientBuilder.newClient();
		client.register(new BasicAuthentication(credentials.getUser(),credentials.getPassword()));
		ResteasyWebTarget restTarget = client.target(credentials.getUrl());

		restStatistic = restTarget.proxy(LigaHistoricalRest.class);
		restPersonal = restTarget.proxy(PersonalRest.class);
	}
	
	public void test() throws UtilsProcessingException
	{
		Round round = restPersonal.round(384);
		
		OfxVectorTable ofx = new OfxVectorTable(round);
		
		logger.info(TxtRoundFactory.round(round));
				
		for(Match match : round.getMatch())
		{
			
			Matches matches = restStatistic.matches(match.getLeft().getOpponent().getId(),match.getRight().getOpponent().getId());
			double balance = MatchBalanceProcessor.tendenceRelative(matches.getMatch(), match.getLeft().getOpponent());
			ofx.addData(balance);
		}
		ofx.addVector("Tendence");
		
		for(Match match : round.getMatch())
		{
			Matches matches = restStatistic.matches(match.getLeft().getOpponent().getId(),match.getRight().getOpponent().getId());
			double balance = MatchBalanceProcessor.balance(matches.getMatch(), match.getLeft().getOpponent());
			ofx.addData(balance);
		}
		ofx.addVector("Balance 5");
		
		for(Match match : round.getMatch())
		{
			Matches matches = XmlMatchesFilter.last(5,restStatistic.matches(match.getLeft().getOpponent().getId(),match.getRight().getOpponent().getId()));
			matches = XmlMatchesFilter.last(5,matches);
			ofx.addData(MatchBalanceProcessor.results(matches.getMatch(), match.getLeft().getOpponent()));
		}
		ofx.addVector("Direkt");
		
		for(Match match : round.getMatch())
		{	
			Matches matches = restStatistic.matches(match.getLeft().getOpponent().getId());
			matches = XmlMatchesFilter.home(match.getLeft().getOpponent().getId(),matches);
			matches = XmlMatchesFilter.last(5,matches);
			
			ofx.addData(MatchBalanceProcessor.results(matches.getMatch(), match.getLeft().getOpponent()));
		}
		ofx.addVector("Heim-Heim");
		
		for(Match match : round.getMatch())
		{		
			Matches matches = restStatistic.matches(match.getLeft().getOpponent().getId());
			matches = XmlMatchesFilter.guest(match.getRight().getOpponent().getId(),matches);
			matches = XmlMatchesFilter.last(5,matches);

			ofx.addData(MatchBalanceProcessor.results(matches.getMatch(), match.getLeft().getOpponent()));
		}
		ofx.addVector("Gast-Gast");
		
		
		ofx.debug();
	}
	
	public static void main(String[] args) throws JeeslNotFoundException, NamingException, UtilsProcessingException
	{
		Configuration config = NnBootstrap.init();
		CliNeuralTr cli = new CliNeuralTr(config);
		cli.test();
	}
}