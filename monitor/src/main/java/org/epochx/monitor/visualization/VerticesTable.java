/* 
 * Copyright 2007-2012
 * Lawrence Beadle, Tom Castle and Fernando Otero
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.monitor.visualization;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.epochx.monitor.tree.TreeVertex;


/**
 * A <code>VerticesTable</code>.
 */
public class VerticesTable extends JScrollPane {

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = -7661840697176301995L;

	JTable table;
	
	VerticesTableModel model;
	
	ArrayList<TreeVertex> vertices = new ArrayList<TreeVertex>();
	ArrayList<Boolean> booleans = new ArrayList<Boolean>();
	
	
	public VerticesTable() {
		setName("Vertices Table");

		// Creates the table model.
		model = new VerticesTableModel();

		// JTable settings.
		table = new JTable(model);
		table.setAutoResizeMode(JTable.	AUTO_RESIZE_ALL_COLUMNS); // Uses a scrollbar.
		
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		table.getColumnModel().getColumn(4).setPreferredWidth(70);
		

		// JScrollPane settings.
		setViewportView(table);
		setPreferredSize(new Dimension(270, 400));
	}
	
	public void addListSelectionListener(ListSelectionListener l) {
		table.getSelectionModel().addListSelectionListener(l);
	}
	
	public TreeVertex[] getSelectedVertex() {
		TreeVertex[] res = new TreeVertex[table.getSelectedRows().length];
		synchronized(vertices) {
			for(int i = 0 ; i<res.length; i++) {
				res[i] = vertices.get(vertices.size() - 1 - table.getSelectedRows()[i]);
			}
		}
		return res;
	}

	public void addVertex(TreeVertex tv, boolean fromGenitor) {
		synchronized(vertices) {
			if(!vertices.contains(tv)){
				vertices.add(tv);
				booleans.add(fromGenitor);
			}
		}
		model.fireTableDataChanged();
	}
	
	public void clear() {
		synchronized(vertices) {
			vertices.clear();
			booleans.clear();
		}
		model.fireTableDataChanged();
	}
	
	class VerticesTableModel extends AbstractTableModel {
		

		/**
		 * The serialVersionUID.
		 */
		private static final long serialVersionUID = -3927090298238502553L;

		public int getColumnCount() {
			return 5;
		}


		public int getRowCount() {
			synchronized(vertices) {
				return vertices.size();
			}
		}

		public Object getValueAt(int rowIndex, int colIndex) {
			synchronized(vertices) {
				TreeVertex tv = vertices.get(getRowCount() - 1 - rowIndex);
				
				try {
					switch(colIndex) {
						case 0 :
							return tv.getVertex().getGenerationNo();
						case 1 :
							return booleans.get(getRowCount() - 1 - rowIndex) ? "Genitor" : "Provider";
						case 2 :
							return tv.getVertex().getIndividual().toString();
						case 3 :
							return tv.getVertex().getFitness().toString();
						case 4 :
							return tv.getVertex().getOperator().getClass().getSimpleName();
						default :
							return "";
							
					}
				}catch(Exception e) {
					return "";
				}
				
			}
		}
		
		public String getColumnName(int column) {
			switch(column) {
				case 0 :
					return "Gen#";
				case 1 :
					return "Nature";
				case 2 :
					return "Value";
				case 3 :
					return "Fitness";
				case 4 :
					return "Operator";
				default :
					return "";
			}
		}
		
		
		
	}
	
}
