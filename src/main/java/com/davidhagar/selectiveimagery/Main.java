package com.davidhagar.selectiveimagery;

import com.davidhagar.selectiveimagery.model.Project;
import com.davidhagar.selectiveimagery.ui.ProjectFrame;


/**
 * Hello world!
 * 
 */
public class Main
{


	public static void main(String[] args)
	{
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				Project p = new Project();
				p.setupInitalProject();
				
				new ProjectFrame(p).setVisible(true);
			}
		});

	}
}
