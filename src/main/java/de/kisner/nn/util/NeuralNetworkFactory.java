package de.kisner.nn.util;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class NeuralNetworkFactory
{
	public static int[] hidden;
	
	public static BasicNetwork build()
	{
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,6));
		for(int i=0;i<hidden.length;i++)
		{
			if(hidden[i]>0)
			{
				network.addLayer(new BasicLayer(new ActivationLinear(), true, hidden[i]));
			}
		}
		
		network.addLayer(new BasicLayer(new ActivationLinear(), false, 2));
		network.getStructure().finalizeStructure () ;
		network.reset();
		return network;
	}	
}