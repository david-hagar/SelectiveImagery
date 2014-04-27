package com.davidhagar.selectiveimagery.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.MemoryImageSource;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("LayeredImage")
public class LayeredImage
{

	public int detailLevel = 1;

	public boolean isSelected = false;

	public Layer hLayer;
	public Layer sLayer;
	public Layer vLayer;

	transient private Image rgbImage = null;
	
	LayeredImage parent1 = null;
	LayeredImage parent2 = null;


	public LayeredImage(int detailLevel, boolean randomlyInitialize)
	{
		this.detailLevel = detailLevel;
		
		hLayer = new Layer(detailLevel, randomlyInitialize);
		sLayer = new Layer(detailLevel, randomlyInitialize);
		vLayer = new Layer(detailLevel, randomlyInitialize);
	}


	public int getSize()
	{
		return detailLevelToSize( detailLevel );
	}
	
	public static int detailLevelToSize( int detailLevel )
	{
		int size = 1 << detailLevel;
		
		return size;
	}


	public Image getImage(Component component)
	{
		if (rgbImage != null)
			return rgbImage;

		hLayer.addLayers();
		sLayer.addLayers();
		vLayer.addLayers();
		
		int size = getSize();
		//System.out.println(size + " " + detailLevel);
		int width = size;
		int height = size;

		int pix[] = new int[width * height];
		int index = 0;
		for (int j = 0; j < height; j++)
			for (int i = 0; i < width; i++)
			{
				float h = hLayer.value[i][j];
				float s = sLayer.value[i][j];
				float v = vLayer.value[i][j];
				pix[index++] = Color.HSBtoRGB(h, s, v);
			}

		rgbImage = component.createImage(new MemoryImageSource(width, height, pix,
				0, width));

		return rgbImage;
	}
}
