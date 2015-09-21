

# Gerando o bnxp.jar #

  1. Para gerar o arquivo .jar, **atualize o seu repositório** com o código mais recente, veja como em [Mercurial](Mercurial.md);
  1. No Eclipse clique no **nome do projeto** que deseja gerar o .jar;
  1. Clique em **File => Export**;
  1. Selecione a opção**Java => JAR file** e clique em **Next**;
  1. Tela 1/3;
    1. MAC\_ONLY Cuidado para desmarcar os arquivos .DS\_Store da seleção de todas as pastas apresentadas;
    1. Selecione o local onde quer salva o bnpx-X.X.jar;
    1. Certifique-se que apenas as opções **Export generated class files and resources** e **Compress the contents of the JAR file**;
    1. Clique em **Next**;
  1. Tela 2/3;
    1. Certifique-se que apenas as opções  **Export class files with compile errors** e **Export class files with compile warnings**;
    1. Clique em **Next**;
  1. Tela 3/3
    1. Selecione a opção **Use existing manifest from workspace**;
    1. Selecione o arquivo **/batalhanavalxp/src/META-INF/MANIFEST.MF**;
    1. Clique em **Finish**.

Só correr para o abraço!

Obs.: Pode dar uma mensagem por causa dos warnings do código, se isso acontecer procure remover os warnings do código, eu sempre faço isso antes de commitar o trabalho, é uma boa prática.