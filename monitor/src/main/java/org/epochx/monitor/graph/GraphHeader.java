package org.epochx.monitor.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

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


public class GraphHeader extends JPanel implements Runnable, Listener<Event>, ChangeListener, ItemListener{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6921704322479981889L;
	
	private GraphViewModel viewModel;
	
	private JProgressBar progressBar;
	private JComboBox comparatorBox;
	private JSlider diameterSlider;
	private JSlider bondSlider;

	/**
	 * Create the panel.
	 */
	public GraphHeader(GraphViewModel viewModel) throws NullPointerException {
		
		if ( viewModel == null) {
			throw new NullPointerException("The view model cannot be null.");
		}
		
		this.viewModel = viewModel;
		
		setPreferredSize(new Dimension(600, 30));
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEADING);
		
		setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		
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
	
	public void run() {
		JLabel lblProgress = new JLabel("Progress :");
		add(lblProgress);
		
		progressBar = new JProgressBar(0, Config.getInstance().get(MaximumGenerations.MAXIMUM_GENERATIONS));
		progressBar.setPreferredSize(new Dimension(100, 14));
		progressBar.setStringPainted(true);
		add(progressBar);
		
		JLabel lblSortedBy = new JLabel("Sort by :");
		add(lblSortedBy);
		
		comparatorBox = new JComboBox();
		comparatorBox.addItem(GraphViewModel.FITNESS_COMPARATOR);
		comparatorBox.addItem(GraphViewModel.PARENT_COMPARATOR);
		comparatorBox.addItem(GraphViewModel.OPERATOR_COMPARATOR);
		comparatorBox.setSelectedIndex(-1);
		comparatorBox.addItemListener(this);
		comparatorBox.setPreferredSize(new Dimension(100, 20));
		add(comparatorBox);
		
		JLabel lblDiameter = new JLabel("Zoom :");
		add(lblDiameter);
		
		diameterSlider = new JSlider(1, 25, viewModel.getDiameter()/2);
		diameterSlider.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		diameterSlider.addChangeListener(this);
		diameterSlider.setPreferredSize(new Dimension(70, 16));
		add(diameterSlider);

		JLabel lblBond = new JLabel("Bonds :");
		add(lblBond);
		
		bondSlider = new JSlider(0, 100, 254-viewModel.getBondColor().getBlue());
		bondSlider.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		bondSlider.addChangeListener(this);
		bondSlider.setPreferredSize(new Dimension(70, 16));
		add(bondSlider);
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
	public void setViewModel(GraphViewModel viewModel) throws NullPointerException {
		
		if ( viewModel == null) {
			throw new NullPointerException("The view model cannot be null.");
		}
		this.viewModel = viewModel;
	}

	/**
	 * @return the progressBar
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	
	/**
	 * @return the comparatorBox
	 */
	public JComboBox getComparatorBox() {
		return comparatorBox;
	}

	
	/**
	 * @return the bondSlider
	 */
	public JSlider getDiameterSlider() {
		return bondSlider;
	}


	

	@SuppressWarnings("unchecked")
	public void itemStateChanged(ItemEvent e) {
		if ( e.getSource() == comparatorBox){
			viewModel.setComparator(((Comparator<GraphVertex>) e.getItem()));
		}
	}


	public void stateChanged(ChangeEvent e) {
		
		if ( e.getSource() == diameterSlider ) {
			viewModel.setDiameter(diameterSlider.getValue()*2);
		}
		
		if ( e.getSource() == bondSlider ) {
			int c = 254-bondSlider.getValue();
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