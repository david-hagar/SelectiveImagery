package com.davidhagar.selectiveimagery.model;;

public class ImageFactory
{

	public static LayeredImage createImage( int detailLevel )
	{
		LayeredImage image = new LayeredImage(detailLevel, true);
		
		
		
		
		return image;
	}
	
	public static LayeredImage createTileImage( int detailLevel )
	{
		LayeredImage image = new LayeredImage(detailLevel, false);
		
		image.hLayer.setValuesToTile();
		image.sLayer.setValuesToTile();
		image.vLayer.setValuesToTile();
		
		
		return image;
	}
	

	
	
	public static LayeredImage createRainbowImage( int detailLevel )
	{
		LayeredImage image = new LayeredImage(detailLevel, false);
		
		int size = image.getSize();
		
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				image.hLayer.value[i][j] = ((float) j)/(size-1);
				image.vLayer.value[i][j] = ((float) i)/(size-1);
				image.sLayer.value[i][j] = 1; //(i+j)%2;
			}
		}
		
		
		return image;
	}
	
	
	public static LayeredImage createDoubledRainbowImage( int detailLevel )
	{
		LayeredImage image = createRainbowImage(detailLevel);
		
		image.hLayer.value = Layer.doubleGrid(image.hLayer.value);
		image.sLayer.value = Layer.doubleGrid(image.sLayer.value);
		image.vLayer.value = Layer.doubleGrid(image.vLayer.value);
		
		image.detailLevel +=1;
		
		return image;
	}
	
	
	
	public static LayeredImage createTiledImage( int detailLevel )
	{
		LayeredImage image = new LayeredImage(detailLevel, false);
		
		int size = image.getSize();
		
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				image.hLayer.value[i][j] = 1;
				//image.vLayer.value[i][j] = (i+j)/(float)size;
				image.vLayer.value[i][j] = (i+j)%2;
				image.sLayer.value[i][j] = 0;
			}
		}
		
		
		return image;
	}
	
	public static LayeredImage createDoubledTiledImage( int detailLevel )
	{
		LayeredImage image1 = createTiledImage(detailLevel);
		LayeredImage image2 = createTiledImage(detailLevel+1);
		
		image2.vLayer.value = Layer.doubleGrid(image1.vLayer.value);
				
		return image2;
	}
	
}
