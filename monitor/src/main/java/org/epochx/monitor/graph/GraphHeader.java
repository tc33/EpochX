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
import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;

public class GraphHeader extends JPanel implements Runnable, Listener<Event>, ChangeListener, ItemListener,
		GraphViewListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6921704322479981889L;

	/**
	 * The view model to set.
	 */
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
	 * Constructs a <code>GraphHeader</code>.
	 */
	public GraphHeader() {
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
	 * Constructs a <code>GraphHeader</code> with a specified view model.
	 * 
	 * @param viewModel the specified view model.
	 */
	public GraphHeader(GraphViewModel viewModel) {
		this();
		setViewModel(viewModel);
	}

	/**
	 * The <code>Runnable</code> implemented method. Creates and shows the
	 * panel.
	 */
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
	 * Returns the view model.
	 * 
	 * @return the view model.
	 */
	public GraphViewModel getViewModel() {
		return viewModel;
	}

	/**
	 * Sets the view model. Manages the update of the listener.
	 * 
	 * @param viewModel the view model to set
	 */
	public void setViewModel(GraphViewModel viewModel) {

		
		if (this.viewModel != null) {
			this.viewModel.removeGraphViewListener(this);
		}
		
		if (viewModel != null) {
			viewModel.addGraphViewListener(this);

			diameterSlider.setValue(viewModel.getDiameter() / 2);
			bondBox.setSelected(viewModel.isBondEnable());
			bondSlider.setValue(254 - viewModel.getBondColor().getBlue());
		}
		
		this.viewModel = viewModel;
		
	}
	
	/**
	 * The <code>GraphViewListener</code> implemented method ; Recieves a
	 * <code>GraphViewEvent</code>.
	 */
	public void viewChanged(GraphViewEvent e) {
		GraphViewProperty viewProperty = e.getProperty();

		switch (viewProperty) {

			case DIAMETER:
				diameterSlider.setValue(viewModel.getDiameter() / 2);
				break;
			case COMPARATOR:
				comparatorBox.setSelectedItem(viewModel.getComparator());
				break;
			case BOUND_ENABLE:
				bondBox.setSelected(viewModel.isBondEnable());
				break;
			case BOUND_COLOR:
				bondSlider.setValue(254 - viewModel.getBondColor().getBlue());
				break;
			default:
				// Do nothing
		}

	}
	
	/**
	 * The <code>Listener</code> implemented method. Refreshs the progress bar.
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

	/**
	 * The <code>ItemListener</code> implemented method. Sets the view model
	 * according to the <code>ItemEvent</code> recieved from the comparator
	 * list.
	 * 
	 * @param e the <code>ItemEvent</code>.
	 */
	@SuppressWarnings("unchecked")
	public void itemStateChanged(ItemEvent e) {

		if (viewModel != null) {

			if (e.getSource() == comparatorBox) {
				viewModel.setComparator(((Comparator<GraphVertex>) e.getItem()));
			}
		}
	}

	/**
	 * The <code>ChangeListener</code> implemented method. Sets the view model
	 * according to the <code>ChangeEvent</code>.
	 * <p>
	 * The <code>ChangeEvent</code> could be recieved from :
	 * <ul>
	 * <li>The diameter slider.
	 * <li>The bond box.
	 * <li>The bond color slider.
	 * </p>
	 * 
	 * @param e the <code>ChangeEvent</code>.
	 */
	public void stateChanged(ChangeEvent e) {

		if (viewModel != null) {

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
	}

}