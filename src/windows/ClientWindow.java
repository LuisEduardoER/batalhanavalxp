package windows;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ClientWindow extends JFrame{

	private static final long serialVersionUID = 42;

	public ClientWindow(){
		super("Acessando um Servidor");
		
		// Cria um Container
		Container cwFrame = this.getContentPane();
		cwFrame.setLayout(null);
		
		// Cria Label sobre Campo IP do Servidor a ser acessado
		JLabel lbClientIp = new JLabel("Digite o IP:");
		lbClientIp.setBounds(10, 10, 100, 50);
		
		// Cria TextField sobre Campo IP do Servidor a ser acessado. Que vai receber informa��es
		// do usu�rio
		JTextField tfClientIp = new JTextField("");
		tfClientIp.setBounds(105, 10, 125, 50);
		
		// Cria Label sobre Campo Porta do Servidor a ser acessado
		JLabel lbClientPort = new JLabel("Digite a Porta:");
		lbClientPort.setBounds(10, 60, 120, 50);
		
		// Cria TextField sobre Campo Porta do Servidor a ser acessado. Que vai receber informa��es
		// do usu�rio
		JTextField tfClientPort = new JTextField("");
		tfClientPort.setBounds(125, 60, 105, 50);
		
		// Cria Bot�o respons�vel por retornar a Tela Inicial
		JButton btReturn = new JButton("Cancelar");
		btReturn.setBounds(10, 115, 80, 40);
		btReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	setVisible(false);
                new BeginWindow();
            }
        });
		
		// Cria Bot�o respons�vel por enviar dados para Startar o Servidor
		JButton btAccessServer = new JButton("Acessar");
		btAccessServer.setBounds(150, 115, 80, 40);
		
		// Adiciona Bot�es, Labels e Textfields ao Container
		cwFrame.add(lbClientIp);
		cwFrame.add(tfClientIp);
		cwFrame.add(lbClientPort);
		cwFrame.add(tfClientPort);
		cwFrame.add(btAccessServer);
		cwFrame.add(btReturn);
				
		// Configura detalhes do Frame
		// 1. Encerrar Applica��o ao Fechar
		// 2. Setar Frame como Vis�vel
		// 3. Setar as dimens�es do Frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 1
		this.setVisible(true); // 2
		this.setSize(240, 180); // 3
	}

}
