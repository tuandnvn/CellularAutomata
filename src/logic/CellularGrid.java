package logic;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Vector;

import util.StopException;
import util.Util;

/**
 * 
 * @author Tuan Do
 * 
 */
public class CellularGrid implements Serializable {
	/**
	 * auto-generated
	 */
	private static final long serialVersionUID = 6905043205676354916L;

	/**
	 * 
	 * @author Tuan Do
	 * 
	 */
	public class CellStructure implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private byte value;

		public byte getValue() {
			return value;
		}

		private byte old_value;
		private byte neighbors[];
		private Integer2D pos;

		/**
		 * Constructor
		 * 
		 * @param pos
		 *            Integer2D position in the grid
		 * @param value
		 *            byte value
		 * @param neighbors
		 *            byte[] an array indicates the number of neighbors having
		 *            the same color
		 */
		CellStructure(Integer2D pos, byte value, byte[] neighbors) {
			this.pos = pos;
			this.value = value;
			this.neighbors = new byte[neighbors.length];
			for (int i = 0; i < neighbors.length; i++) {
				this.neighbors[i] = neighbors[i];
			}
		}

		public void setValue(byte value) {
			this.value = value;
		}

		public void setOldValue(byte value) {
			this.old_value = value;
		}

		public String toString() {
			String s = "Cell " + this.pos.first + "," + this.pos.second + ": "
					+ value + " \nNeighbors ";
			for (int i = 0; i < neighbors.length; i++) {
				s += i + " " + neighbors[i] + ";";
			}
			s += "\n";
			return s;
		}
	}

	/*
	 * An 2-dimensional array to hold the values of the cells
	 */
	private CellStructure[][] cellularGrid;
	/*
	 * Initially I set up the grid that could be rectangular
	 */
	private int width;
	private int height;
	/*
	 * Keep track of which cells need to be checked at next update
	 */
	private HashSet<CellStructure> unchecked_cells = new HashSet<CellStructure>();

	public static enum Neighbor {
		Moore, Newmann
	};

	public static enum Boundary {
		Linear, Toroid
	};

	public static enum Game {
		Game_Of_Life, Food_Chain
	};

	private Neighbor neighbor_type;
	private Boundary boundary_type;
	private Game game_type;
	int[] density = new int[3];

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int[] getDensity() {
		return density;
	}

	public void setDensity(int[] density) {
		this.density = density;
	}

	public CellStructure[][] getCellularGrid() {
		return cellularGrid;
	}

	public Neighbor getNeighborType() {
		return neighbor_type;
	}

	public Boundary getBoundaryType() {
		return boundary_type;
	}

	public Game getGameType() {
		return game_type;
	}

	public void setOptions(Boundary boundary_type, Neighbor neighbor_type,
			Game game_type) {
		this.boundary_type = boundary_type;
		this.neighbor_type = neighbor_type;
		this.game_type = game_type;
	}

	/**
	 * Constructor of a logical component CellularGrid
	 * 
	 * @param height
	 *            int
	 * @param width
	 *            int
	 * @param neighbor_type
	 *            CellularGrid.Neighbor
	 * @param boundary_type
	 *            CellularGrid.Boundary
	 * @param game_type
	 *            CellularGrid.Game
	 */
	public CellularGrid(int height, int width, Neighbor neighbor_type,
			Boundary boundary_type, Game game_type) {
		this.height = height;
		this.width = width;
		if (boundary_type == Boundary.Linear) {
			/*
			 * Expand the grid to make thing "disapper" at the corner
			 */
			this.height = 3 * height;
			this.width = 3 * width;
		}

		this.neighbor_type = neighbor_type;
		this.boundary_type = boundary_type;
		this.game_type = game_type;

		/*
		 * set the value of density at the beginning we only care about the
		 * density in the inner grid
		 */
		this.density[0] = height * width;
		this.density[1] = this.density[2] = 0;

		this.cellularGrid = this.setUpZeroCells(this.height, this.width,
				this.game_type, this.neighbor_type);
	}

	/**
	 * Set the value of a GUI cell (not including the buffer cells) Should only
	 * be called from GUI unit
	 * 
	 * @param i
	 *            int the x_coordinate of the cell in GUI
	 * @param j
	 *            int the y_coordinate of the cell in GUI
	 * @param value
	 */
	public void setDisplayCell(int i, int j, byte value) {
		int i_, j_;
		if (this.boundary_type == Boundary.Linear) {
			i_ = i + this.height / 3;
			j_ = j + this.width / 3;
		} else {
			i_ = i;
			j_ = j;
		}
		byte old_value = this.cellularGrid[i_][j_].value;
		this.cellularGrid[i_][j_].setValue((byte) value);

		density[old_value]--;
		density[value]++;
		update_neighbors(unchecked_cells, i_, j_, old_value, value);
		unchecked_cells.add(this.cellularGrid[i_][j_]);
	}

	/**
	 * Update neighbors array in CellStructure of all neighbors of the cell at
	 * (i,j) That is when there's update at (i,j), its neighbors need to be
	 * updated as well
	 * 
	 * @param neighbor_cells
	 *            HashSet<CellStructure> storing updated_cells for next time
	 *            updating
	 * @param i
	 *            int x_coordinate of current cell
	 * @param j
	 *            int y_coordinate of current cell
	 * @param old_value
	 *            byte old value of current cell
	 * @param new_value
	 *            byte new value of current cell
	 */
	public void update_neighbors(HashSet<CellStructure> neighbor_cells, int i,
			int j, byte old_value, byte new_value) {
		/*
		 * Generate neighbors of the cell at (i,j)
		 */
		Vector<Integer2D> neighbors = generateNeighbors(i, j);
		for (Integer2D neighbor : neighbors) {
			neighbor_cells
					.add(this.cellularGrid[neighbor.first][neighbor.second]);

			/*
			 * update neighbors properties of neighbors
			 */
			this.cellularGrid[neighbor.first][neighbor.second].neighbors[old_value]--;
			this.cellularGrid[neighbor.first][neighbor.second].neighbors[new_value]++;
		}
	}

	public Vector<Integer2D> update() throws StopException {
		boolean stop_flag = true;
		/*
		 * tempo_unchecked_cells is filled in each update and set as
		 * unchecked_cells at the end
		 */
		HashSet<CellStructure> tempo_unchecked_cells = new HashSet<CellStructure>();
		/*
		 * only some cells need to be updated at the GUI (excluding buffer
		 * cells) gui_updating_cells is subset of changed_cells
		 */
		Vector<Integer2D> gui_updating_cells = new Vector<Integer2D>();
		/*
		 * cells need to be updated logically
		 */
		Vector<CellStructure> changed_cells = new Vector<CellStructure>();

		/*
		 * HashSet to Array
		 */
		CellStructure[] t = new CellStructure[unchecked_cells.size()];
		unchecked_cells.toArray(t);

		for (int i = 0; i < t.length; i++) {
			CellStructure cell = t[i];

			/*
			 * Check if this cell need to be updated in GUI could use if (this
			 * in gui_updating_cells) as well
			 */
			if (this.boundary_type == Boundary.Linear) {
				if (Util.checkInside(cell.pos.first - this.height / 3,
						cell.pos.second - this.width / 3, this.height / 3,
						this.width / 3))
					stop_flag = false;
			} else {
				stop_flag = false;
			}
			cell.setOldValue(cell.value);
			if (this.game_type == Game.Game_Of_Life) {
				if (cell.value == 0) {
					if (cell.neighbors[1] == 3) {
						cell.value = 1;
					}
				} else {
					if (cell.neighbors[1] < 2 || cell.neighbors[1] > 3) {
						cell.value = 0;
					}
				}
			}
			if (this.game_type == Game.Food_Chain) {
				if (cell.value == 0) {
					if (cell.neighbors[1] >= 3) {
						cell.value = 1;
					}
				} else if (cell.value == 1) {
					if (cell.neighbors[2] >= 3) {
						cell.value = 2;
					}
				} else {
					if (cell.neighbors[0] >= 3) {
						cell.value = 0;
					}
				}
			}

			/*
			 * If there is a change in value of the cell
			 */
			if (cell.value != cell.old_value) {
				changed_cells
						.add(cellularGrid[cell.pos.first][cell.pos.second]);
				density[cell.old_value]--;
				density[cell.value]++;

				/*
				 * Some of the changed_cells need to be updated at GUI
				 */
				if (this.boundary_type == Boundary.Linear) {
					if (Util.checkInside(cell.pos.first - this.height / 3,
							cell.pos.second - this.width / 3, this.height / 3,
							this.width / 3)) {
						gui_updating_cells.add(new Integer2D(cell.pos.first
								- this.height / 3, cell.pos.second - this.width
								/ 3));
					}
				} else {
					gui_updating_cells.add(cell.pos);
				}
			}
		}

		/*
		 * Update the neighbors for all changed logical cells
		 */
		for (CellStructure cell : changed_cells) {
			update_neighbors(tempo_unchecked_cells, cell.pos.first,
					cell.pos.second, cell.old_value, cell.value);
		}
		unchecked_cells = tempo_unchecked_cells;

		if (stop_flag) {
			throw new StopException();
		}
		return gui_updating_cells;
	}

	/**
	 * Generate all neighbors of the cell at (i,j)
	 * 
	 * @param i
	 * @param j
	 * @return Vector<Integer2D> vector of neighbor positions
	 */
	private Vector<Integer2D> generateNeighbors(int i, int j) {
		Vector<Integer2D> neighbors = new Vector<Integer2D>();
		for (int h_c = -1; h_c <= 1; h_c++)
			for (int w_c = -1; w_c <= 1; w_c++) {
				if (h_c == 0 && w_c == 0) {
					continue;
				}
				if (this.neighbor_type == Neighbor.Newmann
						&& h_c + w_c % 2 == 0)
					continue;

				Integer2D neighbor = generateNeighbor(i, j, h_c, w_c);
				if (neighbor != null)
					neighbors.add(neighbor);
			}

		return neighbors;
	}

	/**
	 * Generate a neighbors of the cell at (i+h_c,j+w_c)
	 * 
	 * @param i
	 * @param j
	 * @param h_c
	 * @param w_c
	 * @return Integer2D neighbor position
	 */
	private Integer2D generateNeighbor(int i, int j, int h_c, int w_c) {
		if (this.boundary_type == Boundary.Linear) {
			if (!Util.checkInside(i + h_c, j + w_c, this.height, this.width))
				return null;
			return new Integer2D(i + h_c, j + w_c);
		} else if (this.boundary_type == Boundary.Toroid) {
			return new Integer2D((i + h_c + this.height) % this.height, (j
					+ w_c + this.width)
					% this.width);
		}
		return null;
	}

	/**
	 * @deprecated
	 * Switch between two boundary types
	 */
	public void switchBoundaryType() {
		if (this.boundary_type == Boundary.Linear) {
			this.height *= 3;
			this.width *= 3;
			CellStructure[][] temporaryCellularGrid = setUpZeroCells(
					this.height, this.width, this.game_type, this.neighbor_type);

			try {
				Util.copyArray(this.cellularGrid, 0, 0, temporaryCellularGrid,
						height, width, height, width);
			} catch (Exception e) {
				return;
			}

			this.cellularGrid = temporaryCellularGrid;
		} else if (this.boundary_type == Boundary.Toroid) {
			this.height /= 3;
			this.width /= 3;
			CellStructure[][] temporaryCellularGrid = setUpZeroCells(
					this.height, this.width, this.game_type, this.neighbor_type);

			try {
				Util.copyArray(this.cellularGrid, height, width,
						temporaryCellularGrid, 0, 0, height, width);
			} catch (Exception e) {
				return;
			}

			this.cellularGrid = temporaryCellularGrid;
		}
	}

	/**
	 * 
	 * @param _height
	 * @param _width
	 * @param _game_type
	 * @param _neighbor_type
	 * @return
	 */
	private CellStructure[][] setUpZeroCells(int _height, int _width,
			Game _game_type, Neighbor _neighbor_type) {
		CellStructure[][] grid = new CellStructure[_height][_width];
		for (int i = 0; i < _height; i++)
			for (int j = 0; j < _width; j++) {
				grid[i][j] = new CellStructure(new Integer2D(i, j), (byte) 0,
						Util.getNZero(_game_type == Game.Game_Of_Life ? 2 : 3));
				if (_neighbor_type == Neighbor.Moore) {
					grid[i][j].neighbors[0] = 8;
				} else if (_neighbor_type == Neighbor.Newmann) {
					grid[i][j].neighbors[0] = 4;
				}
			}

		return grid;
	}

}
