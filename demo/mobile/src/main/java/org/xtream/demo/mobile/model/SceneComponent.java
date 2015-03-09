package org.xtream.demo.mobile.model;

import java.awt.Color;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.State;
import org.xtream.core.model.components.AmbientComponent;
import org.xtream.core.model.components.BackgroundComponent;
import org.xtream.core.model.components.TransformComponent;
import org.xtream.core.model.components.nodes.CameraComponent;
import org.xtream.core.model.components.nodes.lights.PointLightComponent;
import org.xtream.core.model.components.nodes.shapes.CubeComponent;
import org.xtream.core.model.components.nodes.shapes.LineComponent;
import org.xtream.core.model.components.nodes.shapes.PlaneComponent;
import org.xtream.core.model.components.nodes.shapes.SphereComponent;
import org.xtream.core.model.components.transforms.IdentityComponent;
import org.xtream.core.model.components.transforms.chains.RotationComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.model.components.transforms.chains.rotations.YRotationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.demo.mobile.datatypes.Edge;
import org.xtream.demo.mobile.datatypes.Graph;
import org.xtream.demo.mobile.datatypes.Node;
import org.xtream.demo.mobile.model.root.ModulesContainer;
import org.xtream.demo.mobile.model.root.VehicleContainer;
import org.xtream.demo.mobile.visualization.ColorManager;

public class SceneComponent extends Component
{
	
	/////////////////
	// PARAMETERS //
	////////////////
	public ColorManager colorManager;
	
	// Graph normalization
	public static double XSCALE = 1.0;
	public static double YSCALE = 5.0;
	public static double ZSCALE = 1.0;
	
	////////////
	// INPUTS //
	////////////
	
	public Port<Double> input = new Port<>();
	
	/////////////
	// OUTPUTS //
	/////////////
	
	public Port<Double> output = new Port<>();
	
	////////////////
	// COMPONENTS //
	////////////////

	@SuppressWarnings("unchecked")
	public SceneComponent(final Graph graph, ModulesContainer modules)
	{
		colorManager = new ColorManager();
		int modulesLength = modules.modules.length;
		
		positionInputs = new Port[modulesLength];
		positionXInputs = new Port[modulesLength];
		positionYInputs = new Port[modulesLength];
		positionZInputs = new Port[modulesLength];
		
		position = new ChannelExpression[modulesLength];
		positionX = new ChannelExpression[modulesLength];
		positionY = new ChannelExpression[modulesLength];
		positionZ = new ChannelExpression[modulesLength];

		// Position spheres
	
		positionSpheres = new SphereComponent[modulesLength];
		translationPositionSpheres = new TranslationComponent[modulesLength];
		rotationToTranslationPositionSpheres = new ChannelExpression[modulesLength];
		translationToPositionSpheres = new ChannelExpression[modulesLength];
		
		// Graph nodes and height edges

		int nodesSize = graph.getNodes().size();
		
		nodes = new CubeComponent[nodesSize];
		translationNodes = new TranslationComponent[nodesSize];
		rotationToTranslationNodes = new ChannelExpression[nodesSize];
		translationToNodeCubes = new ChannelExpression[nodesSize];
		
		nodeHeightEdges = new LineComponent[nodesSize];
		translationNodeHeightEdges = new TranslationComponent[nodesSize];
		rotationToTranslationNodeHeightEdges = new ChannelExpression[nodesSize];
		translationToNodeHeightEdges = new ChannelExpression[nodesSize];
		
		// Graph edges
		
		int edgeSize = graph.getEdges().size();
		edges = new LineComponent[edgeSize];
		identityToEdges = new ChannelExpression[edgeSize];
		
		// Color distinction threshold for individual traffic participants
		float threshold = 1.0f/modulesLength*2;
		
		for (int i = 0; i < modulesLength; i++)
		{
			final int iterator = i;
			positionInputs[i] = new Port<>();
			positionXInputs[i] = new Port<>();
			positionYInputs[i] = new Port<>();
			positionZInputs[i] = new Port<>();
			
			VehicleContainer vehicleModule = (VehicleContainer) modules.modules[i];
			position[i] = new ChannelExpression<>(positionInputs[i], vehicleModule.positionOutput);
			positionX[i] = new ChannelExpression<>(positionXInputs[i], vehicleModule.context.positionXOutput);
			positionY[i] = new ChannelExpression<>(positionYInputs[i], vehicleModule.context.positionYOutput);
			positionZ[i] = new ChannelExpression<>(positionZInputs[i], vehicleModule.context.positionZOutput);
			
			// Determine color for sphere based on origin
			final Color sphereColor = new Color(Color.HSBtoRGB(colorManager.generateColor(vehicleModule.startPositionParameter, threshold), 1.f, 0.75f));
			
			positionSpheres[i] = new SphereComponent() 
			{
				@SuppressWarnings("unused")
				public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, sphereColor);
				@SuppressWarnings("unused")
				public Expression<Double> radiusExpression = new Expression<Double>(radiusOutput)
				{
					@Override protected Double evaluate(State state, int timepoint)
					{
						return 1.10;
					}
				};
			};
		
			translationPositionSpheres[i] = new TranslationComponent()
			{		
				@SuppressWarnings("unused")
				public Expression<Double> xExpression = new Expression<Double>(xOutput)
				{
					@Override
					protected Double evaluate(State state, int timepoint) 
					{
						return positionX[iterator].get(state, timepoint)*XSCALE;
					}
				};
				
				@SuppressWarnings("unused")
				public Expression<Double> yExpression = new Expression<Double>(yOutput)
				{
					@Override
					protected Double evaluate(State state, int timepoint) 
					{
						return positionZ[iterator].get(state, timepoint)*YSCALE;
					}
				};
				
				@SuppressWarnings("unused")
				public Expression<Double> zExpression = new Expression<Double>(zOutput)
				{
					@Override
					protected Double evaluate(State state, int timepoint) 
					{
						return positionY[iterator].get(state, timepoint)*ZSCALE;
					}
				};
			};

			rotationToTranslationPositionSpheres[i] = new ChannelExpression<RealMatrix>(translationPositionSpheres[i].transformInput, rotation.transformOutput);
			translationToPositionSpheres[i] = new ChannelExpression<RealMatrix>(positionSpheres[i].transformInput, translationPositionSpheres[i].transformOutput);
		}
		
