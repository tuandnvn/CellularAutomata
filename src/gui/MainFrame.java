package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import logic.CellularGrid;
import logic.CellularGrid.CellStructure;
import logic.Integer2D;
import util.StopException;

/**
 * 
 * @author Tuan Do
 * 
 */
public class MainFrame extends JFrame {

	/**
	 * auto-generated
	 */
	private static final long serialVersionUID = -416526919007280138L;

	/**
	 * Some GUI components
	 */
	private JPanel contentPane;
	private MainFrame main_frame;
	private ButtonGrid gridPanel = null;
	ChartDrawer chart;
	final JButton btnPlay = new JButton("Run");
	final JButton btnPause = new JButton("Pause");
	final JButton btnChart = new JButton("Chart");

	/**
	 * The logical component of cellular automata
	 */
	private CellularGrid grid;

	/**
	 * Global counter keep track of the current step
	 */
	private static int counter = 0;

	/**
	 * Global flag to keep track whether the run converge to stable state
	 */
	boolean stop_flag = false;
	/**
	 * Global value (in %) to control the speed and zoom
	 */
	int currentSpeed = 100;
	int currentZoom = 100;
	static int defaut_update_time = 200;
	String[] speed_possibilities = { "50%", "100%", "200%", "400%", "800%",
			"1600%" };
	String[] zoom_possibilities = { "25%", "50%", "75%", "100%", "150%", "200%" };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.setProperty("java.util.Arrays.useLegacyMergeSort",
							"true");
					try {
						UIManager.setLookAndFeel(UIManager
								.getCrossPlatformLookAndFeelClassName());
					} catch (UnsupportedLookAndFeelException e) {
						// handle exception
					} catch (ClassNotFoundException e) {
						// handle exception
					} catch (InstantiationException e) {
						// handle exception
					} catch (IllegalAccessException e) {
						// handle exception
					}
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * A subroutine to remove the current grid
	 */
	void removeGrid() {
		if (gridPanel != null) {
			contentPane.remove(gridPanel);
		}
	}

