package org.epochx.monitor.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.epochx.Config;
import org.epochx.MaximumGenerations;
import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.Listener;
import org.epochx.event.RunEvent.EndRun;

public class GraphHeader extends JPanel implements Runnable, Listener<Event>, ChangeListener, ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6921704322479981889L;

	private GraphViewModel viewModel;

	private JLabel lblProgress;
	private JProgressBar progressBar;
	
	private JLabel lblSortedBy;
	private JComboBox comparatorBox;
	
	private JLabel lblDiameter;
	private JSlider diameterSlider;
	
	private JLabel lblBond;
	private JCheckBox bondBox;
	private JSlider bondSlider;
	
	/**
	 * Create the panel.
	 */
	public GraphHeader(){
		this.viewModel = null;

		// setPreferredSize(new Dimension(600, 30));
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEADING);

		setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		
		

		lblProgress = new JLabel("Progress :");
		int max = Config.getInstance().get(MaximumGenerations.MAXIMUM_GENERATIONS) == null ? 0 : Config.getInstance().get(MaximumGenerations.MAXIMUM_GENERATIONS);
		progressBar = new JProgressBar(0, max);
		progressBar.setPreferredSize(new Dimension(100, 14));
		progressBar.setStringPainted(true);

		lblSortedBy = new JLabel("Sort by :");

		comparatorBox = new JComboBox();
		comparatorBox.addItem(GraphViewModel.FITNESS_COMPARATOR);
		comparatorBox.addItem(GraphViewModel.PARENT_COMPARATOR);
		comparatorBox.addItem(GraphViewModel.OPERATOR_COMPARATOR);
		comparatorBox.setSelectedIndex(-1);
		comparatorBox.addItemListener(this);
		comparatorBox.setPreferredSize(new Dimension(100, 20));

		lblDiameter = new JLabel("Zoom :");
		
		diameterSlider = new JSlider(1, 25);
		diameterSlider.setPreferredSize(new Dimension(70, 16));
		diameterSlider.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		diameterSlider.addChangeListener(this);


		lblBond = new JLabel("Bonds :");

		bondBox = new JCheckBox();
		bondBox.setPreferredSize(new Dimension(18, 18));
		bondBox.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		
		bondBox.addChangeListener(this);
		
		bondSlider = new JSlider(0, 100);
		bondSlider.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		bondSlider.addChangeListener(this);
		bondSlider.setPreferredSize(new Dimension(70, 16));
		
		

		EventManager.getInstance().add(EndGeneration.class, this);
		EventManager.getInstance().add(EndRun.class, this);

		try {
			SwingUtilities.invokeAndWait(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the panel.
	 */
	public GraphHeader(GraphViewModel viewModel) throws IllegalArgumentException {
		this();
		setViewModel(viewModel);
	}

	public void run() {
		removeAll();
		
		add(lblProgress);
		add(progressBar);
		add(lblSortedBy);
		add(comparatorBox);
		add(lblDiameter);
		add(diameterSlider);
		add(lblBond);
		add(bondBox);
		add(bondSlider);
		
		validate();
	}

	/**
	 * @return the viewModel
	 */
	public GraphViewModel getViewModel() {
		return viewModel;
	}

	/**
	 * @param viewModel the viewModel to set
	 */
	public void setViewModel(GraphViewModel viewModel) throws IllegalArgumentException {

		if (viewModel == null) {
			throw new IllegalArgumentException("The view model cannot be null.");
		}
		this.viewModel = viewModel;
		
		diameterSlider.setValue(viewModel.getDiameter() / 2);
		
		bondBox.setSelected(viewModel.isBondEnable());
		bondSlider.setValue(254 - viewModel.getBondColor().getBlue());
	}

	@SuppressWarnings("unchecked")
	public void itemStateChanged(ItemEvent e) throws NullPointerException {
		
		if (viewModel == null) {
			throw new NullPointerException("No view model to set.");
		}
		
		if (e.getSource() == comparatorBox) {
			viewModel.setComparator(((Comparator<GraphVertex>) e.getItem()));
		}
	}

	public void stateChanged(ChangeEvent e) throws NullPointerException {
		
		if (viewModel == null) {
			throw new NullPointerException("No view model to set.");
		}

		if (e.getSource() == diameterSlider) {
			viewModel.setDiameter(diameterSlider.getValue() * 2);
		}

		else if (e.getSource() == bondBox) {
			viewModel.setBondEnable(bondBox.isSelected());
		}

		else if (e.getSource() == bondSlider) {
			int c = 254 - bondSlider.getValue();
			viewModel.setBondColor(new Color(c, c, c));
		}
	}

	/**
	 * The <code>Listener</code> inherited method.
	 * 
	 * @param event the {@link Event}.
	 */
	public void onEvent(Event event) {
		if (event instanceof EndGeneration) {
			progressBar.setValue(((EndGeneration) event).getGeneration());
		} else if (event instanceof EndRun) {
			progressBar.setValue(progressBar.getMaximum());
		}
	}

}