package src.connections;

import java.io.*;
import java.net.*;
import src.main.*;
import javax.swing.*;

public class Client extends Thread implements Runnable{

	protected String ip;
	protected String port;
	protected Socket socketClient;
	public ObjectInputStream receiveObject;
	public ObjectOutputStream sendObject;
	protected Shot shot;
	protected Shot shotAnswer;
	protected boolean hitAnswer;
	protected int countShot;
	protected int countHit;
	protected int countPoints = 15;
	protected int[][] clientBoard;
	protected JTable clientTable;
	protected JTable clientTableAnswer;
	protected JTable clientScore;
	protected JButton clientButton;
	protected Object[] errorMessage;
	
	// Pelo M�todo construtor, conecta-se com o servidor e cria-se o socket
	public Client (String ip, String port){
		
		try {
			socketClient = new Socket(ip, Integer.parseInt(port));
			sendObject = new ObjectOutputStream(socketClient.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Threads que recebe Objeto e envia Objeto
	// Como ela funciona:
	// 1 - Recebe o Objeto e verifica se ele � um Tiro Resposta ou um Tiro. Se n�o for um tiro resposta: pega as Refer�ncias o Tabuleiro do Cliente;
	// verifica quantos tiros j� foram dados; verifica se acertou ou n�o um barco e marca no tabuleiro do Cliente; faz a contagem de Tiros, Acertos 
	// e quantos resta para acabar o jogo; acerta a pontua��o do Cliente; monta o Tiro Resposta para o Servidor, para que ele possa marcar em sua 
	// Grade de Resposta assim como acertar a sua pontua��o; envia o Tiro Resposta para o Servidor; e liberar o Bot�o para Iniciar Disparo do Cliente.
	// ---------------------------------------------------------------------------------------------------------------------------------------------
	// 2 - Se o Tiro for um Tiro Resposta, ent�o: verifica se o tiro acertou ou n�o para determinar qual marca��o usar; atualizar a Grade de
	// Pontua��o
	public void run(){
		if(socketClient.isConnected()){
			try {
				receiveObject = new ObjectInputStream(socketClient.getInputStream());
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
						configureServerBoard(getShot());
						
						// Acerta a contagem de Tiros
						setCountShot(getCountShot() + 1);
						
						// Verifica se o Tiro acerta ou n�o um barco
						setHitAnswer(getShot().receiveShot(getShot().getRow(), getShot().getColumn(), getShot().getBoard()));
						
						// Se Acertou incrementa a contagem
						if (isHitAnswer()){
							setCountHit(getCountHit() + 1);
							setCountPoints(getCountPoints() - 1);
						}
						
						
						// Acerta a pontua��o do Cliente
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
						
						int wipe = 0;
						
						if (getCountPoints() == 0){
							// Cria Mensagem de Derrota
		        			errorMessage = new Object[] {"Voc� PERDEU! Clique em Ok para voltar a Tela Inicial. Novamente, voc� PERDEU!\n\n H�!"};
							wipe = JOptionPane.showConfirmDialog(null, errorMessage, "VOC� PERDEU!", JOptionPane.CANCEL_OPTION);
						}else{
						
							// Libera Bot�o para o Cliente poder Iniciar o Diparo
							getShot().getBoard().getEnemyButton().setEnabled(true);
						}
					}else {
						
						// Verifica se o Tiro Resposta acertou ou n�o. E marca no tabuleiro com determinada Marca 
						if (getShot().isHit()){
							getClientTableAnswer().setValueAt("X", Integer.parseInt(getShot().getRow()), Integer.parseInt(getShot().getColumn())); // Acertou = X
						}else {
							getClientTableAnswer().setValueAt("O", Integer.parseInt(getShot().getRow()), Integer.parseInt(getShot().getColumn())); // Errou = O
						}
						
						// Atualiza Pontua��o do Cliente
						getClientScore().setValueAt(getShot().getCountShot(), 1, 1);
						getClientScore().setValueAt(getShot().getCountHit(), 1, 2);
						getClientScore().setValueAt(getShot().getCountPoints(), 1, 3);
						
						int wipe;
						
						if (getShot().getCountPoints() == 0){
							
							// Cria Mensagem de Derrota
		        			errorMessage = new Object[] {"Voc� GANHOU! Clique em Ok para voltar a Tela Inicial. GANHOU!\n\n"};
							wipe = JOptionPane.showConfirmDialog(null, errorMessage, "FEIT�! VOC� GANHOU!", JOptionPane.CANCEL_OPTION);
						}
						
					}
				}
			}
	}
	
	// Alimenta o Board do Servidor com as referencias do Cliente
	public void configureServerBoard(Shot shot){
		
		shot.getBoard().setEnemyScore(getClientScore());
		shot.getBoard().setEnemyTable(getClientTable());
		shot.getBoard().setEnemyButton(getClientButton());
	}
	
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int[][] getClientBoard() {
		return clientBoard;
	}

	public void setClientBoard(int[][] clientBoard) {
		this.clientBoard = clientBoard;
	}

	public JTable getClientTable() {
		return clientTable;
	}

	public void setClientTable(JTable clientTable) {
		this.clientTable = clientTable;
	}

	public JTable getClientScore() {
		return clientScore;
	}

	public void setClientScore(JTable clientScore) {
		this.clientScore = clientScore;
	}

	public JButton getClientButton() {
		return clientButton;
	}

	public void setClientButton(JButton clientButton) {
		this.clientButton = clientButton;
	}

	public JTable getClientTableAnswer() {
		return clientTableAnswer;
	}

	public void setClientTableAnswer(JTable clientTableAnswer) {
		this.clientTableAnswer = clientTableAnswer;
	}
	
	public Shot getShot() {
		return shot;
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

	public Shot getShotAnswer() {
		return shotAnswer;
	}

	public void setShotAnswer(Shot shotAnswer) {
		this.shotAnswer = shotAnswer;
	}
	
}
