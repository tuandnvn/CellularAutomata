package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import logic.CellularGrid;

public class ButtonGrid extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6263096940253239491L;

	private static int default_cell_size = 16;
	private int grid_height;
	private int grid_width;

	public int getGridHeight() {
		return grid_height;
	}

	public void setGridHeight(int grid_height) {
		this.grid_height = grid_height;
	}

	public int getGridWidth() {
		return grid_width;
	}

	public void setGridWidth(int grid_width) {
		this.grid_width = grid_width;
	}

	private JButton[][] buttons;
	private static final int default_level = 3;
	static Color[] default_binary_colors = { new Color(240, 240, 240),
			Color.DARK_GRAY };
	static Color[] default_trinary_colors = { new Color(240, 240, 240),
			Color.CYAN, Color.YELLOW };
	private MainFrame main_frame;
	private CellularGrid logic_component;
	private boolean change_enabled = true;
	int button_size = 16;

	public ButtonGrid(MainFrame main_frame, int dimension, int zoom_level) {
		this(main_frame, dimension, dimension, zoom_level);
	}

	public ButtonGrid(MainFrame main_frame, int dimension) {
		this(main_frame, dimension, dimension, default_level);
	}

	public void setLogicComponent(CellularGrid logic_component) {
		this.logic_component = logic_component;
	}

	public void changeDisable() {
		change_enabled = false;
	}

	public void changeEnable() {
		change_enabled = true;
	}

	private ButtonGrid(MainFrame main_frame, int height, int width,
			int zoom_level) {
		this.main_frame = main_frame;
		this.grid_width = width;
		this.grid_height = height;
		this.buttons = new JButton[height][width];
		this.setLayout(new GridLayout(width, height));
		for (int i = 0; i < this.grid_height; i++)
			for (int j = 0; j < this.grid_width; j++) {
				buttons[i][j] = new JButton();
				final int f_i = i;
				final int f_j = j;
				buttons[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (change_enabled)
							changeNextColor(f_i, f_j);
					}
				});
				buttons[i][j].setBackground(new Color(240, 240, 240));
				buttons[i][j].setBorder(BorderFactory.createLineBorder(
						new Color(230, 230, 230), 1));
				buttons[i][j].setPreferredSize(new Dimension(button_size,
						button_size));

				this.add(buttons[i][j]);
			}

		setPreferredSize(new Dimension((int) (button_size * width),
				(int) (button_size * height)));
	}

	/**
	 * 
	 * @param x
	 *            int x_coordinate
	 * @param y
	 *            int y_coordinate
	 * @param color
	 *            java.awt.Color
	 */
	protected void setColor(int x, int y, Color color) {
		buttons[x][y].setBackground(color);
	}

	/**
	 * 
	 * @param x
	 *            int x_coordinate
	 * @param y
	 *            int y_coordinate
	 */
	public void changeNextColor(int x, int y) {
		Color current_color = buttons[x][y].getBackground();
		if (main_frame.game_type == CellularGrid.Game.Game_Of_Life) {
			for (int i = 0; i < 2; i++) {
				if (current_color.equals(default_binary_colors[i])) {
					setColor(x, y, default_binary_colors[(i + 1) % 2]);
					this.logic_component.setDisplayCell(x, y, (byte) ((i + 1) % 2));
					break;
				}
			}
		}
		if (main_frame.game_type == CellularGrid.Game.Food_Chain) {
			for (int i = 0; i < 3; i++) {
				if (current_color.equals(default_trinary_colors[i])) {
					setColor(x, y, default_trinary_colors[(i + 1) % 3]);
					this.logic_component.setDisplayCell(x, y, (byte) ((i + 1) % 3));
					break;
				}
			}
		}
	}

	/**
	 * Zoom in and zoom out 
	 * @param level
	 */
	public void changeZoomLevel(int level) {
		this.button_size = (int) (default_cell_size * level / 100);
		for (int i = 0; i < this.grid_height; i++)
			for (int j = 0; j < this.grid_width; j++) {
				buttons[i][j].setPreferredSize(new Dimension(this.button_size,
						this.button_size));
			}
		setPreferredSize(new Dimension((int) (this.button_size * grid_width),
				(int) (this.button_size * grid_height)));
	}
}
