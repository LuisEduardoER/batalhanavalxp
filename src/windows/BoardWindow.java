package src.windows;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import src.main.*;
import src.connections.*;

public class BoardWindow extends JFrame{
	
	private static final long serialVersionUID = 42;
	
	protected Container bwFrame; // container que guarda objetos
	//public DefaultTableCellRenderer io_rd_renderer;
	protected JPanel panel;	// container que guarda pane
	public JTable table;	// tabela que guarda posi��o das pe�as
	public JTable tableEnemy;	// tabela que guarda os tiros dados
	protected JTable tableScore;	// tabela que guarda pontua��o
	protected JScrollPane pane;		// container que guarda table das pe�as
	protected JScrollPane paneEnemy;	// containerq que guarda table dos tiros enviados
	protected JScrollPane paneScore;	// container que guarda table da pontua��p
	protected String type; // Determinar se � Cliente ou Servidor
	protected JTextField tf1c = new JTextField();  // textfield onde ser� inserido a coluna do tiro
	protected JTextField tf1r = new JTextField();  // textfield onde ser� inserido a linha do tiro 
	protected Object[] message;	// objeto que guarda mensagem
	protected Object[] errorMessage;	// objeto que guarda mensagem de erro
	protected JLabel player1 = new JLabel("Seu Tabuleiro  --->");	// label indicativo
	protected JLabel player2 = new JLabel("Respostas ------>");	// label indicativo
	protected JButton btShot;	// bot�o iniciar disparo (chama m�todo)
	protected Server socketServer; // guarda referencia do objeto server
	protected Client socketClient;	// guarda referencia do objeto client
	public Board board;	// guarda referencias dos objetos
	public List<String> shots = new ArrayList<String>(); // arraylist contendo as posi��es enviadas
	
	// M�todo construtor da Tela que contem os Tabuleiros
	public BoardWindow(int[][] tabuleiro, String gameType, Server server, Client client){
		
		// Determina nome Janela
		super("Batalha Naval XP");
		
		// Cria um Container
		bwFrame = this.getContentPane();
		
		// Guarda qual o modo que est� rodando
		setType(gameType);
		
		// Cria um Painel
        panel = new JPanel();
        
        // Cria uma Tabela
        table = new JTable(11, 11);
        tableEnemy = new JTable(11,11);
        tableScore = new JTable(3, 4);
        
        // Determina a cor das bordas da tabela
        Color c = Color.BLACK;
        table.setGridColor(c);
        tableEnemy.setGridColor(c);
        tableScore.setGridColor(c);
        
        // Colorir coluna
        //Color index = Color.GREEN;
        //io_rd_renderer.setBackground(index);
        //table.getColumnModel().getColumn(0).setCellRenderer(io_rd_renderer);
        
        // Retira o cabe�alho da tabela
        table.setTableHeader(null);
        tableEnemy.setTableHeader(null);
        tableScore.setTableHeader(null);
        
        // Exibe as bordas da tabela
        table.setShowGrid(true);
        tableEnemy.setShowGrid(true);
        tableScore.setShowGrid(true);
        
        // N�mera a primeira coluna e linha
        setFirstColumn(table);
        setFirstRow(table);
        
        setFirstColumn(tableEnemy);
        setFirstRow(tableEnemy);
        
        mountTableScore(tableScore);
        
        // Cria um frame para colocar a tabela e ajusta o seu tamanho
        
        // Tabela "Seu tabuleiro"
        pane = new JScrollPane(table);
        pane.getViewport().setPreferredSize(table.getPreferredSize());
        
        // Tabela "Respostas"
        paneEnemy = new JScrollPane(tableEnemy);
        paneEnemy.getViewport().setPreferredSize(tableEnemy.getPreferredSize());
        
        // Tabela de Pontua��o
        paneScore = new JScrollPane(tableScore);
        paneScore.getViewport().setPreferredSize(tableScore.getPreferredSize());
        
        // Bot�o para Iniciar Disparo
        btShot = new JButton("Iniciar Disparo");
        btShot.setSize(50, 50);
        btShot.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
            {
        		// Instancia Objeto a ser enviado por Socket
        		Shot shot = new Shot();
        		
        		int check = 1;
            	int error = 0;
            	
            	// Enquanto n�o foi apertado o bot�o certo e os dados n�o estiverem corretos, o aplicativo ir� solicitar coordenadas
            	while (check != 0){
            		
            		// Limpa poss�veis rastros nos JTexts
            		tf1c.setText(null);
        			tf1r.setText(null);
        			
        			// Cria campos para Inser��o
        			message = new Object[] {  
        					"Coordenadas do Tiro","Coluna", tf1c, "Linha", tf1r};
        		
        			// Cria Mensagem de Erro
        			errorMessage = new Object[] {"Verifique os coordenadas inseridos, pois existem coordenadas inv�lidas.\n" +
													"Coordenadas v�lidas s�o de 1 a 10. Letras n�o s�o v�lidas\n" +
        											"Coordenadas repetidas."};
        				
        			// Solicita dados aos usu�rio
        			check = JOptionPane.showConfirmDialog(null, message, "Inserir coordenada do Tiro", JOptionPane.OK_OPTION);
        			
        			// Se o bot�o pressionado for Sim
        			if (check == 0){
        				
        				// Faz valida��o do tiro. Se for v�lidado envia tiro pelo Socket. Se n�o exibe mensagem de erro
        				if (validateShot(tf1r.getText(), tf1c.getText(), shots)){
        					
        					// Cria Objeto a ser enviado
        					shot.createShot(tf1r.getText(), tf1c.getText(), board);
        					
        					try {
        						
        						// Verifica se � Cliente ou Servidor. Desabilita o Bot�o para n�o enviar mais de um tiro e envia objeto por Socket
        						if (type.equals("Server")){
        							btShot.setEnabled(false);
        							socketServer.sendObject.flush();
        							socketServer.sendObject.writeObject(shot);
        							socketServer.sendObject.flush();
        						}
        						
        						if (type.equals("Client")){
        							btShot.setEnabled(false);
        							socketClient.sendObject.flush();
        							socketClient.sendObject.writeObject(shot);
        							socketClient.sendObject.flush();
        						}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
        				}else {
        					// Mensagem de erro
    						error = JOptionPane.showConfirmDialog(null, errorMessage, "Erro ao inserir Coordenadas", JOptionPane.CANCEL_OPTION);
    						check = 1;
        				}
        			}	
            	}
            }
        });
        
