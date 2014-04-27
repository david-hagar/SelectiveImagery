package com.davidhagar.selectiveimagery.model;

public class DetailLayer
{

	public float value [][];
	public float mutability [][];

	
	public DetailLayer(int detailLevel, boolean initializeRandom )
	{
		
		int size = LayeredImage.detailLevelToSize( detailLevel);

		
		value = new float[size][size];
		mutability = new float[size][size];	
		
		
		if( initializeRandom )
		{
			//float max = 1.0f / (detailLevel +1);
			float max = 1.0f;
			
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < size; j++)
				{
					value[i][j] = (float) Math.random() * max;
					mutability[i][j] = 0.5f;
				}
			}
		}
		
	}
	
	public void setValuesToTile()
	{
		for (int i = 0; i < value.length; i++)
			for (int j = 0; j < value[i].length; j++)
			{
				//value[i][j] = (i+j)%2;
				value[i][j] = (i%2)*(j%2);
			}

	}

	public void setValue(float valueToSet)
	{
		for (int i = 0; i < value.length; i++)
			for (int j = 0; j < value[i].length; j++)
			{
				value[i][j] = valueToSet;
			}
	}

}
