package org.epochx.monitor.graph0;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.epochx.Config;
import org.epochx.MaximumGenerations;
import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.Listener;
import org.epochx.event.RunEvent.EndRun;


public class PnlHeader extends JPanel implements Listener<Event>, ChangeListener, ItemListener{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6921704322479981889L;
	
	private final Graph graph;
	
	private JProgressBar progressBar;
	private JComboBox comparatorBox;
	private JSlider diameterSlider;

	/**
	 * Create the panel.
	 */
	public PnlHeader(Graph graph) {
		this.graph = graph;
		
		setPreferredSize(new Dimension(600, 30));
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEADING);
		
		EventManager.getInstance().add(EndGeneration.class, this);
		EventManager.getInstance().add(EndRun.class, this);
		
		JLabel lblProgress = new JLabel("Progress :");
		add(lblProgress);
		
		progressBar = new JProgressBar(0, Config.getInstance().get(MaximumGenerations.MAXIMUM_GENERATIONS));
		progressBar.setPreferredSize(new Dimension(100, 14));
		progressBar.setStringPainted(true);
		add(progressBar);
		
		JLabel lblSortedBy = new JLabel("Sorted by :");
		add(lblSortedBy);
		
		comparatorBox = new JComboBox();
		comparatorBox.addItem(Graph.FITNESS_COMPARATOR);
		comparatorBox.addItem(Graph.PARENT_COMPARATOR);
		comparatorBox.addItem(Graph.OPERATOR_COMPARATOR);
		comparatorBox.addItemListener(this);
		comparatorBox.setPreferredSize(new Dimension(120, 20));
		add(comparatorBox);
		
		JLabel lblDiameter = new JLabel("Diameter :");
		add(lblDiameter);
		
		diameterSlider = new JSlider(5, 10);
		diameterSlider.addChangeListener(this);
		diameterSlider.setPreferredSize(new Dimension(90, 16));
		add(diameterSlider);

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
	 * @return the diameterSlider
	 */
	public JSlider getDiameterSlider() {
		return diameterSlider;
	}


	@SuppressWarnings("unchecked")
	public void itemStateChanged(ItemEvent arg0) {
		graph.getPnlGraph().reorder((Comparator<GraphNode>) arg0.getItem());
		graph.getPnlGraph().revalidate();
	}


	public void stateChanged(ChangeEvent arg0) {
		graph.getPnlGraph().setDiameter(diameterSlider.getValue());
		
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