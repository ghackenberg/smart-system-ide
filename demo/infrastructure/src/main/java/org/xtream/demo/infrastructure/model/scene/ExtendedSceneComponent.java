package org.xtream.demo.infrastructure.model.scene;

import java.awt.Color;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.nodes.shapes.LineComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.infrastructure.datatypes.Edge;
import org.xtream.demo.infrastructure.datatypes.Graph;
import org.xtream.demo.infrastructure.datatypes.Node;
import org.xtream.demo.infrastructure.model.power.PowerComponent;
import org.xtream.demo.infrastructure.model.scenario.Scenario;
import org.xtream.demo.infrastructure.model.transportation.CarComponent;
import org.xtream.demo.infrastructure.model.transportation.TransportationComponent;

public class ExtendedSceneComponent extends SceneComponent
{
	////////////////
	// COMPONENTS //
	////////////////

	@SuppressWarnings("unchecked")
	public ExtendedSceneComponent(final Graph graph, TransportationComponent transportationSystem, PowerComponent powerSystem)
	{
		super(graph, transportationSystem, powerSystem);
		
		int transportationSystemLength = transportationSystem.cars.length;
		destinationPositionInputs = new Port[transportationSystemLength];
		destinationPosition = new ChannelExpression[transportationSystemLength];
		
		// Destination Position edges
		
		destinationPositionEdges = new LineComponent[transportationSystemLength];
		translationDestinationPositionEdges = new TranslationComponent[transportationSystemLength];
		rotationToTranslationDestinationPositionEdges = new ChannelExpression[transportationSystemLength];
		translationToDestinationPositionEdges = new ChannelExpression[transportationSystemLength];
		
		// Color distinction threshold for individual traffic participants
		float threshold = 1.0f/transportationSystemLength*2;
		
		for (int i = 0; i < transportationSystemLength; i++)
		{
			final int iterator = i;
			destinationPositionInputs[i] = new Port<>();
			
			CarComponent vehicleModule = transportationSystem.cars[i];
			destinationPosition[i] = new ChannelExpression<>(destinationPositionInputs[i], vehicleModule.destinationPositionOutput);

			final Color destinationPositionEdgeColor = new Color(Color.HSBtoRGB(colorManager.generateColor(vehicleModule.startPosition.toString(), threshold), 1.f, 0.75f));
			
			destinationPositionEdges[iterator] = new LineComponent()
			{
				@SuppressWarnings("unused")
				public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, destinationPositionEdgeColor);
				@SuppressWarnings("unused")
				public Expression<RealVector> startExpression = new Expression<RealVector>(startOutput)
				{
					@Override protected RealVector evaluate(State state, int timepoint)
					{
						Double positionZWithOffset;
						
						Edge currentPosition = position[iterator].get(state, timepoint);
						
						if (currentPosition.getSource().equals(currentPosition.getTarget()))
						{
							positionZWithOffset = (positionZ[iterator].get(state, timepoint)+YSTATIONOFFSET);
						}
						else 
						{
							positionZWithOffset = positionZ[iterator].get(state, timepoint);
						}
						
						return new ArrayRealVector(new double[] {positionX[iterator].get(state, timepoint)*XSCALE, positionZWithOffset*YSCALE, positionY[iterator].get(state, timepoint)*ZSCALE, 1});
					}
				};
				@SuppressWarnings("unused")
				public Expression<RealVector> endExpression = new Expression<RealVector>(endOutput)
				{
					@Override protected RealVector evaluate(State state, int timepoint)
					{
						Node destinationPositionNode = Scenario.context.getNode(destinationPosition[iterator].get(state, timepoint).getTarget());
						
						return new ArrayRealVector(new double[] {destinationPositionNode.getXPos()*XSCALE, (destinationPositionNode.getZPos()+YSTATIONOFFSET)*YSCALE, destinationPositionNode.getYPos()*ZSCALE, 1});
					}
				};
			};
			
			translationDestinationPositionEdges[iterator] = new TranslationComponent()
			{
				@SuppressWarnings("unused")
				public Expression<Double> xExpression = new ConstantExpression<Double>(xOutput, 0.);
				@SuppressWarnings("unused")
				public Expression<Double> yExpression = new ConstantExpression<Double>(yOutput, 0.);
				@SuppressWarnings("unused")
				public Expression<Double> zExpression = new ConstantExpression<Double>(zOutput, 0.);
			};
			
			rotationToTranslationDestinationPositionEdges[iterator] = new ChannelExpression<RealMatrix>(translationDestinationPositionEdges[iterator].transformInput, rotation.transformOutput);
			translationToDestinationPositionEdges[iterator] = new ChannelExpression<RealMatrix>(destinationPositionEdges[iterator].transformInput, translationDestinationPositionEdges[iterator].transformOutput);
			
		}
	}

	public LineComponent[] destinationPositionEdges;
	public TranslationComponent[] translationDestinationPositionEdges;
	public Port<Edge>[] destinationPositionInputs;
	
	//////////////
	// CHANNELS //
	//////////////
	
	public ChannelExpression<Edge>[] destinationPosition;
	public ChannelExpression<RealMatrix>[] rotationToTranslationDestinationPositionEdges;
	public ChannelExpression<RealMatrix>[] translationToDestinationPositionEdges;

}
