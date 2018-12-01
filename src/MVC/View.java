package MVC;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class View extends JPanel implements MouseListener, KeyListener{

	PlayerCharacter player;
	ArrayList<NativePlant> nativePlants;
	ArrayList<InvasivePlant> invasivePlants;
	ArrayList<GroundPatch> groundList;
	ArrayList<Obstacle> obstacleList;
	Tool tool;
	Controller control;
	
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenHeight = screenSize.getSize().height;
	int screenWidth = screenSize.getSize().width;
	int plantedCount = 0;
	int plantsRemoved = 0;
	int manFrameCountRight = 0;

	Image playerimgFront;
	Image playerimgBack;
	Image playerimgRight;
	Image playerimgLeft;
	Image playerimg;
	Image groundimg;
	Image nplantimg;
	Image iplantimg;
	Image rockimg;
	Image bgimg;
  ///  JLabel background1 = new JLabel(new ImageIcon("res/Person-Images/Background.jpg"));

	public Image img;//vague
	
	public View() {
		player = new PlayerCharacter();
		nativePlants = new ArrayList<NativePlant>();
		invasivePlants = new ArrayList<InvasivePlant>();
		groundList = new ArrayList<GroundPatch>();
		obstacleList = new ArrayList<Obstacle>();
		tool = new Tool(true);
		
		setFocusable(true);
		
		requestFocusInWindow();
		
		//Images
		ImageIcon bgImage = new ImageIcon("res/Person-Images/BackGround1.jpg");
		ImageIcon groundicon = new ImageIcon("images/ground.png");
		ImageIcon nplanticon = new ImageIcon("images/nativeplant.png");
		ImageIcon iplanticon = new ImageIcon("images/phragmites.png");
		ImageIcon rockicon = new ImageIcon("images/rock.png");
		ImageIcon playerImageFront = new ImageIcon("images/Person_FRONT.png");
		ImageIcon playerImageBack = new ImageIcon("images/Person_BACK.png");
		ImageIcon playerImageRight = new ImageIcon("images/Person_RIGHT.png");
		ImageIcon playerImageLeft = new ImageIcon("images/Person_LEFT.png");
		playerimgFront = playerImageFront.getImage();
		playerimgBack = playerImageBack.getImage();
		playerimgRight = playerImageRight.getImage();
		playerimgLeft = playerImageLeft.getImage();
		playerimg = playerimgFront;
		groundimg = groundicon.getImage();
		nplantimg = nplanticon.getImage();
		iplantimg = iplanticon.getImage();
		rockimg = rockicon.getImage();
		bgimg = bgImage.getImage();
		
		

		
		
	}
	
	public void paintPlantedInfo(int plantedCount,int x, int y) {
	    final JOptionPane pane = new JOptionPane("You've planted " + plantedCount + " native plants and removed " + plantsRemoved + " invasive Phragmites plants!");
	    final JDialog d = pane.createDialog((JFrame)null, "New Message!");
	    d.setLocation(x,y);
	    d.setVisible(true);
//		JOptionPane.showMessageDialog(null, 
//                "You've planted a new plant! #" + plantedCount, 
//                "New Message!", 
//                JOptionPane.PLAIN_MESSAGE);
		
	}

	public void initialize() {
		// this method was meant to add the Controller listeners to View, but we're
		// changing it so that the Listeners are in the View instead
		
		JFrame frame = new JFrame();
		frame.add(this);
		frame.pack();
		frame.setResizable(false);     
		
		frame.setTitle("Wetlands Game");
		frame.setLayout(new GridLayout(1,1));
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(screenSize);
		//frame.setSize(700, 365);
		//frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.addMouseListener(this);
		frame.addKeyListener(this);
		addKeyListener(this);

	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//draw background image, need to find another image to make fullscreen
		g2d.drawImage(bgimg, 0, 0, getWidth(), getHeight(), null);
		
		// draw each of the game objects
		for (GroundPatch gr : groundList) {
			g2d.drawImage(groundimg, gr.getXloc(), gr.getYloc(), null);
		}
		for (Obstacle o : obstacleList) {
			g2d.drawImage(rockimg, o.getXloc(), o.getYloc(), null);
		}
		for (InvasivePlant in : invasivePlants) {
			g2d.drawImage(iplantimg, in.getXloc(), in.getYloc(), null);
		}
		for (NativePlant n : nativePlants) {
			g2d.drawImage(nplantimg, n.getXloc(), n.getYloc(), null);
		}
		//draw the playerimage
		g2d.drawImage(playerimg, player.getXloc(), player.getYloc(), null);

	
	}
	
	//set view's controller to match input controller
	public void setControl(Controller c) {
		control = c;
	}

	// set player location to match input player
	public void setPlayer(PlayerCharacter p) {
		player.setXloc(p.getXloc());
		player.setYloc(p.getYloc());
	}

	// set list of native plants to input
	public void setNativePlants(ArrayList<NativePlant> n) {
		nativePlants.clear();
		nativePlants.addAll(n);
	}

	// set list of invasive plants to input
	public void setInvasivePlants(ArrayList<InvasivePlant> in) {
		invasivePlants.clear();
		invasivePlants.addAll(in);
	}

	// set list of plantable ground to input
	public void setPatches(ArrayList<GroundPatch> g) {
		groundList.clear();
		groundList.addAll(g);
	}

	// set obstacles to input
	public void setObstacles(ArrayList<Obstacle> o) {
		obstacleList.clear();
		obstacleList.addAll(o);
	}

	// set tool to input
	public void setTool(Tool t) {
		tool = t;
	}



	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Mouse clicked");
		control.click(arg0.getX(),arg0.getY());
		//plantedCount++;
		paintPlantedInfo(plantedCount, arg0.getX(), arg0.getY());
		
		
		
	}

	public int getPlantedCount() {
		return plantedCount;
	}

	public void setPlantedCount(int plantedCount) {
		this.plantedCount = plantedCount;
	}

	public int getPlantsRemoved() {
		return plantsRemoved;
	}

	public void setPlantsRemoved(int plantsRemoved) {
		this.plantsRemoved = plantsRemoved;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Key pressed");
		control.key(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Key pressed");
		control.key(e);
	}
	//**--------------------------Testing-----------------------------**//
	public void printStuff() {
		// For testing purposes: prints the location of all the objects to console
		System.out.println("Your location is: " + player.getXloc() + ", " + player.getYloc());
		for (GroundPatch gr : groundList) {
			System.out.println("Plantable ground at " + gr.getXloc() + " and " + gr.getYloc());

		}
		for (NativePlant nat : nativePlants) {
			System.out.println("Goldenrod" + " at " + nat.getXloc() + " and " + nat.getYloc());
		}
		for (InvasivePlant nat : invasivePlants) {
			System.out.println("Phragmites" + " at " + nat.getXloc() + " and " + nat.getYloc());
		}
		for (Obstacle ob : obstacleList) {
			System.out.println("Obstacle" + " at " + ob.getXloc() + " and " + ob.getYloc());
		}
	}

}
