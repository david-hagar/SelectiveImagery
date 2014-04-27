package com.davidhagar.selectiveimagery.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias("Project")
public class Project
{

	public ArrayList<Generation> generationList = new ArrayList<Generation>();
	public ProjectSettings projectSettings = new ProjectSettings();

	public Project()
	{

	}


	public void setupInitalProject()
	{
		Generation g = new Generation();
		generationList.add(g);

		for (int i = 0; i < projectSettings.numberOfGeneratedImages; i++)
		{
			LayeredImage img = ImageFactory.createImage( projectSettings.depthOfImages  );
			g.imageList.add(img);
		}
		
		//LayeredImage img = ImageFactory.createDoubledTiledImage(4);
		//g.imageList.add(img);


	}


	public void load(File file) throws IOException
	{
		XStream xstream = createXStream();

		BufferedReader br = new BufferedReader(new FileReader(file));

		Project p = (Project) xstream.fromXML(br);
		this.generationList = p.generationList;

		br.close();

	}


	private XStream createXStream()
	{
		XStream xstream = new XStream(new DomDriver());
		Annotations.configureAliases(xstream, Project.class);
		Annotations.configureAliases(xstream, Generation.class);
		Annotations.configureAliases(xstream, LayeredImage.class);
		Annotations.configureAliases(xstream, ProjectSettings.class);
		return xstream;
	}


	public void save(File file) throws IOException
	{
		XStream xstream = createXStream();

		BufferedWriter br = new BufferedWriter(new FileWriter(file));

		xstream.toXML(this, br);

		br.close();
	}


	public void makeNextGeneration() throws Exception
	{
		Generation lastGeneration = getLastGeneration();
		
		ArrayList<LayeredImage> selected = lastGeneration.getSelected();
		if( selected.isEmpty())
		{
			throw new Exception("No images Selected");
		}
		
		lastGeneration.imageList = selected;
		
		Generation newGeneration = lastGeneration.createNextGeneration( projectSettings );
		
		generationList.add( newGeneration );
	}


	public Generation getLastGeneration()
	{
		return generationList.get(generationList.size()-1);
	}

}
