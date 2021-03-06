package thread;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import basic.Basic;
import basic.Monster;
import main.Main;
import main.Main_Frame;
import manage.manage_data;
import panel.EndGame;
import panel.Game;
import panel.M_Missile;
import panel.Missile;
import panel.Start;

@SuppressWarnings("serial")
public class RunGame extends JFrame implements Runnable, KeyListener {
	private Thread th;
	private JPanel game;
	private Main_Frame win;
	private Basic user;
	private JLabel[] status = new JLabel[3]; // score board (Hp, score, time)
	private int score;
	private boolean repeat = true;
	private int cnt = 0;
	private long seed = System.currentTimeMillis();
	private Random rand = new Random(seed);
	private int ran;
	private int Missile_speed = 6;
	private boolean check = true;

	private boolean KeyUp = false;
	private boolean KeyDown = false;
	private boolean KeyLeft = false;
	private boolean KeyRight = false;
	private boolean KeySpace = false;

	private ArrayList<Missile> Aplus_List = new ArrayList<>();
	private ArrayList<M_Missile> Cplus_List = new ArrayList<>();
	private ArrayList<M_Missile> Dplus_List = new ArrayList<>();
	private ArrayList<M_Missile> Fplus_List = new ArrayList<>();

	private Missile AInfo;
	private M_Missile CInfo;
	private M_Missile DInfo;
	private M_Missile FInfo;
	
	private Image Aplus = new ImageIcon(Start.class.getResource("../image/a_30.png")).getImage();
	private Image Cplus = new ImageIcon(Start.class.getResource("../image/c_30.png")).getImage();
	private Image Dplus = new ImageIcon(Start.class.getResource("../image/d_35.png")).getImage();
	private Image Fplus = new ImageIcon(Start.class.getResource("../image/f_40.png")).getImage();

	private ArrayList<Monster> Mon_List = new ArrayList<>();
	private Monster mon;
	private Image mon1 = new ImageIcon(Game.class.getResource("../Image/monster1.png")).getImage();
	private Image mon2 = new ImageIcon(Game.class.getResource("../Image/monster2.png")).getImage();
	private Image mon3 = new ImageIcon(Game.class.getResource("../Image/monster3.png")).getImage();
	private Image temp;
	private long startTime=2*60*1000+System.currentTimeMillis();
	private final java.text.SimpleDateFormat timerFormat = new java.text.SimpleDateFormat("mm : ss : SSS");
	private long elapsed = 1;
	private int loop; // control monster's missile loop
	private int did; // if monster's missile loop is reduced did = 1;

	private manage_data result = new manage_data();
	
	@SuppressWarnings("unused")
	private Graphics pan;

	public RunGame(Main_Frame win, JPanel game, Basic p, JLabel[] status) {
		this.win = win;
		this.user = p;
		this.game = game;
		
		score = 0; // initial score
		loop = 80; // initial loop for monster's missile loop
		did = 0; // initial did for monster's missile loop's change

		for (int i = 0; i < 3; i++) {
			this.status[i] = status[i]; // store in board
		}

		win.addKeyListener(this);
		win.setFocusable(true);

		pan = win.getGraphics();

		th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("thread create!");
		//Time timer = new Time();
		try {
			while (repeat) {
				show_status(startTime - System.currentTimeMillis());
				KeyProcess();
				MissileProcess();
				MonsterProcess();
				Enemy_MissileProcess();
				EndGameProcess();
				
				Thread.sleep(20);
				cnt++;
			}
		} catch (Exception e) {
		}
	}

	public void kill() {
		System.out.println("thread kill!");
		repeat = false;
	}

