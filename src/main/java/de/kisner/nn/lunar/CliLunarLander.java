package de.kisner.nn.lunar;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.MLMethod;
import org.encog.ml.MLResettable;
import org.encog.ml.MethodFactory;
import org.encog.ml.genetic.MLMethodGeneticAlgorithm;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A lunar lander game where the neural network learns to land a space craft.  
 * The neural network learns the proper amount of thrust to land softly 
 * and conserve fuel.
 * 
 * This example is unique because it uses supervised training, yet does not 
 * have expected values.  For this it can use genetic algorithms or 
 * simulated annealing.
 */
public class CliLunarLander
{
	final static Logger logger = LoggerFactory.getLogger(CliLunarLander.class);
	
	public static BasicNetwork createNetwork()
	{
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(3);
		pattern.addHiddenLayer(6);
		pattern.addHiddenLayer(3);
		pattern.setOutputNeurons(1);
		pattern.setActivationFunction(new ActivationTANH());
		BasicNetwork network = (BasicNetwork)pattern.generate();
		network.reset();
		return network;
	}
	
	public void threads()
	{
		BasicNetwork network = createNetwork();
		
		MLTrain train = new MLMethodGeneticAlgorithm(new MethodFactory()
			{
				@Override
				public MLMethod factor()
				{
					final BasicNetwork result = createNetwork();
					((MLResettable)result).reset();
					return result;
			}
			},new PilotScoreCalculator(),500);
		
		int epoch = 1;

		for(int i=0;i<50;i++)
		{
			train.iteration();
			logger.trace("Epoch #" + epoch + " Score:" + train.getError());
			epoch++;
		} 
		train.finishTraining();

		System.out.println("\nHow the winning network landed:");
		network = (BasicNetwork)train.getMethod();
		NeuralPilot pilot = new NeuralPilot(network,true);
		System.out.println(pilot.scorePilot());
	}
	
	public static void main(String args[])
	{
		for(int i=0;i<1;i++)
		{
			CliLunarLander ll = new CliLunarLander();
			ll.threads();
			Encog.getInstance().shutdown();
		}
	}
}