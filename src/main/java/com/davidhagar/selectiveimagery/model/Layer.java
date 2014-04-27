package com.davidhagar.selectiveimagery.model;

public class Layer
{
	public int detailLevel = 0;

	DetailLayer layers[];

	public float value[][];


	public Layer(int detailLevel, boolean randomlyInitialize)
	{
		this.detailLevel = detailLevel;

		layers = new DetailLayer[detailLevel + 1];
		for (int i = 0; i < detailLevel+1; i++)
		{
			layers[i] = new DetailLayer(i, randomlyInitialize);
		}

		int size = LayeredImage.detailLevelToSize( detailLevel);
		
		value = new float[size][size];
	}


	public void addLayers()
	{
		value = new float[1][1];
		value[0][0] = layers[0].value[0][0];


		for (int level = 1; level < detailLevel+1; level++)
		{
			//value = doubleGrid(value);
			value = doubleGridAve(value);
			//value = doubleGridBiLinear(value);
			add(value, layers[level].value);
		}

		scale(value, 1.0f/layers.length);
		clip(value);

	}


	private void clip(float[][] grid)
	{
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
			{
				/*
                float v = grid[i][j];
				if (v < 0)
					grid[i][j] = 0;

				if (v > 1)
					grid[i][j] = 1;
					*/

                grid[i][j] = grid[i][j] % 1.0f;
			}

	}


	public static void add(float[][] grid1, float[][] grid2)
	{

		for (int i = 0; i < grid1.length; i++)
			for (int j = 0; j < grid1[i].length; j++)
			{
				grid1[i][j] += grid2[i][j];
			}

	}
	
	public static void scale(float[][] grid1, float scale)
	{

		for (int i = 0; i < grid1.length; i++)
			for (int j = 0; j < grid1[i].length; j++)
			{
				grid1[i][j] *= scale;
			}

	}

	public static float[][] doubleGrid(float[][] grid)
	{

		int newSize = grid.length << 1;
		float newValue[][] = new float[newSize][newSize];

		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
			{
				int i2 = i << 1;
				int j2 = j << 1;

				newValue[i2][j2] = newValue[i2 + 1][j2] = newValue[i2][j2 + 1] = newValue[i2 + 1][j2 + 1] = grid[i][j];

			}

		return newValue;
	}


	public static float[][] doubleGridAve(float[][] grid)
	{

		int newSize = grid.length << 1;
		float newValue[][] = new float[newSize][newSize];

		for (int i = 0; i < grid.length - 1; i++)
			for (int j = 0; j < grid[i].length - 1; j++)
			{
				int i2 = i << 1;
				int j2 = j << 1;

				newValue[i2][j2] = grid[i][j];
				newValue[i2 + 1][j2] = (grid[i][j] + grid[i + 1][j]) / 2;
				newValue[i2][j2 + 1] = (grid[i][j] + grid[i][j + 1]) / 2;
				newValue[i2 + 1][j2 + 1] = (grid[i][j] + grid[i][j + 1]
						+ grid[i + 1][j] + grid[i + 1][j + 1]) / 4;

			}

		int last = grid.length - 1;

		for (int last2 = newSize - 2; last2 < newSize; last2++)
			for (int i = 0; i < grid.length - 1; i++)
			{
				int i2 = i << 1;

				newValue[last2][i2] = grid[last][i];
				newValue[last2][i2 + 1] = (grid[last][i] + grid[last][i + 1]) / 2;

				newValue[i2][last2] = grid[i][last];
				newValue[i2 + 1][last2] = (grid[i][last] + grid[i + 1][last]) / 2;


			}


		return newValue;
	}


	public static float[][] doubleGridBiLinear(float[][] grid)
	{				
		BiLinearInterpolator bi = new BiLinearInterpolator();

		int newSize = grid.length << 1;
		float newValue[][] = new float[newSize][newSize];

		if( grid.length == 1)
		{
			for (int i = 0; i < newValue.length; i++)
				for (int j = 0; j < newValue[i].length; j++)
					newValue[i][j] = grid[0][0];
			
			return newValue;
		}

		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
			{
				int i2 = i << 1;
				int j2 = j << 1;

				int iSign = (i == grid.length - 1) ? -1 : 1;
				int jSign = (j == grid[i].length - 1) ? -1 : 1;

				bi.f_0_0 = grid[i][j];
				bi.f_0_1 = grid[i][j + jSign];
				bi.f_1_0 = grid[i + iSign][j];
				bi.f_1_1 = grid[i + iSign][j + jSign];

				newValue[i2][j2] = bi.getNonNegativeValue(-0.25f * iSign, -0.25f
						* jSign);
				newValue[i2 + 1][j2] = bi.getNonNegativeValue(0.25f * iSign, -0.25f
						* jSign);
				newValue[i2][j2 + 1] = bi.getNonNegativeValue(-0.25f * iSign,
						0.25f * jSign);
				newValue[i2 + 1][j2 + 1] = bi.getNonNegativeValue(0.25f * iSign,
						0.25f * jSign);

			}

		return newValue;
	}


	private static class BiLinearInterpolator
	{
		public float f_0_0 = 0;
		public float f_0_1 = 0;
		public float f_1_0 = 0;
		public float f_1_1 = 0;


		public float getValue(float x, float y)
		{
			return f_0_0 * (1 - x) * (1 - y) + f_1_0 * x * (1 - y) + f_0_1 * (1 - x)
					* y + f_1_1 * x * y;
		}


		public float getNonNegativeValue(float x, float y)
		{
			float f = f_0_0 * (1 - x) * (1 - y) + f_1_0 * x * (1 - y) + f_0_1
					* (1 - x) * y + f_1_1 * x * y;
			if (f < 0)
				f = 0;

			if (f > 1)
				f = 1;
			return Math.abs(f);
		}
	}


	public void setValuesToTile()
	{
		for (int i = 0; i < layers.length; i++)
		{
			layers[i].setValuesToTile();
		}
	}
	
	public void setValue(float value)
	{
		for (int i = 0; i < layers.length; i++)
		{
			layers[i].setValue(value);
		}
	}
}