        // Adiciona frame ao painel
        panel.add(player1);
        panel.add(pane);
        panel.add(player2);
        panel.add(paneEnemy);
        panel.add(paneScore);
        panel.add(btShot);
        panel.setAlignmentX(CENTER_ALIGNMENT);
        
        // Adiciona o painel ao container
        bwFrame.add(panel);
        
        // Instancia Objeto Board
        board = new Board();
        
        // Inicia o jogo pelo Servidor
        if (type.equals("Client")){
        	btShot.setEnabled(false);
        }
        
        // Adiciona as refer�ncias aos objetos que ser�o usados pelas Classes na thread
        if (getType().equals("Server")){
			socketServer = server;
			socketServer.setServerTable(table);
			socketServer.setServerScore(tableScore);
			socketServer.setServerButton(btShot);
			socketServer.setServerTableAnswer(tableEnemy);
		}else{
			socketClient = client;
			socketClient.setClientTable(table);
			socketClient.setClientScore(tableScore);
			socketClient.setClientButton(btShot);
			socketClient.setClientTableAnswer(tableEnemy);
		}
			
        // Configura detalhes do Frame
        // 1. Encerrar Applica��o ao Fechar
        // 2. Setar Frame como Vis�vel
        // 3. Setar as dimens�es do Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 1
        this.setVisible(true); // 2
        this.setSize(1024, 460); // 3		
    	
