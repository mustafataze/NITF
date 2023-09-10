package org.geotools.NitfProject;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.swing.JMapPane;

//Define a class that extends JPanel and implements Runnable
public class NitfViewer extends JPanel implements Runnable {

private File nitfFile; // The nitf file to display
private JMapPane mapPane; // The map pane to show the image
private MapContent mapContent; // The map content to hold the grid coverage

// Define a constructor that takes a nitf file as an argument
public NitfViewer(File nitfFile) {
 this.nitfFile = nitfFile;
 this.mapPane = new JMapPane();
 this.mapContent = new MapContent();
 this.mapPane.setMapContent(mapContent);
 this.add(mapPane); // Add the map pane to the panel
}

// Define a method that reads the grid coverage from the nitf file
public GridCoverage2D readGridCoverage() throws IOException {
 // Find the format of the nitf file
 AbstractGridFormat format = GridFormatFinder.findFormat(nitfFile);
 // Create a reader for the nitf file
 GeoTiffReader reader = new GeoTiffReader(nitfFile, format.getReadHints());  //!!!!(error?)
 // Read the grid coverage from the reader
 GridCoverage2D coverage = reader.read(null);
 
 reader.dispose();
 
 return coverage;
}

// Define a method that adds the grid coverage to the map content
public void addGridCoverageToMap(GridCoverage2D coverage) {
 // Create a layer for the grid coverage
 GridCoverageLayer layer = new GridCoverageLayer(coverage, null, null);
 // Add the layer to the map content
 mapContent.addLayer(layer);
}

// Define a method that sets up the renderer for the map pane
public void setupRenderer() {
 // Create a renderer for the map pane
 StreamingRenderer renderer = new StreamingRenderer();
 // Set the map content for the renderer
 renderer.setMapContent(mapContent);
 // Set the renderer for the map pane
 mapPane.setRenderer(renderer);
}

// Override the run method of Runnable interface
@Override
public void run() {
 try {
   // Read the grid coverage from the nitf file
   GridCoverage2D coverage = readGridCoverage();
   // Add the grid coverage to the map content
   addGridCoverageToMap(coverage);
   // Set up the renderer for the map pane
   setupRenderer();

   mapPane.repaint();
 } catch (IOException e) {
   e.printStackTrace();
 }
}
}

class Main {

public static void main(String[] args) {
 // Create a nitf file object
 File nitfFile = new File("example.nitf"); //address path
 // Create a nitf viewer object with the nitf file
 NitfViewer viewer = new NitfViewer(nitfFile);
 // Create a frame to hold the viewer panel
 JFrame frame = new JFrame("Nitf Viewer");
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.add(viewer); // Add the viewer panel to the frame
 frame.setSize(800, 600); // Set the size of the frame
 frame.setVisible(true); // Make the frame visible

 // Create a thread to run the viewer object
 Thread thread = new Thread(viewer);
 thread.start(); // 
	}
}