		// Create graph nodes
		
		int iterator = 0;
		
		for (final Node node : graph.getNodes())
		{
			nodes[iterator] = new CubeComponent() 
			{
				@SuppressWarnings("unused")
				public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(112,112,112));
				@SuppressWarnings("unused")
				public Expression<Double> sizeExpression = new ConstantExpression<Double>(sizeOutput, 1.);
			};
			
			final Double nodeXPos = Double.parseDouble(node.getXpos());
			final Double nodeYPos = Double.parseDouble(node.getYpos());
			final Double nodeZPos = Double.parseDouble(node.getWeight());
			
			translationNodes[iterator] = new TranslationComponent()
			{
				@SuppressWarnings("unused")
				public Expression<Double> xExpression = new ConstantExpression<Double>(xOutput, nodeXPos*XSCALE);
				@SuppressWarnings("unused")
				public Expression<Double> yExpression = new ConstantExpression<Double>(yOutput, nodeZPos*YSCALE);
				@SuppressWarnings("unused")
				public Expression<Double> zExpression = new ConstantExpression<Double>(zOutput, nodeYPos*ZSCALE);
			};

			rotationToTranslationNodes[iterator] = new ChannelExpression<RealMatrix>(translationNodes[iterator].transformInput, rotation.transformOutput);
			translationToNodeCubes[iterator] = new ChannelExpression<RealMatrix>(nodes[iterator].transformInput, translationNodes[iterator].transformOutput);
			
