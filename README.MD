# Jogo da Forca

> Uma implementação simples do jogo da forca utilizando 
Sockets TCP e multi-threads para comunicação com cada jogador

Ao iniciar o arquivo `server.java` ele solicitará qual porta desejará 
que o servidor escute para que os jogadores possam se conectarem.

Assim que selecionada a porta, o servidor irá solicitar a palavra a ser 
descoberta, para em seguida abrir o servidor para conexão e iniciar o jogo. 

O jogo possui um tempo limite para que novos jogadores possam se conectar e 
dentro do arquivo localizado em `src/server/resources/users.txt` existe 
a relação de usuários que podem se conectar à aplicação.

Uma vez conectado, um outro cliente não poderá utilizar as mesmas 
credenciais em outro local, portanto fazendo com que cada cliente 
seja representado por um usuário único.

Finalizado o tempo de execução o jogo se inicia, na qual cada jogador 
pode enviar seus palpites sem que os outros saibam em que fase está. 

O jogo finaliza quando um jogador vence ou quando o tempo limite do jogo 
acaba.