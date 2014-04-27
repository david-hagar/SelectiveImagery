package com.davidhagar.selectiveimagery.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ProjectSettings")
public class ProjectSettings
{
	public int numberOfGeneratedImages = 40;
	public int depthOfImages = 7;
	public float mutabilityChange = 1.5f;
}
