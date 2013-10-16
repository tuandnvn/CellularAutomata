package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import logic.CellularGrid;
import logic.CellularGrid.Boundary;
import logic.CellularGrid.Game;
import logic.CellularGrid.Neighbor;

/**
 * 
 * @author Tuan Do
 * 
 */
public class OptionFrame extends JFrame {

	/**
	 * auto-generated
	 */
	private static final long serialVersionUID = -4710898292883526228L;
	private JPanel contentPane;
	private MainFrame mainFrame;
	private OptionFrame optionFrame;

	/**
	 * Create the frame.
	 */
	public OptionFrame(MainFrame _mainFrame) {
		mainFrame = _mainFrame;
		optionFrame = this;

		setTitle("Options");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 270, 210);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JLabel lblBorderOption = new JLabel("Border option");

		JLabel lblNeighborOption = new JLabel("Neighbor option");

		JLabel lblGameOption = new JLabel("Game option");

		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Linear", "Toroid" }));
		if (mainFrame.boundary_type == Boundary.Linear)
			comboBox.setSelectedIndex(0);
		else
			comboBox.setSelectedIndex(1);

		final JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Moore", "Von Neumann" }));
		if (mainFrame.neighbor_type == Neighbor.Moore)
			comboBox_1.setSelectedIndex(0);
		else
			comboBox_1.setSelectedIndex(1);

		final JComboBox<String> comboBox_2 = new JComboBox<String>();
		comboBox_2.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Game of Life", "Food chain" }));
		if (mainFrame.game_type == Game.Game_Of_Life)
			comboBox_2.setSelectedIndex(0);
		else
			comboBox_2.setSelectedIndex(1);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CellularGrid.Boundary boundary_type = comboBox
						.getSelectedIndex() == 0 ? CellularGrid.Boundary.Linear
						: CellularGrid.Boundary.Toroid;
				CellularGrid.Neighbor neighbor_type = comboBox_1
						.getSelectedIndex() == 0 ? CellularGrid.Neighbor.Moore
						: CellularGrid.Neighbor.Newmann;
				CellularGrid.Game game_type = comboBox_2.getSelectedIndex() == 0 ? CellularGrid.Game.Game_Of_Life
						: CellularGrid.Game.Food_Chain;
				CellularGrid.Game old_game_type = mainFrame.game_type;
				CellularGrid.Neighbor old_neighbor_type = mainFrame.neighbor_type;
				CellularGrid.Boundary old_boundary_type = mainFrame.boundary_type;
				mainFrame.setOptions(boundary_type, neighbor_type, game_type);
				if (old_game_type != game_type
						|| old_neighbor_type != neighbor_type
						|| old_boundary_type != boundary_type) {
					// Initiate the grid panel again
					mainFrame.restartGrid();
				}
				optionFrame.dispose();
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionFrame.dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addContainerGap()
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblNeighborOption)
																						.addComponent(
																								lblBorderOption)
																						.addComponent(
																								lblGameOption))
																		.addPreferredGap(
																				ComponentPlacement.RELATED,
																				26,
																				Short.MAX_VALUE))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				btnOk)
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)))
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(btnCancel)
														.addGroup(
																gl_contentPane
																		.createParallelGroup(
																				Alignment.LEADING,
																				false)
																		.addComponent(
																				comboBox_2,
																				0,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				comboBox,
																				0,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				comboBox_1,
																				0,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblBorderOption)
														.addComponent(
																comboBox,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblNeighborOption)
														.addComponent(
																comboBox_1,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblGameOption)
														.addComponent(
																comboBox_2,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED, 11,
												Short.MAX_VALUE)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(btnCancel)
														.addComponent(btnOk))));
		contentPane.setLayout(gl_contentPane);
	}
}
