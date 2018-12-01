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
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	ArrayList<Integer> keys=new ArrayList();
	
	JFrame frame;
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenHeight = screenSize.getSize().height;
	int screenWidth = screenSize.getSize().width;

	Image playerimg;
	Image groundimg;
	Image nplantimg;
	Image iplantimg;
	Image rockimg;
	Image backgroundimg;
	Image scaled_bg_img;
	Image menuimg;
	Image scaled_bg_img_menu;
	
	//Menu
	private Menu menu;
	
	//EndScreen
	private EndScreen endScreen;
	
	ToolBar toolbar;
    
	//TODO: Will use these to check for multiple key presses
	boolean isUpPressed, isDownPressed, isRightPressed,isLeftPressed;
	boolean[] keyArray = new boolean[4];
	
    
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
		ImageIcon bgImage = new ImageIcon("images/grass_template2.jpg");
		ImageIcon bgMenuImage = new ImageIcon("images/background.png");
		
		ImageIcon playericon = new ImageIcon("images/player.png");
		ImageIcon groundicon = new ImageIcon("images/ground.png");
		ImageIcon nplanticon = new ImageIcon("images/nativeplant.png");
		ImageIcon iplanticon = new ImageIcon("images/phragmites.png");
		ImageIcon rockicon = new ImageIcon("images/rock.png");
		
		playerimg = playericon.getImage();
		groundimg = groundicon.getImage();
		nplantimg = nplanticon.getImage();
		iplantimg = iplanticon.getImage();
		rockimg = rockicon.getImage();
		backgroundimg = bgImage.getImage();
		menuimg = bgMenuImage.getImage();
		
		//scale image to screen size
		scaled_bg_img = backgroundimg.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);   
		scaled_bg_img_menu = menuimg.getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);   
	}
	//state of game
	public enum STATE{
		MENU,
		GAME,
		END
	};
	
	//Initialize state to Menu, to skip menu for testing change state to GAME
	public static STATE State = STATE.MENU;

	public void initialize() {
		// this method was meant to add the Controller listeners to View, but we're
		// changing it so that the Listeners are in the View instead
		
			frame = new JFrame();
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
			
			addKeyListener(this); 	//makes player move		

			//Menu stuff
			menu= new Menu();
			
			//Toolbar
			toolbar = new ToolBar(frame);
	    //   frame.add(toolbar);
			
			 JButton button = new JButton("Click here!");
		     JPanel panel = new JPanel();
		     panel.add(button);
		        // And JPanel needs to be added to the JFrame itself!
		        this.getRootPane().add(panel);

		        setVisible(true);
			
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//draw background
		//g2d.drawImage(scaled_bg_img, 0, 0, null);

		//if in GAME state
		if(State == STATE.GAME) {
			g2d.drawImage(scaled_bg_img, 0, 0, null);
			
			//add toolbar to game, doesnt work
//			toolbar = new ToolBar();
//			frame.add(toolbar);
			
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
		
		}else if(State ==STATE.MENU) {//if game state is not in game,draw menu
			g2d.drawImage(scaled_bg_img_menu, 0, 0, null);
			menu.render(g);
		
		}else if(State == STATE.END) {	//if game is ended
			//g2d.drawImage(backgroundimg, 0, 0, null);
			endScreen.render(g);
		}
		
	
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int mx = e.getX();	//x value of mouse
		int my = e.getY();	//y value of mouse
		
		if(State == STATE.GAME) {
		control.click(mx,my);
		}
		else {System.out.println("Mouse clicked");
			
		}
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		int mx = e.getX();	//x value of mouse
		int my = e.getY();	//y value of mouse
		
		if(State == STATE.MENU) 
		{
			if(mx >= screenWidth/2-35 && mx <= screenWidth/2 +65 )
			{	//first button
				if(my >= 150 && my <= 200)
				{
					//Pressed play button
					View.State = View.STATE.GAME;
				}
				//second button
				if(my >= 250 && my <= 300)
				{
					//Pressed quit button
					System.exit(1);
				}
				//third button
				if(my >= 350 && my <= 400)
				{
					//Pressed other button
					System.exit(1);
				}
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		control.key(e);
	 
		System.out.println("Key pressed");
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Key pressed");
		control.key(e);
	}
	
	
	
//**-------------------------------Testing-----------------------------------------------**//
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
