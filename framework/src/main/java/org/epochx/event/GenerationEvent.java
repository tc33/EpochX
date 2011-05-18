package org.epochx.event;

public abstract class GenerationEvent implements Event {

	private int generation;
	
	public GenerationEvent(int generation) {
		this.generation = generation;
	}
	
	public int getGeneration()
	{
		return generation;
	}
	
	public static class StartGeneration extends GenerationEvent
	{
		public StartGeneration(int generation) {
			super(generation);
		}
	}
	
	public static class EndGeneration extends GenerationEvent
	{
		public EndGeneration(int generation) {
			super(generation);
		}
	}
}
