package org.xtream.core.workbench.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;

import org.xtream.core.model.Component;
import org.xtream.core.model.markers.Objective;
import org.xtream.core.optimizer.Key;
import org.xtream.core.optimizer.State;
import org.xtream.core.optimizer.Statistics;
import org.xtream.core.workbench.Part;

import com.jogamp.opengl.util.gl2.GLUT;

public class SearchSpacePart<T extends Component> extends Part<T>
{
	
	private GLJPanel canvas;
	private int timepoint;
	private Statistics statistics;
	private double minObjective = Double.MAX_VALUE;
	private double maxObjective = Double.MIN_VALUE;
	private List<Set<State>> states;
	private Map<State, Set<State>> followers;
	private Map<State, State> leaders;
	private Map<State, Integer> counts;
	private Object mutex = new Object();

	public SearchSpacePart()
	{
		this(0, 0);
	}
	public SearchSpacePart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public SearchSpacePart(int x, int y, int width, int height)
	{
		super("Search space", x, y, width, height);
		
		try
		{
			GLProfile profile = GLProfile.getDefault();
			GLCapabilities capabilities = new GLCapabilities(profile);
			
			canvas = new GLJPanel(capabilities);
			canvas.addGLEventListener(new GLEventListener()
				{
					@Override
					public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
					{
						GL2 gl2 = drawable.getGL().getGL2();
						GLU glu = new GLU();
						
						// Viewport
						gl2.glViewport(x, y, width, height);

						// Projection
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						{
							gl2.glLoadIdentity();
							glu.gluPerspective(60f, (float)width/height, 1f, 1000f);
						}
					}
					@Override
					public void init(GLAutoDrawable drawable)
					{
						GL2 gl2 = drawable.getGL().getGL2();
						
						// Features
						gl2.glEnable(GL2.GL_DEPTH_TEST);
						gl2.glEnable(GL2.GL_LIGHTING);
						gl2.glEnable(GL2.GL_LIGHT0);
						gl2.glEnable(GL2.GL_COLOR_MATERIAL);
						
						// Clear
						gl2.glClearColor(1f, 1f, 1f, 0f);
						gl2.glClearDepth(1f);

						// Material
						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[] {1f, 1f, 1f, 1f}, 0);
						gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 50f);
						gl2.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
						
						// Light
						gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {0.5f, 0.5f, 0.5f, 1f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {0f, 20f, 5f, 0f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] {1f, 1f, 1f, 1f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {1f, 1f, 1f, 1f}, 0);
					}
					@Override
					public void dispose(GLAutoDrawable drawable)
					{
						
					}
					@Override
					public void display(GLAutoDrawable drawable)
					{
						GL2 gl2 = drawable.getGL().getGL2();
						GLU glu = new GLU();
						GLUT glut = new GLUT();
						
						gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
						
						// Modelview
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						{
							gl2.glLoadIdentity();
							glu.gluLookAt(-125f, 25f, 150f, 25f, 0f, 0f, 0f, 1f, 0f);
						}
						
						synchronized (mutex)
						{
							if (timepoint > 0)
							{
								layoutFollowing(gl2, glut, states.get(0).iterator().next(), 0, 360, 0, -100, 0, 0);
								
								gl2.glLineWidth(1f);
								gl2.glColor3d(0.5f, 0.5f, 0.5f);
								
								// Time axis
								
								gl2.glBegin(GL2.GL_LINES);
								{
									gl2.glVertex3d(-100, 0, 0);
									gl2.glVertex3d(100, 0, 0);
								}
								gl2.glEnd();
								
								// Minimum objective
								
								gl2.glBegin(GL2.GL_LINE_LOOP);
								{
									double radius = (statistics.minObjective - minObjective) / (maxObjective - minObjective);
									
									for (double a = 0; a < 360; a+=5)
									{
										gl2.glVertex3d(100, Math.sin(a / 180 * Math.PI) * radius * 100, Math.cos(a / 180 * Math.PI) * radius * 100);
									}
								}
								gl2.glEnd();
								
								// Maximum objective
								
								gl2.glBegin(GL2.GL_LINE_LOOP);
								{
									double radius = (statistics.maxObjective - minObjective) / (maxObjective - minObjective);
									
									for (double a = 0; a < 360; a+=5)
									{
										gl2.glVertex3d(100, Math.sin(a / 180 * Math.PI) * radius * 100, Math.cos(a / 180 * Math.PI) * radius * 100);
									}
								}
								gl2.glEnd();
							}
						}
					}
				}
			);
			
			getPanel().add(canvas);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void handle(int timepoint, Statistics statistics, Map<Key, List<State>> clusters, State best)
	{
		synchronized (mutex)
		{
			this.timepoint = timepoint;
			this.statistics = statistics;
			
			// Create data structures
			
			states = new ArrayList<>();
			followers = new HashMap<>();
			leaders = new HashMap<>();
			counts = new HashMap<>();
			
			for (int step = 0; step <= timepoint + 1; step++)
			{
				states.add(new HashSet<State>());
			}
			
			// Initialize data structures
	
			for (Entry<Key, List<State>> entry : clusters.entrySet())
			{
				for (State state : entry.getValue())
				{
					State leader = state;
					
					while (state != null)
					{
						// States
						
						Set<State> set = states.get(state.getTimepoint() + 1);
						
						set.add(state);
						
						// Follower
						
						Set<State> next = followers.get(state.getPrevious());
						
						if (next == null)
						{
							next = new HashSet<>();
							
							followers.put(state.getPrevious(), next);
						}
						
						next.add(state);
						
						// Leaders
						
						State current = leaders.get(state);
						
						if (current == null || leader.compareObjectiveTo(current) < 0)
						{
							leaders.put(state, leader);
						}
						
						// Iterate
						
						state = state.getPrevious();
					}
				}
			}
			
			for (int step = 0; step <= timepoint; step++)
			{
				for (State state : states.get(step + 1))
				{
					double objective = getRoot().getDescendantByClass(Objective.class).getPort().get(state, step);
					
					minObjective = Math.min(minObjective, objective);
					maxObjective = Math.max(maxObjective, objective);
				}
			}
			
			countFollowing(states.get(0).iterator().next());
			
			// Repaint canvas
			
			canvas.repaint();
		}
	}
	
	private void layoutFollowing(GL2 gl2, GLUT glut, State state, double minAngle, double maxAngle, double prevAngle, double prevX, double prevY, double prevZ)
	{
		if (followers.get(state) != null)
		{
			// Calculate weight sum
			
			double weightSum = counts.get(state);
			
			// Render each individual
			
			double iterAngle = minAngle;
	
			for (State next : followers.get(state))
			{
				// Calculate weight
				
				double weight = counts.get(next);
				
				// Calculate coordinates
				
				double objective = getRoot().getDescendantByClass(Objective.class).getPort().get(next, next.getTimepoint());
				double radius = (objective - minObjective) / (maxObjective - minObjective);
				double angle = minAngle + (maxAngle - minAngle) * weight / weightSum;
				
				double actualAngle = prevAngle + ((angle + iterAngle) / 2 - prevAngle) / 4;
				
				double x = 200 * next.getTimepoint() / timepoint - 100;
				double y = Math.sin(actualAngle / 180 * Math.PI) * radius * 100;
				double z = Math.cos(actualAngle / 180 * Math.PI) * radius * 100;
				
				// Draw state
				
				gl2.glPushMatrix();
				{
					gl2.glTranslated(x, y, z);
					
					double leaderObjective = getRoot().getDescendantByClass(Objective.class).getPort().get(leaders.get(next), timepoint);
					
					if (Math.abs(leaderObjective - statistics.maxObjective) < Math.abs(leaderObjective - statistics.avgObjective))
					{
						gl2.glColor3d(0, 1, 0);
					}
					else if (Math.abs(leaderObjective - statistics.minObjective) < Math.abs(leaderObjective - statistics.avgObjective))
					{
						gl2.glColor3d(1, 0, 0);
					}
					else
					{
						gl2.glColor3d(0, 1, 0);
					}
					
					glut.glutSolidCube(1);
				}
				gl2.glPopMatrix();
				
				// Draw connection
				
				gl2.glLineWidth(1f);
				gl2.glColor3d(0.5f, 0.5f, 0.5f);
				
				gl2.glBegin(GL2.GL_LINES);
				{
					gl2.glVertex3d(prevX, prevY, prevZ);
					gl2.glVertex3d(x, y, z);
				}
				gl2.glEnd();
				
				// Draw following
				
				layoutFollowing(gl2, glut, next, iterAngle, angle, actualAngle, x, y, z);
				
				// Update iteration variables
				
				iterAngle = angle;
			}
		}
	}
	
	private int countFollowing(State state)
	{
		if (followers.get(state) != null)
		{
			int count = 0;
			
			for (State next : followers.get(state))
			{
				count += countFollowing(next);
			}
			
			counts.put(state, count);
			
			return count;
		}
		else
		{
			counts.put(state, 1);
			
			return 1;
		}
	}

}
