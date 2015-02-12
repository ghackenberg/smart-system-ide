package org.xtream.demo.ppu.model;

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
import org.xtream.core.model.components.nodes.CameraComponent;
import org.xtream.core.model.components.nodes.LightComponent;
import org.xtream.core.model.components.nodes.lights.PointLightComponent;
import org.xtream.core.model.components.nodes.shapes.PlaneComponent;
import org.xtream.core.model.components.transforms.IdentityComponent;
import org.xtream.core.model.components.transforms.chains.TranslationComponent;
import org.xtream.core.model.expressions.ChannelExpression;
import org.xtream.core.model.expressions.ConstantExpression;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.model.markers.objectives.MinObjective;
import org.xtream.core.workbench.Workbench;

@SuppressWarnings("unused")
public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Workbench<>(new RootComponent(), 50);
	}
	
	// Ports
	
	public Port<Double> objectiveOutput = new Port<Double>();
	
	// Markers
	
	public Objective objectiveMarker = new MinObjective(objectiveOutput);
	
	// Components
	
	public StackComponent stack = new StackComponent();
	public CraneComponent crane = new CraneComponent();
	public StampComponent stamp = new StampComponent();
	public ConveyorComponent conveyor = new ConveyorComponent();
	public SlideComponent slide_one = new SlideComponent(2.0,0.0,2.0);
	public SlideComponent slide_two = new SlideComponent(2.0,0.0,2.0);
	public SlideComponent slide_three= new SlideComponent(0.0,2.0,2.0);
	public WorkpieceComponent workpiece = new WorkpieceComponent(0,0);
	
	public BackgroundComponent background = new BackgroundComponent()
	{
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(255,255,255));
	};
	public AmbientComponent ambient = new AmbientComponent()
	{
		public Expression<Color> colorExpression = new ConstantExpression<>(colorOutput, new Color(32,32,32));
	};
	public CameraComponent camera = new CameraComponent()
	{
		public Expression<RealVector> eyeExpression = new ConstantExpression<RealVector>(eyeOutput, new ArrayRealVector(new double[] {10.0,20.0,17.0,1.0}));
		public Expression<RealVector> centerExpression = new ConstantExpression<RealVector>(centerOutput, new ArrayRealVector(new double[] {0.0,0.0,7.0,1.0}));
		public Expression<RealVector> upExpression = new ConstantExpression<RealVector>(upOutput, new ArrayRealVector(new double[] {0.0,1.0,0.0,1.0}));
	};
	public LightComponent light = new PointLightComponent()
	{
		public Expression<RealVector> positionExpression = new ConstantExpression<>(positionOutput, new ArrayRealVector(new double[] {-10.0,10.0,10.0,1.0}));
		public Expression<Color> specularExpression = new ConstantExpression<>(specularOutput, new Color(255,255,255));
		public Expression<Color> diffuseExpression = new ConstantExpression<>(diffuseOutput, new Color(255,255,255));
	};
	public PlaneComponent plane = new PlaneComponent() 
	{
		public Expression<Color> colorExpression = new ConstantExpression<Color>(colorOutput, new Color(255, 255, 255));
		public Expression<Double> heightExpression = new ConstantExpression<Double>(heightOutput, 0.0);	
	};
	
	public IdentityComponent identity = new IdentityComponent();
	
	public TranslationComponent translation_stack = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, -5.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 1.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	};
	public TranslationComponent translation_crane = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 0.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 1.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	};
	public TranslationComponent translation_stamp = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 5.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 1.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 0.0);
	};
	public TranslationComponent translation_conveyor = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 0.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 1.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 7.0);
	};
	public TranslationComponent translation_slide_one = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 1.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 0.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 5.0);
	};
	public TranslationComponent translation_slide_two = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 1.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 0.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 9.0);
	};
	public TranslationComponent translation_slide_three = new TranslationComponent()
	{
		public Expression<Double> xExpression = new ConstantExpression<>(xOutput, 0.0);
		public Expression<Double> yExpression = new ConstantExpression<>(yOutput, 0.0);
		public Expression<Double> zExpression = new ConstantExpression<>(zOutput, 12.0);
	};
	
	// Channels
	
	public Expression<RealMatrix> transformIdentityToCamera = new ChannelExpression<>(camera.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToLight= new ChannelExpression<>(light.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToPlane = new ChannelExpression<>(plane.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToWorkpiece = new ChannelExpression<>(workpiece.transformInput, identity.transformOutput);
	
	public Expression<RealMatrix> transformIdentityToTranslationStack = new ChannelExpression<>(translation_stack.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToTranslationCrane = new ChannelExpression<>(translation_crane.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToTranslationStamp = new ChannelExpression<>(translation_stamp.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToTranslationConveyor = new ChannelExpression<>(translation_conveyor.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToTranslationSlideOne = new ChannelExpression<>(translation_slide_one.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToTranslationSlideTwo = new ChannelExpression<>(translation_slide_two.transformInput, identity.transformOutput);
	public Expression<RealMatrix> transformIdentityToTranslationSlideThree = new ChannelExpression<>(translation_slide_three.transformInput, identity.transformOutput);
	
	public Expression<RealMatrix> transformTranslationStackToStack = new ChannelExpression<>(stack.transformInput, translation_stack.transformOutput);
	public Expression<RealMatrix> transformTranslationCraneToCrane = new ChannelExpression<>(crane.transformInput, translation_crane.transformOutput);
	public Expression<RealMatrix> transformTranslationStampToStamp = new ChannelExpression<>(stamp.transformInput, translation_stamp.transformOutput);
	public Expression<RealMatrix> transformTranslationConveyorToConveyor = new ChannelExpression<>(conveyor.transformInput, translation_conveyor.transformOutput);
	public Expression<RealMatrix> transformTranslationSlideOneToSlideOne = new ChannelExpression<>(slide_one.transformInput, translation_slide_one.transformOutput);
	public Expression<RealMatrix> transformTranslationSlideTwoToSlideTwo = new ChannelExpression<>(slide_two.transformInput, translation_slide_two.transformOutput);
	public Expression<RealMatrix> transformTranslationSlideThreeToSlideThree = new ChannelExpression<>(slide_three.transformInput, translation_slide_three.transformOutput);
	
	public Expression<RealVector> positionWorkpieceToConveyor = new ChannelExpression<>(conveyor.positionInput, workpiece.positionOutput);
	public Expression<RealVector> positionWorkpieceToStack = new ChannelExpression<>(stack.positionInput, workpiece.positionOutput);
	public Expression<RealVector> positionWorkpieceToStamp = new ChannelExpression<>(stamp.positionInput, workpiece.positionOutput);
	public Expression<Integer> typeWorkpieceToConveyor = new ChannelExpression<>(conveyor.typeInput, workpiece.typeOutput);
	public Expression<Integer> typeWorkpieceToStack = new ChannelExpression<>(stack.typeInput, workpiece.typeOutput);
	public Expression<Integer> typeWorkpieceToStamp = new ChannelExpression<>(stamp.typeInput, workpiece.typeOutput);
	public Expression<Integer> stateWorkpieceToConveyor = new ChannelExpression<>(conveyor.stateInput, workpiece.stateOutput);
	public Expression<Integer> stateWorkpieceToStack = new ChannelExpression<>(stack.stateInput, workpiece.stateOutput);
	public Expression<Integer> stateWorkpieceToStamp = new ChannelExpression<>(stamp.stateInput, workpiece.stateOutput);
	
	public Expression<Double> positionStackCylinderToWorkpiece = new ChannelExpression<>(workpiece.positionStackCylinder, stack.cylinder.positionOutput);
	public Expression<Double> positionCraneCylinderToWorkpiece = new ChannelExpression<>(workpiece.positionCraneCylinder, crane.cylinder.positionOutput);
	public Expression<Double> positionCraneArmToWorkpiece = new ChannelExpression<>(workpiece.positionCraneArm, crane.arm.positionOutput);
	public Expression<Double> positionStampCylinderOneToWorkpiece = new ChannelExpression<>(workpiece.positionStampCylinderOne, stamp.cylinder_one.positionOutput);
	public Expression<Double> positionStampCylinderTwoToWorkpiece = new ChannelExpression<>(workpiece.positionStampCylinderTwo, stamp.cylinder_two.positionOutput);
	public Expression<Double> positionConveyorCylinderOneToWorkpiece = new ChannelExpression<>(workpiece.positionConveyorCylinderOne, conveyor.cylinder_one.positionOutput);
	public Expression<Double> positionConveyorCylinderTwoToWorkpiece = new ChannelExpression<>(workpiece.positionConveyorCylinderTwo, conveyor.cylinder_two.positionOutput);
	public Expression<Double> energyCraneArmSuckerToWorkpiece = new ChannelExpression<>(workpiece.energyCraneArmSucker, crane.arm.sucker.energyOutput);
	
	// Expressions
	
	public Expression<Double> objectiveExpression = new Expression<Double>(objectiveOutput, true)
	{
		@Override protected Double evaluate(State state, int timepoint)
		{
			return 0.0;
		}
		
	};

}
