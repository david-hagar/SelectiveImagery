package com.davidhagar.selectiveimagery;

import java.io.File;
import java.io.IOException;

import com.davidhagar.selectiveimagery.model.Generation;
import com.davidhagar.selectiveimagery.model.ImageFactory;
import com.davidhagar.selectiveimagery.model.LayeredImage;
import com.davidhagar.selectiveimagery.model.Project;

import junit.framework.TestCase;

public class ProjectIOTest extends TestCase
{

	public void test() throws IOException
	{
		Project p = new Project();
		p.setupInitalProject();
		
//		Generation g = new Generation();
//		p.generationList.add( g );
//		
//		for (int i = 0; i < 2; i++)
//		{
//			LayeredImage img = ImageFactory.createRainbowImage( 7);
//			g.imageList.add( img);
//		}
		
		File file = new File("target/testOut.xml");
		p.save(file );
		
		Project p2 = new Project();
		p2.load(file );
		p2.save( new File("target/testOut2.xml") );
	
	}
	
	
}
