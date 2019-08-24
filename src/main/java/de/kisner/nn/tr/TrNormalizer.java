package de.kisner.nn.tr;

import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

public class TrNormalizer
{
	public static NormalizedField nfScore()
	{
		return new NormalizedField(NormalizationAction.Normalize,null,4,0,1,0);
	}
	
	public static NormalizedField nfDiff()
	{
		return new NormalizedField(NormalizationAction.Normalize,null,4,-4,1,-1);
	}	
}