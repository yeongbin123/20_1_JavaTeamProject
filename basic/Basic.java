package basic;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.Main_Frame;

@SuppressWarnings("serial")
public class Basic extends JPanel{
	private int hp = 100 ;
	private int attack_d = 5 ;//attack damage
	// private static final double attack_s = 5.0;
	private int x = 0 ;
	private int y = 0 ;
	private Main_Frame win ;
	/*
	boolean KeyUp = false ;
	boolean KeyDown = false ;
	boolean KeyLeft = false ;
	boolean KeyRight = false ;
	boolean KeySpace = false ;
	*/
	
	public void init(int x, int y, int hp, int damage, Main_Frame win) {//(basic setting)
		this.x = x;
		this.y = y;
		this.hp = hp ;
		this.attack_d = damage ;
		this.win = win ;
	}
	
	/*
	public void KeyProcess() {
		if(KeyUp == true)  y -= 5;
		if(KeyDown == true) y += 5;
		if(KeyLeft == true)  x -= 5;
		if(KeyRight == true) x += 5;
	}
	*/
	
	public int attack(int damage) {//hp
		this.hp -= damage ;
	 
		return this.hp ; 	 
	}
	
	public int getDamage() { return this.attack_d ; }
	public int getHp() { return this.hp; }
	public int getX() { return this.x ; }
	public int getY() { return this.y ; }
	public void setX(int x) { this.x = x ; }
	public void setY(int y) { this.y = y ; }
	
}

/*
class Attack{
	Point pos;
	Attack(int x, int y){
		pos = new  Point(x,y);
	}
	public void move() {
		pos.x+=10;
	}
}

class Enemy{
int x;
int y;

Enemy(int x, int y){
this.x = x;
this.y = y;
}
public void move(){
x -= 3;
}
}
*/