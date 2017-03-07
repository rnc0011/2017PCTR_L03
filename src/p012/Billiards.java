package p012;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Billiards extends JFrame {

	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;
	
	private Board board;

	// TODO update with number of group label. See practice statement.
	private final int N_BALL = 3 + 3;
	
	// Vector en el que se van a guardar las bolas
	private Ball[] balls = new Ball[this.N_BALL];
	// Vector en el que se van a guardar los hilos
	protected Thread[] threads = new Thread[this.N_BALL];

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Práctica programación concurrente objetos móviles independientes");
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Método que inicializa el vector de bolas.
	 * 
	 * @author Raúl Negro Carpintero
	 * @auhtor Mario Núñez Izquierdo
	 * @param -
	 * @return -
	 */
	private void initBalls() {
		for (int i = 0; i < this.N_BALL; i++){
			balls[i] = new Ball();
		}
	}
	
	/**
	 * Método que crea e inicializa los hilos.
	 * 
	 * @author Raúl Negro Carpintero
	 * @author Mario Núñez Izquierdo
	 * @param bola
	 * @return Thread
	 */
	protected Thread makeThread (final Ball bola){
		Runnable bucle = new Runnable() {
			public void run(){
				try{
					for(;;) {
						bola.move();
						bola.reflect();
						board.repaint();
						Thread.sleep(10);
					}
				}catch(InterruptedException e){
					return ;
				}
			}
		};
		return new Thread (bucle);
	}
	
	/**
	 * Clase del botón Empezar.
	 * 
	 * @author Raúl Negro Carpintero
	 * @author Mario Núñez Izquierdo
	 *
	 */
	private class StartListener implements ActionListener {
		@Override
		/**
		 * Método del botón Empezar.
		 * 
		 * @author Raúl Negro Carpintero
		 * @auhtor Mario Núñez Izquierdo
		 * @param arg0
		 * @return -
		 */
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when start button is pushed
			board.setBalls(balls);
			for(int i = 0; i < N_BALL; i++){
				threads[i] = makeThread(balls[i]);
				threads[i].start();
			}
		}
	}

	/**
	 * Clase del botón Parar.
	 * 
	 * @author Raúl Negro Carpintero
	 * @author Mario Núñez Izquierdo
	 *
	 */
	private class StopListener implements ActionListener {
		@Override
		/**
		 * Método del botón Parar.
		 * 
		 * @author Raúl Negro Carpintero
		 * @auhtor Mario Núñez Izquierdo
		 * @param arg0
		 * @return -
		 */
		public void actionPerformed(ActionEvent arg0) {
			// TODO Code is executed when stop button is pushed
			if(threads != null){
				for(int i = 0; i < N_BALL; i++){
					threads[i].interrupt();
					threads[i] = null;
				}
			}
		}
	}

	public static void main(String[] args) {
		new Billiards();
	}
}