        // Percorre matriz do tabuleiro preenchida com valores dos barcos posicionados pelo usuario
    	for( int linha = 1; linha < 10; linha++ )
        {
        	for( int coluna = 1; coluna < 10; coluna++ )
        	{
        		// Se houver algum barco na posicao corrente, exibe na celula da tabela
        		if ( tabuleiro[linha][coluna] > 0 )
        		{
        			// Coloca as Iniciais dos Barcos(P, Q, T, D e S)
        			switch (tabuleiro[linha][coluna]){
	        			case 1:
	        			{
	        				table.setValueAt("S", linha, coluna);
	        				break;
	        			}
	        			case 2:
	        			{
	        				table.setValueAt("D", linha, coluna);
	        				break;
	        			}
	        			case 3:
	        			{
	        				table.setValueAt("T", linha, coluna);
	        				break;
	        			}
	        			case 4:
	        			{
	        				table.setValueAt("Q", linha, coluna);
	        				break;
	        			}
	        			case 5:
	        			{
	        				table.setValueAt("P", linha, coluna);
	        				break;
	        			}
        			}
        			
        		}
        	}		
        }
    	
	}

	// Seta valores para a Primeira Linha
	public void setFirstRow(JTable table){
		
		for (int i = 0; i <= 10; i++) {
			table.setValueAt(String.valueOf(i), i, 0);
		}		
	}
	
	// Seta valores para a Primeira Coluna
	public void setFirstColumn(JTable table){
		
		for (int i = 0; i <= 10; i++) {
			table.setValueAt(String.valueOf(i), 0, i);
		}
	}
	
	// Retorna a vari�vel Type
	public String getType() {
		return type;
	}

	// Seta vari�vel Type
	public void setType(String type) {
		this.type = type;
	}
	
	// M�todo que verifica se tem algum zero
	public boolean hasZeros(String row, String column){
		
		boolean check = false; 
		
		if (row.equals("0") || column.equals("0")){
			 return true;
		}
		
		return check;
	}
	
	// M�todo que verifica se tem alguma letra
	public boolean hasLetters(String row, String column){
		
		boolean check = false;
		
		Pattern pattern = Pattern.compile("[0-9]");  
        Matcher matchRow = pattern.matcher(row);
        Matcher matchColumn = pattern.matcher(column);
          
        if(!matchRow.find() || !matchColumn.find()) {   
			return true;
		}
		
		return check;
	}
	
	// M�todo que verifica se tem algum valor ""
	public boolean hasNull(String row, String column){

		boolean check = false;
		
		if (row.equals("") || column.equals("")){
			return true;
		}
		
		return check;
		
	}
	
	// Verifica se as coordenadas s�o v�lidas
	public boolean isCoordenatesOk(String row1, String column1, String row2, String column2){
		
		boolean check = false;
		
		if (Integer.parseInt(row1) - 1 == Integer.parseInt(row2) || Integer.parseInt(row1) + 1 == Integer.parseInt(row2)) {
			check = true;
		}
		
		if (Integer.parseInt(column1) - 1 == Integer.parseInt(column2) || Integer.parseInt(column1) + 1 == Integer.parseInt(column2)) {
			check = true;
		}
		
		if (row1.equals(row2) && column1.equals(column2)){
			check = false;
		}
		
		return check;
	}
	
	// Verifica se nas coordenadas enviadas, n�o existe nenhuma pe�a
	public boolean isPositionOk(JTable table, String row1, String column1, String row2, String column2){
	
		boolean check = false;
		
		if (table.getValueAt(Integer.parseInt(row1), Integer.parseInt(column1)) == null &&
				table.getValueAt(Integer.parseInt(row2), Integer.parseInt(column2)) == null){
			check = true;
		}
		
		return check;
	}
	
	// Monta as Colunas e Linhas da tabela com pontua��o
	public void mountTableScore(JTable table){
		
		table.setValueAt("Seus Pontos", 1, 0);
		table.setValueAt("Pontos Rival", 2, 0);
		
		table.setValueAt("Qntd. Tiros", 0, 1);
		table.setValueAt("Acertos", 0, 2);
		table.setValueAt("Restam", 0, 3);
	}
	
	// M�todo para valida��o do Tiro
	public boolean validateShot(String row, String column, List<String> shots){
		
		boolean check = false;
		
		// Verifica se essas coordenadas j� foram usadas. Se foram usadas avisa usu�rio
		if (!shots.isEmpty()){
			
			for (int i = 0; i < shots.size(); i++) {
				
				if (shots.get(i).equals(column + row)){
					
					return false;
				}
			}		
		}
		
		// Se coordenadas n�o foram usadas, guarda em um List
		shots.add(column + row);
		
		// Verifica se as coordenadas n�o tem Zeros, ou Letras ou passam do Limite do Tabuleiro
		if (!hasZeros(row, column) && !hasLetters(row, column) && !hasNull(row, column) && (Integer.parseInt(row)<=10)
				&& (Integer.parseInt(row)>0) && (Integer.parseInt(column)<=10) && (Integer.parseInt(column)>0)){
			
			check = true;
		}
		return check;
	}
	
	
}
