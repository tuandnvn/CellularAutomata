package gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import logic.CellularGrid;
import logic.CellularGrid.Game;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
/**
 * 
 * @author Tuan Do
 *	adapt from an example of JFreeChart
 */
public class ChartDrawer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3546735305414658930L;

	MainFrame mainFrame;
	String series[] = new String[3];
	CategoryDataset dataset;

	public ChartDrawer(String title, MainFrame mainFrame) {
		super(title);
		this.mainFrame = mainFrame;
		this.dataset = createDefaultDataset(mainFrame.game_type);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		System.out.println(this.getDefaultCloseOperation());
	}
	
	/**
	 * create a chart given the current data
	 * @return JFreeChart
	 */
	public JFreeChart createChart() {
		// create the chart...
		final JFreeChart chart = ChartFactory.createLineChart("Density chart", // chart
																				// title
				"Counter", // domain axis label
				"Value", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		chart.setBackgroundPaint(new Color(0xBBBBDD));
		CategoryPlot plot = chart.getCategoryPlot();
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// disable bar outlines...
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		renderer.setDrawOutlines(false);
		// set up gradient paints for series...
		GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f,
				0.0f, Color.lightGray);
		GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f,
				0.0f, Color.lightGray);
		GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f,
				0.0f, Color.lightGray);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);
		return chart;
	}
	
	/**
	 * show chart as a Panel
	 */
	public void showChart() {
		JFreeChart chart = createChart();
		// add the chart to a panel...
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		RefineryUtilities.centerFrameOnScreen(this);
		this.pack();
		setVisible(true);
	}
	
	/**
	 * save chart to a file
	 * @param filename String
	 */
	public void saveChart(String filename) {
		JFreeChart chart = createChart();
		try {
			ChartUtilities.saveChartAsJPEG(new File(filename), chart, 500,
					270);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create series for different states (colors) of the game
	 * @param game_type
	 * @return CategoryDataset
	 */
	private CategoryDataset createDefaultDataset(CellularGrid.Game game_type) {
		if (game_type == Game.Game_Of_Life) {
			this.series[0] = "WHITE";
			this.series[1] = "BLACK";
		} else if (game_type == Game.Food_Chain) {
			this.series[0] = "WHITE";
			this.series[1] = "CYAN";
			this.series[2] = "YELLOW";
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		return dataset;
	}
	
	/**
	 * Add data point to a series
	 * @param value
	 * @param series_index
	 * @param counter
	 */
	public void addDataPoint(int value, int series_index, int counter) {
		((DefaultCategoryDataset) this.dataset).addValue(value,
				series[series_index], "" + counter);
	}
}