			nodeHeightEdges[iterator] = new LineComponent()
			{
				@SuppressWarnings("unused")
				public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(240,240,240));
				@SuppressWarnings("unused")
				public Expression<RealVector> startExpression = new ConstantExpression<RealVector>(startOutput, new ArrayRealVector(new double[] {0, nodeZPos*YSCALE, 0, 1}));
				@SuppressWarnings("unused")
				public Expression<RealVector> endExpression = new Expression<RealVector>(endOutput)
				{
					@Override protected RealVector evaluate(State state, int timepoint)
					{
						return new ArrayRealVector(new double[] {0, nodeZPos*YSCALE, 0, 1});
					}
				};
			};
			
			translationNodeHeightEdges[iterator] = new TranslationComponent()
			{
				@SuppressWarnings("unused")
				public Expression<Double> xExpression = new ConstantExpression<Double>(xOutput, nodeXPos*XSCALE);
				@SuppressWarnings("unused")
				public Expression<Double> yExpression = new ConstantExpression<Double>(yOutput, 0.);
				@SuppressWarnings("unused")
				public Expression<Double> zExpression = new ConstantExpression<Double>(zOutput, nodeYPos*ZSCALE);
			};
			
			rotationToTranslationNodeHeightEdges[iterator] = new ChannelExpression<RealMatrix>(translationNodeHeightEdges[iterator].transformInput, rotation.transformOutput);
			translationToNodeHeightEdges[iterator] = new ChannelExpression<RealMatrix>(nodeHeightEdges[iterator].transformInput, translationNodeHeightEdges[iterator].transformOutput);
			
			iterator++;
		}
		
		// Create graph edges
		
		iterator = 0;

		for (final Edge edge : graph.getEdges())
		{
			final Double edgeXPos = Double.parseDouble((graph.getNode(edge.getSource()).getXpos()));
			final Double edgeYPos = Double.parseDouble((graph.getNode(edge.getSource()).getYpos()));
			final Double edgeZPos = Double.parseDouble((graph.getNode(edge.getSource()).getWeight()));
			
			final Double edgeVectorX = Double.parseDouble((graph.getNode(edge.getTarget()).getXpos()));
			final Double edgeVectorY = Double.parseDouble((graph.getNode(edge.getTarget()).getYpos()));
			final Double edgeVectorZ = Double.parseDouble((graph.getNode(edge.getTarget()).getWeight()));
			
			edges[iterator] = new LineComponent()
			{
				@SuppressWarnings("unused")
				public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(240,240,240));
				@SuppressWarnings("unused")
				public Expression<RealVector> startExpression = new ConstantExpression<RealVector>(startOutput, new ArrayRealVector(new double[] {edgeXPos*XSCALE, edgeZPos*YSCALE, edgeYPos*ZSCALE, 1}));
				@SuppressWarnings("unused")
				public Expression<RealVector> endExpression = new ConstantExpression<RealVector>(endOutput, new ArrayRealVector(new double[] {edgeVectorX*XSCALE, edgeVectorZ*YSCALE, edgeVectorY*ZSCALE, 1}));
			};
			
			identityToEdges[iterator] = new ChannelExpression<RealMatrix>(edges[iterator].transformInput, identity.transformOutput);
			
			iterator++;
		}
	}
	
	public SphereComponent[] positionSpheres;
	public TranslationComponent[] translationPositionSpheres;
	
	public CubeComponent[] nodes;
	public TranslationComponent[] translationNodes;
	
	public LineComponent[] nodeHeightEdges;
	public TranslationComponent[] translationNodeHeightEdges;
	
	public LineComponent[] edges;
	
	public TransformComponent identity = new IdentityComponent()
	{
		// empty
	};
	public BackgroundComponent background = new BackgroundComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(255, 255, 255));
	};
	public AmbientComponent ambient = new AmbientComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(255, 255, 255));
	};
	public PlaneComponent plane = new PlaneComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(255, 255, 255));
		@SuppressWarnings("unused")
		public Expression<Double> heightExpression = new ConstantExpression<Double>(heightOutput, 0.);
	};
	public CameraComponent camera = new CameraComponent()
	{
		@SuppressWarnings("unused")
		public Expression<RealVector> eyeExpression = new ConstantExpression<RealVector>(eyeOutput, new ArrayRealVector(new double[] {-25,50,-25,1}));
		@SuppressWarnings("unused")
		public Expression<RealVector> centerExpression = new ConstantExpression<RealVector>(centerOutput, new ArrayRealVector(new double[] {50,0,50,1}));
		@SuppressWarnings("unused")
		public Expression<RealVector> upExpression = new ConstantExpression<RealVector>(upOutput, new ArrayRealVector(new double[] {0,1,0,1}));
	};
	public PointLightComponent light = new PointLightComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Color> specularExpression = new ConstantExpression<Color>(specularOutput, new Color(255,255,255));
		@SuppressWarnings("unused")
		public Expression<Color> diffuseExpression = new ConstantExpression<Color>(diffuseOutput, new Color(255,255,255));
		@SuppressWarnings("unused")
		public Expression<RealVector> positionExpression = new ConstantExpression<RealVector>(positionOutput, new ArrayRealVector(new double[] {0,25,25,0}));
	};
	public RotationComponent rotation = new YRotationComponent()
	{
		@SuppressWarnings("unused")
		public Expression<Double> angleExpression = new ConstantExpression<Double>(angleOutput, 0.);
	};

	public Port<Edge>[] positionInputs;
	public Port<Double>[] positionXInputs;
	public Port<Double>[] positionYInputs;
	public Port<Double>[] positionZInputs;
	
	//////////////
	// CHANNELS //
	//////////////

	public ChannelExpression<Edge>[] position;
	public ChannelExpression<Double>[] positionX;
	public ChannelExpression<Double>[] positionY;
	public ChannelExpression<Double>[] positionZ;
	
	public ChannelExpression<RealMatrix>[] rotationToTranslationPositionSpheres;
	public ChannelExpression<RealMatrix>[] translationToPositionSpheres;
	
	public ChannelExpression<RealMatrix>[] rotationToTranslationNodes;
	public ChannelExpression<RealMatrix>[] translationToNodeCubes;
	
	public ChannelExpression<RealMatrix>[] rotationToTranslationNodeHeightEdges;
	public ChannelExpression<RealMatrix>[] translationToNodeHeightEdges;

	public ChannelExpression<RealMatrix> identityToPlane = new ChannelExpression<RealMatrix>(plane.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> identityToCamera = new ChannelExpression<RealMatrix>(camera.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> identityToLight = new ChannelExpression<RealMatrix>(light.transformInput, identity.transformOutput);
	public ChannelExpression<RealMatrix> identityToRotation = new ChannelExpression<RealMatrix>(rotation.transformInput, identity.transformOutput);
	
	public ChannelExpression<RealMatrix>[] identityToEdges;
	
	/////////////////
	// EXPRESSIONS //
	/////////////////

	// Legacy
	
	public Expression<Double> inputExpression = new ConstantExpression<Double>(input, 0.);
	
	public Expression<Double> outputExpression = new ConstantExpression<Double>(output, 0.);
}
