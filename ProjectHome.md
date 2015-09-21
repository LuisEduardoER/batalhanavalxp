Batalha Naval XP é um jogo, com fins acadêmicos, feito na linguagem JAVA e utilizando Sockets.

# Batalha Naval #

Batalha naval é um jogo de tabuleiro de dois jogadores, no qual os jogadores têm de adivinhar em que quadrados estão os navios do oponente. O jogo é jogado em duas grelhas para cada jogador — uma que representa a disposição dos barcos do jogador, e outra que representa a do oponente. As grelhas são tipicamente quadradas, estando identificadas por números.

Antes do início do jogo, cada jogador coloca os seus navios nas coordenadas desejadas, alinhados horizontalmente ou verticalmente. O número de navios permitidos é igual para ambos os jogadores e os navios não podem se sobrepor.
Após os navios terem sido posicionados o jogo continua numa série de turnos, em cada turno o jogador diz uma coordenada, se houver um navio nessa coordenada, é colocada uma marca X, senão houver é colocada uma marca O.

Os tipos de peças são: uma de Cinco posições, uma de Quatro posições, uma de Três posições e uma de Duas posições. A grelha tem tamanho 10x10.

Para jogar existem duas formas, ou criar um Servidor ou ser um Cliente. Ao iniciar o jogo, exibem-se essas duas opções. Escolhendo a primeira opção, você será redirecionado para uma tela onde você deverá inserir o endereço Ip de sua máquina e a Porta onde você deseja. Após clica-se em “Iniciar”, onde você ficará em espera até outro jogador se conectar ao seu servidor. Escolhendo a segunda opção, a tela exibida é muito parecida com a primeira opção, porém com a diferença que você não vai criar um Servidor e sim Acessar um, ou seja, será necessário saber o Endereço Ip e a Porta válidos a fim de iniciar a jogo.

Conectados os jogadores, inicia-se o jogo. Ambos os jogadores serão redirecionados para uma janela onde será solicitado a eles, que insiram as posições onde querem posicionar suas peças (Coluna e Linha iniciais, e o posicionamento Horizontal ou Vertical), serão ao todo Cinco peças.

Após ambos os jogadores posicionarem suas peças, serão redirecionados para a tela com dois tabuleiros. O jogador Servidor inicia a partida, é solicitado a ela entrar com o número de uma coluna e de uma linha. Enviado o “tiro” é solicitado ao jogador Cliente o mesmo, entrar com o número de uma coluna e de uma linha, e assim até um dos jogadores estiver com todas as suas peças destruídas, sendo ele o perdedor.