	/**
	 * A subroutine to add a new grid
	 * 
	 * @param dimension 
	 */
	void addGrid(int dimension) {
		chart = new ChartDrawer("Density", this);
		gridPanel = new ButtonGrid(main_frame, dimension);
		gridPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null,
				null, null));
		contentPane.add(gridPanel, BorderLayout.WEST);
		contentPane.validate();
		contentPane.repaint();
		grid = new CellularGrid(dimension, dimension, this.neighbor_type,
				this.boundary_type, this.game_type);
		linkGridComponents(gridPanel, grid);
		main_frame.pack();
		initiateButtonState();
	}

	/**
	 * A subroutine to remove the old grid, and create a new one having the same
	 * dimension
	 */
	void restartGrid() {
		if (gridPanel == null) {
			return;
		}

		int dimension = gridPanel.getGridHeight();
		removeGrid();
		addGrid(dimension);
	}

	/**
	 * Initiate interactive button.
	 * Initially, Play button is enabled, while Pause
	 * and Chart are not
	 */
	public void initiateButtonState() {
		btnPlay.setEnabled(true);
		btnPause.setEnabled(false);
		btnChart.setEnabled(false);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		main_frame = this;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");

		/**
		 * Action performed: create a new grid
		 */
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = (String) JOptionPane.showInputDialog(main_frame,
						"Input dimension", "New Cellular Automata",
						JOptionPane.PLAIN_MESSAGE);
				try {
					int dimension = Integer.parseInt(s);
					counter = 0;
					removeGrid();
					addGrid(dimension);
				} catch (NumberFormatException e) {
					removeGrid();
				}
			}
		});
		mnFile.add(mntmNew);

		JMenuItem mntmLoad = new JMenuItem("Load");
		/**
		 * Action performed: load a grid from a .cel file
		 */
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final String dir = System.getProperty("user.dir");
				final JFileChooser fc = new JFileChooser(dir);
				fc.setFileFilter(new FileNameExtensionFilter("Cellular Files",
						"cel"));
				int returnVal = fc.showOpenDialog(main_frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					ObjectInputStream ois;
					File file = fc.getSelectedFile();
					try {
						FileInputStream fin = new FileInputStream(file);
						ois = new ObjectInputStream(fin);
						main_frame.grid = (CellularGrid) ois.readObject();
						resetGridPanel();
						ois.close();
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(main_frame,
								"File couldn't be read", "IO Exception",
								JOptionPane.ERROR_MESSAGE);
						ioe.printStackTrace();
					} catch (ClassNotFoundException e) {
						JOptionPane.showMessageDialog(main_frame,
								"Couldn't cast the object into Grid object",
								"Serialization Exception",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}
		});
		mnFile.add(mntmLoad);

		JMenuItem mntmSave = new JMenuItem("Save");
		/**
		 * Action performed: save a grid to a .cel file
		 */
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String dir = System.getProperty("user.dir");
				final JFileChooser fc = new JFileChooser(dir);
				fc.setFileFilter(new FileNameExtensionFilter("Cellular Files",
						"cel"));
				int returnVal = fc.showOpenDialog(main_frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String file_name = file.toString();
					if (!file_name.endsWith(".cel")) {
						file_name += ".cel";
					}

					ObjectOutputStream oos;
					try {
						FileOutputStream fout = new FileOutputStream(new File(
								file_name));
						oos = new ObjectOutputStream(fout);
						oos.writeObject(grid);
						oos.close();
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(main_frame,
								"File couldn't be written", "IO Exception",
								JOptionPane.ERROR_MESSAGE);
						ioe.printStackTrace();
					}
				}
			}
		});
		mnFile.add(mntmSave);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mnFile.add(mntmQuit);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		/**
		 * Action performed: load the configuration frame
		 */
		mntmConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OptionFrame of = new OptionFrame(main_frame);
				of.setVisible(true);
			}
		});
		mnOptions.add(mntmConfiguration);

		JMenuItem mntmSpeed = new JMenuItem("Speed");
		/**
		 * Action performed: change the speed of running
		 */
		mntmSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(main_frame, "",
						"Change speed", JOptionPane.PLAIN_MESSAGE, null,
						speed_possibilities, currentSpeed + "%");

				// If a string was returned, say so.
				if ((s != null) && (s.length() > 0)) {
					currentSpeed = Integer.parseInt(s.substring(0,
							s.length() - 1));
					return;
				}
			}
		});
		mnOptions.add(mntmSpeed);

		JMenuItem mntmZoom = new JMenuItem("Zoom");
		/**
		 * Action performed: zoom in and out
		 */
		mntmZoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(main_frame, "",
						"Zoom", JOptionPane.PLAIN_MESSAGE, null,
						zoom_possibilities, currentZoom + "%");

				// If a string was returned, say so.
				if ((s != null) && (s.length() > 0)) {
					currentZoom = Integer.parseInt(s.substring(0,
							s.length() - 1));
					gridPanel.changeZoomLevel(currentZoom);
					gridPanel.revalidate();
					gridPanel.repaint();
					main_frame.pack();
					return;
				}
			}
		});
		mnOptions.add(mntmZoom);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		JMenuItem mntmDocument = new JMenuItem("Document");
		mnAbout.add(mntmDocument);

		JMenuItem mntmHelp = new JMenuItem("About");
		mnAbout.add(mntmHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_2.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null,
				null));
		contentPane.add(panel_2, BorderLayout.NORTH);

		JToolBar toolBar = new JToolBar();
		panel_2.add(toolBar);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null,
				null));
		toolBar.add(panel_1);

		panel_1.add(btnPlay);
		btnPlay.setMnemonic('R');
		btnPlay.setIcon(new ImageIcon(MainFrame.class
				.getResource("/icons/Play-Green-Button-icon.png")));
		/**
		 * Action performed: click the Play button
		 */
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnPlay.setEnabled(false);
				btnPause.setEnabled(true);
				btnChart.setEnabled(true);
				/*
				 * set stop_flag to false at the beginning of run
				 */
				stop_flag = false;
				final Runnable update = new Runnable() {
					public void run() {
						try {
							/*
							 * Each update, the logical grid updates return a
							 * list of changing cells The GUI component update
							 * the color of cells
							 */
							Vector<Integer2D> change_cells = grid.update();
							for (Integer2D cell : change_cells) {
								gridPanel.changeNextColor(cell.first,
										cell.second);
							}
							gridPanel.revalidate();
							gridPanel.repaint();
						} catch (StopException e) {
							stop_flag = true;
						}
					}
				};

				Thread appThread = new Thread() {
					public void run() {
						/*
						 * First disable changing the grid panel
						 */
						gridPanel.changeDisable();

						while (!stop_flag) {
							/*
							 * Chart updating
							 */
							int[] density = main_frame.grid.getDensity();
							for (int i = 0; i < (main_frame.game_type == CellularGrid.Game.Game_Of_Life ? 2
									: 3); i++) {
								chart.addDataPoint(density[i], i, counter);
							}
							counter++;

							/*
							 * Sleep the thread before the next updating
							 */
							try {
								Thread.sleep((int) ((float) defaut_update_time
										/ currentSpeed * 100));
								SwingUtilities.invokeLater(update);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						btnPause.setEnabled(false);
						btnPlay.setEnabled(true);
						btnChart.setEnabled(true);
					}
				};
				appThread.start();
			}
		});

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null,
				null));
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setAlignOnBaseline(true);
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		toolBar.add(panel_3);

		btnPause.setMnemonic('P');
		btnPause.setEnabled(false);
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnPause.setEnabled(false);
				btnPlay.setEnabled(true);
				btnChart.setEnabled(true);
				gridPanel.changeEnable();
				stop_flag = true;
			}
		});
		panel_3.add(btnPause);
		btnPause.setIcon(new ImageIcon(MainFrame.class
				.getResource("/icons/Pause-Green-Button-icon.png")));

		btnChart.setEnabled(false);
		btnChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main_frame.chart.showChart();
				// Stop as well
				stop_flag = true;
			}
		});
		btnChart.setIcon(new ImageIcon(MainFrame.class
				.getResource("/icons/bar-chart-icon.png")));
		panel_3.add(btnChart);
		main_frame.pack();
	}

	/**
	 * Keep track of the configurations
	 */
	CellularGrid.Boundary boundary_type = CellularGrid.Boundary.Linear;
	CellularGrid.Neighbor neighbor_type = CellularGrid.Neighbor.Moore;
	CellularGrid.Game game_type = CellularGrid.Game.Game_Of_Life;

	/**
	 * Set configurations for logical component
	 * 
	 * @param boundary_type
	 *            
	 * @param neighbor_type
	 *            CellularGrid.Neighbor
	 * @param game_type
	 *            CellularGrid.Game
	 */
	public void setOptions(CellularGrid.Boundary boundary_type,
			CellularGrid.Neighbor neighbor_type, CellularGrid.Game game_type) {
		this.boundary_type = boundary_type;
		this.neighbor_type = neighbor_type;
		this.game_type = game_type;
		if (this.grid != null) {
			this.grid.setOptions(boundary_type, neighbor_type, game_type);
			/*
			 * Initially I allow the changing of configuration without creating
			 * a new grid
			 */
			/*
			 * if (change_boundary_grid){ this.grid.switchBoundaryType(); }
			 */
		}
	}

	/**
	 * Linking the GUI grid panel and its corresponding logical component
	 * CellularGrid
	 * 
	 * @param gridPanel
	 * @param grid
	 */
	public void linkGridComponents(ButtonGrid gridPanel, CellularGrid grid) {
		gridPanel.setLogicComponent(grid);
	}

	/**
	 * Reset the GUI grid panel if there is a loading of logical component
	 */
	private void resetGridPanel() {
		this.setOptions(this.grid.getBoundaryType(),
				this.grid.getNeighborType(), this.grid.getGameType());
		CellStructure[][] logic_cells = this.grid.getCellularGrid();
		removeGrid();
		counter = 0;
		if (this.grid.getBoundaryType() == CellularGrid.Boundary.Linear) {
			addGrid(this.grid.getHeight() / 3);
			for (int i = 0; i < this.grid.getHeight() / 3; i++)
				for (int j = 0; j < this.grid.getHeight() / 3; j++) {
					for (int t = 0; t < logic_cells[i + this.grid.getHeight()
							/ 3][j + this.grid.getHeight() / 3].getValue(); t++) {
						this.gridPanel.changeNextColor(i, j);
					}
				}
		} else if (this.grid.getBoundaryType() == CellularGrid.Boundary.Toroid) {
			addGrid(this.grid.getHeight());
			for (int i = 0; i < this.grid.getHeight(); i++)
				for (int j = 0; j < this.grid.getHeight(); j++) {
					for (int t = 0; t < logic_cells[i][j].getValue(); t++) {
						this.gridPanel.changeNextColor(i, j);
					}
				}
		}

	}
}
