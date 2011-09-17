package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.UnknownHostException;

public class BeginWindow extends JFrame{

	private static final long serialVersionUID = 42;

	public BeginWindow(){
			
			// Determina nome Janela
			super("Batalha Naval");
			
			// Cria um Container
			Container bwFrame = this.getContentPane();
			bwFrame.setLayout(null);
			
			// Cria Bot�o respons�vel por iniciar o Processo de Startar o Servidor
			JButton btServer = new JButton ("Iniciar um novo Servidor");
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
			JButton btClient = new JButton ("Acessar um Servidor");
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
			// 2. Setar Frame como Vis�vel
			// 3. Setar as dimens�es do Frame
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 1
			this.setVisible(true); // 2
			this.setSize(240, 180); // 3
		}
}