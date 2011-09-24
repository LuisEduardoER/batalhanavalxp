package src.windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;

public class BoardWindow extends JFrame{
	
	private static final long serialVersionUID = 42;
	public Container bwFrame; 
	//public DefaultTableCellRenderer io_rd_renderer;
	public JPanel panel;
	public JTable table;
	public JTable tableEnemy;
	public JTable tableScore;
	public JScrollPane pane;
	public JScrollPane paneEnemy;
	public JScrollPane paneScore;
	public String type; // Determinar se � Cliente ou Servidor
	public JTextField tf1c = new JTextField();  
	public JTextField tf1r = new JTextField();  
	public JTextField tf2c = new JTextField();  
	public JTextField tf2r = new JTextField();  
	public JTextField tf3c = new JTextField();  
	public JTextField tf3r = new JTextField();  
	public JTextField tf4c = new JTextField();  
	public JTextField tf4r = new JTextField(); 
	public Object[] message;
	public Object[] errorMessage;
	public JLabel player1 = new JLabel("Seu Tabuleiro  --->");
	public JLabel player2 = new JLabel("Respostas ------>");
	public JButton btShot;
	
	public BoardWindow(String type){
		
		// Determina nome Janela
		super("Batalha Naval XP");
		
		// Cria um Container
		bwFrame = this.getContentPane();
		
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
        pane = new JScrollPane(table);
        pane.getViewport().setPreferredSize(table.getPreferredSize());
        
        paneEnemy = new JScrollPane(tableEnemy);
        paneEnemy.getViewport().setPreferredSize(tableEnemy.getPreferredSize());
        
        paneScore = new JScrollPane(tableScore);
        paneScore.getViewport().setPreferredSize(tableScore.getPreferredSize());
        
        btShot = new JButton("Iniciar Disparo");
        btShot.setSize(50, 50);
        btShot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	int check = 1;
            	int error = 0;
            	while (check != 0){
            		tf1c.setText(null);
        			tf1r.setText(null);
        			
        			message = new Object[] {  
        					"Pe�a 1","Coluna", tf1c, "Linha", tf1r,};
        		
        			errorMessage = new Object[] {"Verifique os coordenadas inseridos, pois existem coordenadas inv�lidos.\n" +
													"Coordenadas v�lidas s�o de 1 a 10. Letras n�o s�o v�lidas\n"};
        				
        			// Solicita dados aos usu�rio
        			check = JOptionPane.showConfirmDialog(null, message, "Inserir coordenada do Tiro", JOptionPane.OK_OPTION);
        			if (check == 0){
        				if (validateShot(tf1r.getText(), tf1c.getText())){
        					// fazer envio de tiro
        					System.out.println("FECHOU TODAS");
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

        // Configura detalhes do Frame
        // 1. Encerrar Applica��o ao Fechar
        // 2. Setar Frame como Vis�vel
        // 3. Setar as dimens�es do Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 1
        this.setVisible(true); // 2
        this.setSize(1024, 460); // 3		

		// Guardar na classe que tipo de conex�o est� sendo feita (Cliente ou Servidor)
		setType(type.toUpperCase());
		
		// Monta o Jogo
		//mountBoard(table);
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
	
	// M�todo que monta o Tabuleiro e valida os valores inseridos
	public void mountBoard(JTable table){
		
		int check = 1;
		int error = 0;
		
		// Mensagem de erro com os valores
		errorMessage = new Object[]{"Verifique os coordenadas inseridos, pois existem coordenadas inv�lidos.\n" +
									"Coordenadas v�lidas s�o de 1 a 10. Letras n�o s�o v�lidas\n" +
									"Coordenadas devem ser valores seq��nciais (ou para Direita ou para Esquerda).\n" +
									"N�o � permitido usar Coordenadas repetidas."};
		
		
		// Insere 2 pe�as de 2 posi��es
		message = new Object[] {  
		"Pe�a 1","Coluna", tf1c, "Linha", tf1r,"Pe�a 2", "Coluna", tf2c, "Linha", tf2r};
		for (int i = 0; i <= 1; i++) {  
			while(check != 0){
				
				// Limpa poss�veis rastros nos inputs 
				tf1c.setText(null);
				tf1r.setText(null);
				tf2c.setText(null);
				tf2r.setText(null);
				
				// Solicita dados aos usu�rio
				check = JOptionPane.showConfirmDialog(null, message, "Inserir pe�a de 2 posi��es", JOptionPane.OK_OPTION);
				
				// Verifica qual � o op��o escolhida
				if (check == 0){
					
					// Verifica se tem Zeros, Letras e "". SE N�O TEM insere valores na tabela
					// SE TEM exibe mensagem de erro e solicita novamente ao usu�rio
					if (!hasZeros(tf1r.getText(), tf1c.getText()) && !hasZeros(tf2r.getText(), tf2c.getText()) &&  
						!hasLetters(tf1r.getText(), tf1c.getText()) && !hasLetters(tf2r.getText(), tf2c.getText()) &&
						!hasNull(tf1r.getText(), tf1c.getText()) && !hasNull(tf2r.getText(), tf2c.getText()) &&
						isCoordenatesOk(tf1r.getText(), tf1c.getText(), tf2r.getText(), tf2c.getText()) &&
						isPositionOk(table, tf1r.getText(), tf1c.getText(), tf2r.getText(), tf2c.getText())){
						
						// Verificar se as posi��es a serem inseridas est�o livres
						
						// Insere na tabela
						table.setValueAt("<>", Integer.parseInt(tf1r.getText()), Integer.parseInt(tf1c.getText()));
						table.setValueAt("<>", Integer.parseInt(tf2r.getText()), Integer.parseInt(tf2c.getText()));
					}else {
						
						// Mensagem de erro
						error = JOptionPane.showConfirmDialog(null, errorMessage, "Erro ao inserir Coordenadas", JOptionPane.CANCEL_OPTION);
						check = 1;
					}
				}
			}
			check = 1;
		}
		
		// Insere 2 pe�as de 3 posi��es
		message = new Object[] {  
		"Pe�a 1", "Coluna", tf1c, "Linha", tf1r, "Pe�a 2", "Coluna", tf2c, "Linha", tf2r, 
		"Pe�a 3", "Coluna", tf3c, "Linha", tf3r};
		
		for (int i = 0; i <= 1; i++) {  
			while(check != 0){
				
				// Limpa poss�veis rastros nos inputs
				tf1c.setText(null);
				tf1r.setText(null);
				tf2c.setText(null);
				tf2r.setText(null);
				tf3c.setText(null);
				tf3r.setText(null);
				
				// Solicita dados aos usu�rio
				check = JOptionPane.showConfirmDialog(null, message, "Incluir pe�a de 3 posi��es", JOptionPane.OK_OPTION);
				
				// Verifica qual � o op��o escolhida
				if (check == 0){
					
					// Verifica se tem Zeros, Letras e "". SE N�O TEM insere valores na tabela
					// SE TEM exibe mensagem de erro e solicita novamente ao usu�rio
					if (!hasZeros(tf1r.getText(), tf1c.getText()) && !hasZeros(tf2r.getText(), tf2c.getText()) &&
							!hasZeros(tf3r.getText(), tf3c.getText()) &&
						!hasLetters(tf1r.getText(), tf1c.getText()) && !hasLetters(tf2r.getText(), tf2c.getText()) &&
							!hasLetters(tf3r.getText(), tf3c.getText()) &&
						!hasNull(tf1r.getText(), tf1c.getText()) && !hasNull(tf2r.getText(), tf2c.getText()) &&
							!hasNull(tf3r.getText(), tf3c.getText()) && 
						isCoordenatesOk(tf1r.getText(), tf1c.getText(), tf2r.getText(), tf2c.getText()) && 
							isCoordenatesOk(tf2r.getText(), tf2c.getText(), tf3r.getText(), tf3c.getText()) &&
						isPositionOk(table, tf1r.getText(), tf1c.getText(), tf2r.getText(), tf2c.getText()) &&
							isPositionOk(table, tf1r.getText(), tf1c.getText(), tf3r.getText(), tf3c.getText()) &&
							isPositionOk(table, tf2r.getText(), tf2c.getText(), tf3r.getText(), tf3c.getText())){
						
						
						// Insere na tabela
						table.setValueAt("<>", Integer.parseInt(tf1r.getText()), Integer.parseInt(tf1c.getText()));
						table.setValueAt("<>", Integer.parseInt(tf2r.getText()), Integer.parseInt(tf2c.getText()));
						table.setValueAt("<>", Integer.parseInt(tf3r.getText()), Integer.parseInt(tf3c.getText()));
					}else {
						
						// Mensagem de erro
						error = JOptionPane.showConfirmDialog(null, errorMessage, "Erro ao inserir Coordenadas", JOptionPane.CANCEL_OPTION);
						check = 1;
					}
				}
			}
			check = 1;
		}
		
		// Insere 1 pe�as de 3 posi��es
		message = new Object[] {  
		"Pe�a 1", "Coluna", tf1c, "Linha", tf1r, "Pe�a 2", "Coluna", tf2c, "Linha", tf2r, 
		"Pe�a 3", "Coluna", tf3c, "Linha", tf3r, "Pe�a 4", "Coluna", tf4c, "Linha", tf4r};
		  
		while(check != 0){
			
			// Limpa poss�veis rastros nos inputs
			tf1c.setText(null);
			tf1r.setText(null);
			tf2c.setText(null);
			tf2r.setText(null);
			tf3c.setText(null);
			tf3r.setText(null);
			tf4c.setText(null);
			tf4r.setText(null);
			
			// Solicita dados aos usu�rio
			check = JOptionPane.showConfirmDialog(null, message, "Incluir pe�a de 4 posi��es", JOptionPane.OK_OPTION);

			// Verifica qual � o op��o escolhida
			if (check == 0){
				
				// Verifica se tem Zeros, Letras e "". SE N�O TEM insere valores na tabela
				// SE TEM exibe mensagem de erro e solicita novamente ao usu�rio
				if (!hasZeros(tf1r.getText(), tf1c.getText()) && !hasZeros(tf2r.getText(), tf2c.getText()) &&
						!hasZeros(tf3r.getText(), tf3c.getText()) && !hasZeros(tf4r.getText(), tf4c.getText()) &&
					!hasLetters(tf1r.getText(), tf1c.getText()) && !hasLetters(tf2r.getText(), tf2c.getText()) &&
						!hasLetters(tf3r.getText(), tf3c.getText()) && !hasZeros(tf4r.getText(), tf4c.getText()) &&
					!hasNull(tf1r.getText(), tf1c.getText()) && !hasNull(tf2r.getText(), tf2c.getText()) &&
						!hasNull(tf3r.getText(), tf3c.getText()) && !hasZeros(tf4r.getText(), tf4c.getText()) &&
					isCoordenatesOk(tf1r.getText(), tf1c.getText(), tf2r.getText(), tf2c.getText()) && 
						isCoordenatesOk(tf2r.getText(), tf2c.getText(), tf3r.getText(), tf3c.getText()) &&
						isCoordenatesOk(tf3r.getText(), tf3c.getText(), tf4r.getText(), tf4c.getText()) &&
					isPositionOk(table, tf1r.getText(), tf1c.getText(), tf2r.getText(), tf2c.getText()) &&
						isPositionOk(table, tf1r.getText(), tf1c.getText(), tf3r.getText(), tf3c.getText()) &&
						isPositionOk(table, tf1r.getText(), tf1c.getText(), tf4r.getText(), tf4c.getText()) &&
						isPositionOk(table, tf2r.getText(), tf2c.getText(), tf3r.getText(), tf3c.getText()) &&
						isPositionOk(table, tf2r.getText(), tf2c.getText(), tf4r.getText(), tf4c.getText()) &&
						isPositionOk(table, tf3r.getText(), tf3c.getText(), tf4r.getText(), tf4c.getText())){
					
					
					// Insere na tabela
					table.setValueAt("<>", Integer.parseInt(tf1r.getText()), Integer.parseInt(tf1c.getText()));
					table.setValueAt("<>", Integer.parseInt(tf2r.getText()), Integer.parseInt(tf2c.getText()));
					table.setValueAt("<>", Integer.parseInt(tf3r.getText()), Integer.parseInt(tf3c.getText()));
					table.setValueAt("<>", Integer.parseInt(tf4r.getText()), Integer.parseInt(tf4c.getText()));
				}else {
					
					// Mensagem de erro
					error = JOptionPane.showConfirmDialog(null, errorMessage, "Erro ao inserir Coordenadas", JOptionPane.CANCEL_OPTION);
					check = 1;
				}
			}
		}
		check = 1;
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
	
	public boolean validateShot(String row, String column){
		
		boolean check = false;
		
		if (!hasZeros(row, column) && !hasLetters(row, column) && !hasNull(row, column)){
			
			check = true;
		}
		return check;
	}
}
