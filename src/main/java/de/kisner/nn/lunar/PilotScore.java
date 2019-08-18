package de.kisner.nn.lunar;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.networks.BasicNetwork;

public class PilotScore implements CalculateScore
{
	@Override public double calculateScore(MLMethod network)
	{
		NeuralPilot pilot = new NeuralPilot((BasicNetwork)network, false);
		return pilot.scorePilot();
	}

	@Override public boolean shouldMinimize(){return false;}
	@Override public boolean requireSingleThreaded() {return false;}
}