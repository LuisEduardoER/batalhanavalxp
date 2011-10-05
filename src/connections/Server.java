package src.connections;

import java.io.*;
import java.net.*;
import src.main.*;
import javax.swing.*;

public class Server extends Thread implements Runnable{
	
	protected String ip;	// guarda ip
	protected String port;	// guarda porta
	public ServerSocket socketServer;	// objeto que criar o server do socket
	public Socket socketConnection;	// objeto que cria o socket
	public ObjectInputStream receiveObject;	// objeto que recebe objeto
	public ObjectOutputStream sendObject;	// objeto que envia objeto
	protected Shot shot = new Shot();	// objeto tiro
	protected Shot shotAnswer;	// objeto tiro resposta
	protected boolean hitAnswer;	// tag que guarda se � tiro resposta ou n�o
	protected int countShot;	// contador de tiro dados
	protected int countHit;	// contador de tiros que acertaram
	protected int countPoints = 15;	// contador dos tiros totais
	protected JTable serverTable; // guarda referencia da tabela que contem os barcos do server
	protected JTable serverTableAnswer;	//  guarda referencia da tabela que contem os tiros enviados pelo server
	protected JTable serverScore;	//  guarda referencia da tabela que contem a pontua��o do server
	protected JButton serverButton;	//  guarda referencia do bot�o iniciar disparo do server 
	protected Object[] errorMessage; // objeto que guarda mensagem de erro
	
