package com.nyu.bds.assignment2;

import java.awt.Color;

import javax.swing.JFrame;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.ui.InteractivePanel;

public class Visualizer {
	

	
	
	public Visualizer(double[][] conceptsArr, int[] conceptClusters, String[] clusterNames) {	
		LinePlotTest frame = new LinePlotTest(conceptsArr, conceptClusters, clusterNames);
        frame.setVisible(true);
	}
	
	
	
	final class ClusteringDataTable extends DataTable {
		public ClusteringDataTable(Class<? extends Comparable<?>>... data) {
			super(data);
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.getName();
		}	
	}
	
	final class LinePlotTest extends JFrame {
	    public LinePlotTest(double[][] concepts, int[] conceptClusters, String[] clusterNames) {
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setSize(600, 400);
	        DataTable[] dataTables = new DataTable[3];
	        for (int i = 0; i < dataTables.length; i++) {
	        	dataTables[i] = new ClusteringDataTable(Double.class, Double.class);
	        	dataTables[i].setName(clusterNames[i]);
			}
	      
			for (int i=0; i < concepts[0].length; i++) {
				dataTables[conceptClusters[i]].add(concepts[0][i], concepts[1][i]);
			}
	        XYPlot plot = new XYPlot(dataTables[0], dataTables[1], dataTables[2]);
	        getContentPane().add(new InteractivePanel(plot));
	        plot.getPointRenderers(dataTables[0]).get(0).setColor(new Color(0.0f, 0.3f, 1.0f));
	        plot.getPointRenderers(dataTables[1]).get(0).setColor(new Color(1.0f, 0.3f, 0.0f));
	        plot.getPointRenderers(dataTables[2]).get(0).setColor(new Color(0.0f, 1.0f, 0.0f));
	        plot.setLegendVisible(true);
	    }

	}
	

}
