package src.windows;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import src.connections.Server;

public class ServerWindow  extends JFrame{

	private static final long serialVersionUID = 42;
	
	public Container swFrame; // container que guarda os objetos
	public JLabel lbServerIp;	// label da ip do server
	public JTextField tfServerIp; // textfield do server, contem o ip do server
	public JLabel lbServerPort;	// label da porta do server
	public JTextField tfServerPort;	// textfield para inserir a porta do server
	public JButton btReturn;	// bot�o para retornar a tela inicial
	public JButton btStartServer;	// bot�o para criar o server com os dados passados(chama m�todo)
	
	// M�todo construtor da tela criar servidor
	public ServerWindow() throws UnknownHostException{
		
		// Determina nome Janela
		super("Iniciando um Servidor");
		
		// Cria um Container
		swFrame = this.getContentPane();
		swFrame.setLayout(null);
		
		// Cria Label sobre Campo IP do Servidor
		lbServerIp = new JLabel("Digite o seu IP:");
		lbServerIp.setBounds(10, 10, 100, 50);
		
		InetAddress serverIp = InetAddress.getLocalHost();
		
		// Cria TextField sobre Campo IP do Servidor. Que vai receber informa��es
		// do usu�rio
		tfServerIp = new JTextField(serverIp.getHostAddress());
		tfServerIp.setBounds(105, 10, 125, 50);
		tfServerIp.setEnabled(false);
		
		// Cria Label sobre Campo Porta do Servidor
		lbServerPort = new JLabel("Digite a sua Porta:");
		lbServerPort.setBounds(10, 60, 120, 50);
		
		// Cria TextField sobre Campo Porta do Servidor. Que vai receber informa��es
		// do usu�rio
		tfServerPort = new JTextField();
		tfServerPort.setBounds(125, 60, 105, 50);
		
		// Cria Bot�o respons�vel por retornar a Tela Inicial
		btReturn = new JButton("Cancelar");
		btReturn.setBounds(10, 115, 80, 40);
		btReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	setVisible(false);
                new BeginWindow();
            }
        });
		
		// Cria Bot�o respons�vel por enviar dados para Startar o Servidor
		btStartServer = new JButton("Iniciar");
		btStartServer.setBounds(150, 115, 80, 40);
		btStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Server server = new Server(tfServerPort.getText());
				setVisible(false);
				server.start();
				new BoardWindow("Server", server, null);
			}
		});
		
		// Adiciona Bot�es, Labels e Textfields ao Container
		swFrame.add(lbServerIp);
		swFrame.add(tfServerIp);
		swFrame.add(lbServerPort);
		swFrame.add(tfServerPort);
		swFrame.add(btStartServer);
		swFrame.add(btReturn);
				
		// Configura detalhes do Frame
		// 1. Encerrar Applica��o ao Fechar
		// 2. Setar as dimens�es do Frame
		// 3. Setar Frame como Vis�vel
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 1
		this.setSize(240, 180); // 2
		this.setVisible(true); // 3
		
	}

}
