# Especificação de Requisitos

## Projeto Archwizard Duel

### Especificação de Requisitos de Software

| Versão | Autores | Data | Ação |
| :---: | :--- | :--- | :--- |
| 0.1 | Alek Frohlich, Gabriel B. Sant'Anna e Mateus Favarin | 10/09/2019 | Eliciação |

## Introdução

Em **Archwizard Duel**, cada jogador tem ao seu controle um mago com o qual deve provar seu valor ao vencer **duelos** contra seus oponentes.
O combate acontece **em turnos**, cada um consistindo na simulação de um intervalo de tempo em uma arena.
As ações dos magos são **programadas por código** escrito pelos jogadores em uma linguagem específica.

### Objetivos

Desenvolver um **jogo *Player vs Player*** (PVP) utilizando a ferramenta de jogos distribuídos **NetGames**.
Este será entregue como [trabalho da disciplina **INE5417**](https://www.inf.ufsc.br/~ricardo.silva/INE5417e5608/) (Engenharia de Software I).

### Referências

Jogos com ideias semelhantes e seus respectivos estilos de controles programáticos:

- [Gladiabots](https://store.steampowered.com/app/871930/Gladiabots/) (2019) - Programação visual através de fluxogramas.
- [Robocode](http://robowiki.net/wiki/Robocode) (2001) - Programação em código, semelhante a Java.
- [Crobots](http://crobots.deepthought.it/home.php) (1985) - Programação em código, semelhante a C.
- [RobotWar](https://en.wikipedia.org/wiki/RobotWar) (1981) - Programação em código, semelhante a BASIC.
- [Color Robot Battle](https://programminggames.org/Color_Robot_Battle) (1981) - Programação em código, semelhante a Assembly.

## Visão Geral

### Arquitetura do Programa

Programa **orientado a objetos**, **distribuído** e **multiusuário** (dois jogadores).

### Premissas de Desenvolvimento

- A implementação deverá ser na linguagem Java.
  - Deve ser compatível com o [Java Runtime Environment (JRE) versão 8](https://java.com/en/download/).
- Deve utilizar o framework [NetGamesNRT](http://www.labsoft.ufsc.br/~netgames/NetGamesNRT/).
- Deverá ser entregue a modelagem do software em UML 2 produzida com a ferramenta [Visual Paradigm](https://www.visual-paradigm.com/) (*Community Edition*).
- O programa deve apresentar uma interface gráfica.

## Requisitos de Software

### Requisitos Funcionais

- **Criar sessão:**
  O programa deve fornecer em sua interface a opção de estabelecer uma sessão de jogo para que outros usuários possam se conectar.

- **Conectar à sessão:**
  O programa deverá estabelecer a conexão entre os dois jogadores através do servidor NetGames.
  Cada jogador deve se identificar com um nome para seu personagem.

- **Iniciar partida:**
  O programa deve conter um botão que inicia a partida (caso já não exista alguma em andamento).

- **Entrada de código:**
  O programa deve disponibilizar uma caixa de texto onde o jogador poderá digitar o código que servirá como esquema de controle do seu personagem em uma partida.

- **Enviar jogada:**
  O programa deverá conter um botão que finaliza a jogada atual, enviando o código a um interpretador que realiza o controle do personagem na partida em andamento.

- **Receber jogada:**
  O programa deverá mostrar as jogadas realizadas pelo jogador remoto, atualizando o cliente do usuário local.

- **Desistir da partida:**
  O programa deve conter um botão para abandonar uma partida em andamento.

- **Desconectar da sessão:**
  O programa poderá desligar a conexão entre os jogadores caso estes não desejem iniciar novas partidas.

### Requisitos Não Funcionais

- **Decidir quem inicia a partida:**
  O programa deverá, ao início de uma partida, definir qual jogador terá o primeiro turno.

- **Controles programáticos:**
  O programa deverá conter um interpetador de código que, dada uma sequência de instruções válidas na linguagem dos feitiços, execute jogadas que irão afetar seu oponente.
  A linguagem jogo tomará uma forma semelhante à de [LISP](https://en.wikipedia.org/wiki/LISP); devendo prover primitivas para controlar o personagem (movimento, feitiços) e definir o fluxo de execução de suas ações (condicionais, procedimentos).

- **Visualização do estado da partida:**
  Os atributos dos personagens visíveis ao jogador devem ser atualizados pelo interpretador em decorrência das jogadas efetuadas; em seguida deve ser feita a verificação de fim de jogo e então a preparação para o turno do jogador seguinte.