	private void KeyProcess() {
		// TODO Auto-generated method stub
		if (KeyUp == true) {
			System.out.println("up");
			user.setY(user.getY() - 5);
		}
		if (KeyDown == true) {
			System.out.println("down");
			user.setY(user.getY() + 5);
		}
		if (KeyLeft == true) {
			System.out.println("left");
			user.setX(user.getX() - 5);
		}
		if (KeyRight == true) {
			System.out.println("right");
			user.setX(user.getX() + 5);
		}
		if (KeySpace == true) {
			System.out.println("attack");
		}

		if (user.getX() > Main.SCREEN_WIDTH - 25)
			user.setX(Main.SCREEN_WIDTH - 25);
		if (user.getX() < -15)
			user.setX(-15);
		if (user.getY() > Main.SCREEN_HEIGHT - 60)
			user.setY(Main.SCREEN_HEIGHT - 60);
		if (user.getY() < 0)
			user.setY(0);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = true;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = true;
			break;
		case KeyEvent.VK_LEFT:
			KeyLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			KeyRight = true;
			break;
		case KeyEvent.VK_SPACE:
			KeySpace = true;
			break;
		}

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = false;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = false;
			break;
		case KeyEvent.VK_LEFT:
			KeyLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			KeyRight = false;
			break;
		case KeyEvent.VK_SPACE:
			KeySpace = false;
			break;
		}
	}

	private void MissileProcess() {
		if (KeySpace) {
			AInfo = new Missile(user.getX() + 10, user.getY() + 15);
			Aplus_List.add(AInfo);
			KeySpace = false; // continue to press space bar is banned.
		}

		for (int i = 0; i < Aplus_List.size(); ++i) {
			AInfo = (Missile) Aplus_List.get(i);
			AInfo.move();
			for (int j = 0; j < Mon_List.size(); ++j) {
				mon = (Monster) (Mon_List.get(j));
				if (Crash(AInfo.getX() + 3, AInfo.getY() + 3, mon, user, 30, 90)) {
					System.out.println("a - mon");
					if (mon.getHp() <= 0) {
						mon.setX(-300);
						score += 100; // kill monster get score 100
					}
					mon.attack(user.getDamage());
					AInfo.setX(600);
				}
			}

			if (AInfo.getX() > 550) {
				Aplus_List.remove(i);
			}
		}
	}

	private void Enemy_MissileProcess() { // Enemy Missile Process
		if (cnt % loop == 0) {
			for (int i = 0; i < Mon_List.size() && Mon_List.get(i).getX() <= 550; i++) { // 미사일이 몬스터 등장한 후 나옴
				System.out.println("Missile speed is " + Missile_speed) ;
				switch (Mon_List.get(i).getDamage() / 10) {
				case 3:
					FInfo = new M_Missile(Mon_List.get(i).getX() - 10, Mon_List.get(i).getY() + 30, Missile_speed);
					Fplus_List.add(FInfo);

					break;
				case 2:
					DInfo = new M_Missile(Mon_List.get(i).getX() - 10, Mon_List.get(i).getY() + 30, Missile_speed);
					Dplus_List.add(DInfo);

					break;
				case 1:
					CInfo = new M_Missile(Mon_List.get(i).getX() - 10, Mon_List.get(i).getY() + 30, Missile_speed);
					Cplus_List.add(CInfo);

					break;
				}
			}
		}
	}

	public void Draw_Missile(Graphics pan) {
		for (int i = 0; i < Aplus_List.size(); ++i) {
			AInfo = (Missile) (Aplus_List.get(i));
			pan.drawImage(Aplus, AInfo.getX(), AInfo.getY(), win);
		}
	}

	public void Draw_Enemy_Missile(Graphics pan) { // Draw Enemy's Missile
		for (int j = 0; j < Fplus_List.size(); ++j) {
			FInfo = (M_Missile) (Fplus_List.get(j));
			pan.drawImage(Fplus, FInfo.getX(), FInfo.getY(), win);
			FInfo.move() ;
			if (Crash(FInfo.getX(), FInfo.getY(), user, user, 40, 50)) {
				System.out.println("user - f");
				user.attack(20);
				FInfo.setX(-30);
			}
			if (FInfo.getX() < -20) {
				Fplus_List.remove(j);
			}

		}

		for (int j = 0; j < Dplus_List.size(); ++j) {
			DInfo = (M_Missile) (Dplus_List.get(j));
			pan.drawImage(Dplus, DInfo.getX(), DInfo.getY(), win);
			DInfo.move();
			if (Crash(DInfo.getX(), DInfo.getY(), user, user, 35, 50)) {
				System.out.println("user - d");
				user.attack(15);
				DInfo.setX(-30);
			}
			if (DInfo.getX() < -20) {
				Dplus_List.remove(j);
			}
		}

		for (int j = 0; j < Cplus_List.size(); ++j) {
			CInfo = (M_Missile) (Cplus_List.get(j));
			pan.drawImage(Cplus, CInfo.getX(), CInfo.getY(), win);
			CInfo.move();
			if (Crash(CInfo.getX(), CInfo.getY(), user, user, 30, 50)) {
				System.out.println("user - c");
				user.attack(10);
				CInfo.setX(-30);
			}
			if (CInfo.getX() < -20) {
				Cplus_List.remove(j);
			}
		}
	}

	private void show_status(long elapsed) {
		
		status[0].setText("HP : " + user.getHp());
		if (user.getHp() <= 100) {
			status[0].setForeground(Color.RED);
		}
		status[1].setText("Score : " + score);
		status[2].setText("Time : " + timerFormat.format(elapsed));
		
		if(elapsed/1000 == 60 && check)
		{
			Missile_speed += 1 ;
			check = false ;
		}
		if(elapsed/1000 == 30 && check)
		{
			Missile_speed += 1 ;
			check = false ;
		}
		if(!(elapsed/1000 == 60) && !(elapsed/1000 == 30)) check = true ;
		
		if(elapsed <= 0) {
			elapsed = 0;
		}
		
		else if ((elapsed / 1000) % 30 == 0 && did == 0) { // every 30seconds missile's loop -= 10
			loop -= 10;
			did = 1;

		}
		else if ((elapsed / 1000) % 30 != 0 && did == 1){
			did = 0;
		}
	}

	private Image GetImage()
	{
		ran = rand.nextInt(50) + 1;
		if (rand.nextInt(50) + 1 < ran)
			return mon2;
		else if (rand.nextInt(50) + 1 > ran)
			return mon3;
		return mon1;

	}

	private void MonsterProcess() {
		for (int i = 0; i < Mon_List.size(); ++i) {
			mon = (Monster) (Mon_List.get(i));
			mon.setX(mon.getX() - 2 - mon.getSpeed());
			mon.setLocation(mon.getX(), mon.getY());
			if (Crash(user.getX(), user.getY(), mon, user, 50, 90) && !mon.getChecking()) {
				System.out.println("user - mon");
				user.attack(mon.getDamage());
				mon.setChecking(true);
			}
			if (mon.getX() < -200)
				Mon_List.remove(i);
		}

		if (cnt % 300 == 0) {
			System.out.println("Make a monster!");

			temp = GetImage();
			mon = new Monster() {
				public void paint(Graphics g) {
					g.drawImage(temp, 0, 0, null);
				};
			};
			mon.init(650, 80, 10, rand.nextInt(30) + 10, win, rand.nextInt(3) + 1);
			mon.setBounds(mon.getX(), mon.getY(), 90, 90);
			game.add(mon);
			Mon_List.add(mon);

			temp = GetImage();
			mon = new Monster() {
				public void paint(Graphics g) {
					g.drawImage(temp, 0, 0, null);
				};
			};
			mon.init(650, 180, 10, rand.nextInt(30) + 10, win, rand.nextInt(3) + 1);
			mon.setBounds(mon.getX(), mon.getY(), 90, 90);
			game.add(mon);
			Mon_List.add(mon);

			temp = GetImage();
			mon = new Monster() {
				public void paint(Graphics g) {
					g.drawImage(temp, 0, 0, null);
				};
			};
			mon.init(650, 280, 10, rand.nextInt(30) + 10, win, rand.nextInt(3) + 1);
			mon.setBounds(mon.getX(), mon.getY(), 90, 90);
			game.add(mon);
			Mon_List.add(mon);

			temp = GetImage();
			mon = new Monster() {
				public void paint(Graphics g) {
					g.drawImage(temp, 0, 0, null);
				};
			};
			mon.init(650, 380, 10, rand.nextInt(30) + 10, win, rand.nextInt(3) + 1); // 3 + alpha
			mon.setBounds(mon.getX(), mon.getY(), 90, 90);
			game.add(mon);
			Mon_List.add(mon);

		}

	}

	private boolean Crash(int GradeX, int GradeY, Basic character, Basic attackChar, int range, int size) {
		if (MatchingPoint(GradeX, GradeY, character.getX(), character.getY(), 30, size))
			return true;
		else if (MatchingPoint(GradeX + range, GradeY, character.getX(), character.getY(), 30, size))
			return true;
		else if (MatchingPoint(GradeX, GradeY + range, character.getX(), character.getY(), 30, size))
			return true;
		else if (MatchingPoint(GradeX + range, GradeY + range, character.getX(), character.getY(), 30, size))
			return true;
		else
			return false;
	}

	private boolean MatchingPoint(int msPointX, int msPointY, int charPointX, int charPointY, int msLen, int charLen) {
		if ((charPointX < msPointX && msPointX < charPointX + charLen)
				&& (charPointY < msPointY && msPointY < charPointY + charLen))
			return true;
		return false;
	}

	private void EndGameProcess() {
		if (user.getHp() < 0) {
			int temp = score;
			result.store_score(score - 1000); // store score
			kill();
			((Game) game).kill();
			win.endGame = new EndGame(win, temp, "HP");
			win.change("End Game");
		}
		if (elapsed <= 0 || startTime - System.currentTimeMillis() <= 0) {
			int temp = score;
			result.store_score(score + 1000); // store score
			kill();
			((Game) game).kill();
			win.endGame = new EndGame(win, temp, "TIME");
			win.change("End Game");
		}
	}

}
