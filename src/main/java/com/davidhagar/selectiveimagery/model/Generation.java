package com.davidhagar.selectiveimagery.model;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Generation")
public class Generation
{

	public ArrayList<LayeredImage> imageList = new ArrayList<LayeredImage>();


	public Generation()
	{

	}


	public ArrayList<LayeredImage> getSelected()
	{
		ArrayList<LayeredImage> list = new ArrayList<LayeredImage>();

		for (LayeredImage image : imageList)
		{
			if (image.isSelected)
				list.add(image);
		}

		return list;
	}


	public Generation createNextGeneration(ProjectSettings projectSettings)
	{
		Generation newGeneration = new Generation();

//		for (LayeredImage image : imageList)
//		{
//			image.isSelected = false;
//		}
		
		newGeneration.imageList.addAll(imageList);
		int numberToAdd = projectSettings.numberOfGeneratedImages - imageList.size();
		
		for (int i = 0; i < numberToAdd ; i++)
		{
			LayeredImage image1 = imageList.get((int) (Math.random() * imageList
					.size()));
			LayeredImage image2;
			while( true )
			{
				image2 = imageList.get((int) (Math.random() * imageList
					.size()));
				if( image1 != image2 )
					break;
			}
			
			LayeredImage newImage = merge( image1, image2, projectSettings );

			newGeneration.imageList.add(newImage);
		}


		return newGeneration;
	}


	private LayeredImage merge(LayeredImage image1, LayeredImage image2, ProjectSettings projectSettings)
	{
		LayeredImage newImage = new LayeredImage( image1.detailLevel, false );
		
		newImage.parent1 = image1;
		newImage.parent2 = image2;
		
		mergeLayeredImage( newImage.hLayer, image1.hLayer, image2.hLayer, projectSettings );
		mergeLayeredImage( newImage.sLayer, image1.sLayer, image2.sLayer, projectSettings );
		mergeLayeredImage( newImage.vLayer, image1.vLayer, image2.vLayer, projectSettings );
		
		
		return newImage;
	}


	private void mergeLayeredImage(Layer newlayer, Layer layer1, Layer layer2, ProjectSettings projectSettings)
	{
		
		for (int i = 0; i < newlayer.layers.length; i++)
		{
			mergeLayer(newlayer.layers[i],layer1.layers[i],layer2.layers[i], projectSettings);
		}
		
		
	}


	private void mergeLayer(DetailLayer newlayer, DetailLayer layer1, DetailLayer layer2, ProjectSettings projectSettings)
	{
		float[][] newValue = newlayer.value;
		float[][] newMutability = newlayer.mutability;
		
		for (int i = 0; i < newValue.length; i++)
			for (int j = 0; j < newValue[i].length; j++)
			{
				newValue[i][j] = ( Math.random() >0.5 ) ? layer1.value[i][j]: layer2.value[i][j];
				
				newMutability[i][j] =0.1f;
//				float aveParentMutability = (layer1.mutability[i][j] + layer2.mutability[i][j])/2;
//				if(  layer1.value[i][j] ==  layer2.value[i][j] )
//					newMutability[i][j] =  aveParentMutability / projectSettings.mutabilityChange;
//				else
//					newMutability[i][j] = aveParentMutability * projectSettings.mutabilityChange;
//				
//				if( newMutability[i][j] > 1.0f)
//					newMutability[i][j] = 1.0f;
				
				if( Math.random() < newMutability[i][j] )
				{
					newValue[i][j] =  (float)(newValue[i][j]  + (Math.random()-0.5)* 2.0) % 1.0f;
				}
				
			}

		
	}


}