	// M�todo Construtor que cria Server Socket
	public Server(String port){
		try {
			socketServer = new ServerSocket(Integer.parseInt(port));
			socketConnection = socketServer.accept();
			sendObject = new ObjectOutputStream(socketConnection.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setPort(port);
	}

	// Threads que recebe Objeto e envia Objeto
	// Como ela funciona:
	// 1 - Recebe o Objeto e verifica se ele � um Tiro Resposta ou um Tiro. Se n�o for um tiro resposta: pega as Refer�ncias o Tabuleiro do Servidor;
	// verifica quantos tiros j� foram dados; verifica se acertou ou n�o um barco e marca no tabuleiro do Servidor; faz a contagem de Tiros, Acertos 
	// e quantos resta para acabar o jogo; acerta a pontua��o do Servidor; monta o Tiro Resposta para o Cliente, para que ele possa marcar em sua 
	// Grade de Resposta assim como acertar a sua pontua��o; envia o Tiro Resposta para o Cliente; e liberar o Bot�o para Iniciar Disparo do Servidor.
	// ------------------------------------------------------------------------------------------------------------------------------------------------
	// 2 - Se o Tiro for um Tiro Resposta, ent�o: verifica se o tiro acertou ou n�o para determinar qual marca��o usar; atualizar a Grade de
	// Pontua��o
	public void run(){
		if (socketConnection.isConnected()){
			try {
				receiveObject = new ObjectInputStream(socketConnection.getInputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			while(true){
					try {
						// Recebe Objeto
						setShot((Shot) receiveObject.readObject());	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// Verifica se � Tiro Normal ou Tiro Resposta
					if (!getShot().isAnswer()){
						
						// Pega as Refer�ncias do Tabuleiro
						configureClientBoard(getShot());
						
						// Acerta a contagem de Tiros
						setCountShot(getCountShot() + 1);
						
						// Verifica se o Tiro acerta ou n�o um barco
						setHitAnswer(getShot().receiveShot(getShot().getRow(), getShot().getColumn(), shot.getBoard()));
						
						// Se Acertou incrementa a contagem
						if (isHitAnswer()){
							setCountHit(getCountHit() + 1);
							setCountPoints(getCountPoints() - 1);
						}
						
						// Acerta a pontua��o do Servidor
						getShot().getBoard().getEnemyScore().setValueAt(getCountShot(), 2, 1);
						getShot().getBoard().getEnemyScore().setValueAt(getCountHit(), 2, 2);
						getShot().getBoard().getEnemyScore().setValueAt(getCountPoints(), 2, 3);
						
						// Cria o Tiro Resposta
						setShotAnswer(new Shot());
						getShotAnswer().createShotAnswer(getShot().getRow(), getShot().getColumn(), true, isHitAnswer(), getCountShot(), getCountHit(), getCountPoints());
						
						try {
							// Envia Tiro Resposta
							sendObject.writeObject(getShotAnswer());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						int wipe;
						
						if (getCountPoints() == 0){
							
							interrupt();
							disconnect();
							
							// Cria Mensagem de Derrota
		        			errorMessage = new Object[] {"Voc� PERDEU! Clique em Ok para voltar a Tela Inicial. Novamente, voc� PERDEU!\n\n H�!"};
							wipe = JOptionPane.showConfirmDialog(null, errorMessage, "VOC� PERDEU!", JOptionPane.CANCEL_OPTION);
						}else{
						
							// Libera Bot�o para o Servidor poder Iniciar o Diparo
							getShot().getBoard().getEnemyButton().setEnabled(true);
						}
					}else {
						
						// Verifica se o Tiro Resposta acertou ou n�o. E marca no tabuleiro com determinada Marca
						if (getShot().isHit()){
							getServerTableAnswer().setValueAt("X", Integer.parseInt(getShot().getRow()), Integer.parseInt(getShot().getColumn())); // Acertou = X
						}else {
							getServerTableAnswer().setValueAt("O", Integer.parseInt(getShot().getRow()), Integer.parseInt(getShot().getColumn())); // Errou = O
						}
						
						// Atualiza Pontua��o do Servidor
						getServerScore().setValueAt(getShot().getCountShot(), 1, 1);
						getServerScore().setValueAt(getShot().getCountHit(), 1, 2);
						getServerScore().setValueAt(getShot().getCountPoints(), 1, 3);
						
						int wipe;
						
						if (getShot().getCountPoints() == 0){
							
							interrupt();
							disconnect();
							
							// Cria Mensagem de Derrota
		        			errorMessage = new Object[] {"Voc� GANHOU! Clique em Ok para voltar a Tela Inicial. FEITORIA!\n\n"};
							wipe = JOptionPane.showConfirmDialog(null, errorMessage, "FEIT�! VOC� GANHOU!", JOptionPane.CANCEL_OPTION);
						}
					}
				}
			}
	}
	
	// M�todo que checa se a PORT passada � v�lida ou n�o.
	public boolean checkAddress(String port){
	
		boolean statusAddress = false;
		
		InetSocketAddress socketAddress = new InetSocketAddress(Integer.parseInt(port));
	
		if (socketAddress.isUnresolved() == false){
			statusAddress = true;
		}
		
		return statusAddress;
	}

	// Alimenta o Board do Cliente com as referencias do Servidor
	public void configureClientBoard(Shot shot){
		
		getShot().getBoard().setEnemyScore(getServerScore());
		getShot().getBoard().setEnemyTable(getServerTable());
		getShot().getBoard().setEnemyButton(getServerButton());
	}
	
	// M�todo para disconectar do Socket
	public void disconnect(){
		try {
			socketConnection.close();
			socketServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public String getPort() {
		return port;
	}

	public JTable getServerTable() {
		return serverTable;
	}

	public void setServerTable(JTable serverTable) {
		this.serverTable = serverTable;
	}

	public JTable getServerScore() {
		return serverScore;
	}

	public void setServerScore(JTable serverScore) {
		this.serverScore = serverScore;
	}

	public JButton getServerButton() {
		return serverButton;
	}

	public void setServerButton(JButton serverButton) {
		this.serverButton = serverButton;
	}

	public JTable getServerTableAnswer() {
		return serverTableAnswer;
	}

	public void setServerTableAnswer(JTable serverTableAnswer) {
		this.serverTableAnswer = serverTableAnswer;
	}

	public Shot getShotAnswer() {
		return shotAnswer;
	}

	public void setShotAnswer(Shot shotAnswer) {
		this.shotAnswer = shotAnswer;
	}

	public boolean isHitAnswer() {
		return hitAnswer;
	}

	public void setHitAnswer(boolean hitAnswer) {
		this.hitAnswer = hitAnswer;
	}

	public int getCountShot() {
		return countShot;
	}

	public void setCountShot(int countShot) {
		this.countShot = countShot;
	}

	public int getCountHit() {
		return countHit;
	}

	public void setCountHit(int countHit) {
		this.countHit = countHit;
	}

	public int getCountPoints() {
		return countPoints;
	}

	public void setCountPoints(int countPoints) {
		this.countPoints = countPoints;
	}

	public void setShot(Shot shot) {
		this.shot = shot;
	}
	
	public Shot getShot() {
		return shot;
	}
}
