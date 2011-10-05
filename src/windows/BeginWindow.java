package src.windows;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;

public class BeginWindow extends JFrame{

	private static final long serialVersionUID = 42;
	
	public Container bwFrame; // container que guarda os objetos swing
	public JButton btServer; // bot�o que redireciona para tela de criar server
	public JButton btClient;	// bot�o que redireciona para tela de acessar server
	
	// M�todo construtor da tela inicial
	public BeginWindow(){
			
			// Determina nome Janela
			super("Batalha Naval XP");
			
			// Cria um Container
			bwFrame = this.getContentPane();
			bwFrame.setLayout(null);
			
			// Cria Bot�o respons�vel por iniciar o Processo de Startar o Servidor
			btServer = new JButton ("Iniciar um novo Servidor");
			btServer.setBounds(30, 10, 180, 50);
			btServer.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	            	setVisible(false);
	                try {
						new ServerWindow();
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
	        }); 
			
			// Cria Bot�o respons�vel para entrar em um Servidor j� Startado
			btClient = new JButton ("Acessar um Servidor");
			btClient.setBounds(30, 80, 180, 50);
			btClient.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	            	setVisible(false);
	                new ClientWindow();
	            }
	        });
			
			// Adiciona Bot�es ao Container
			bwFrame.add(btServer);
			bwFrame.add(btClient);
			
			// Configura detalhes do Frame
			// 1. Encerrar Applica��o ao Fechar
			// 2. Setar as dimens�es do Frame
			// 3. Setar Frame como Vis�vel
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 1
			this.setSize(240, 180); // 2
			this.setVisible(true); // 3
		}
}