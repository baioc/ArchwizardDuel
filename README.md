# Archwizard Duel

[Projeto da disciplina INE5417](https://www.inf.ufsc.br/~ricardo.silva/INE5417e5608/) (Engenharia de Software I).
Consiste em um jogo multijogador distribuído desenvolvido em Java através de modelagem orientada a objetos em UML e utilizando a biblioteca [NetGamesNRT](http://www.labsoft.ufsc.br/~netgames/NetGamesNRT).

## Desenvolvimento

### [Tarefas](https://github.com/baioc/ArchwizardDuel/projects)

- [Geral](https://github.com/baioc/ArchwizardDuel/projects/4)
- [Infraestrutura](https://github.com/baioc/ArchwizardDuel/projects/1)
- [Interface](https://github.com/baioc/ArchwizardDuel/projects/2)
- [Linguagem](https://github.com/baioc/ArchwizardDuel/projects/3)

### Controle

[**TODO:** definir esquema de branches para desenvolvimento.](https://github.com/baioc/ArchwizardDuel/projects/4#card-25310033)

### Java

[**TODO:** padronizar formatação e nomenclatura no código.](https://github.com/baioc/ArchwizardDuel/projects/4#card-25310034)

## Jogabilidade

### Descrição

Em *Archwizard Duel*, cada jogador tem ao seu controle um mago com o qual deve provar seu valor ao vencer duelos contra seus inimigos.
O combate acontece em turnos, cada um consistindo na simulação de um intervalo de tempo em uma arena aberta.
As ações dos magos são programadas por código escrito pelos jogadores em uma linguagem específica.

### Referências

Jogos com ideias semelhantes:

- [**Gladiabots**](https://store.steampowered.com/app/871930/Gladiabots/) (2019)
- [7 Billion Humans](https://store.steampowered.com/app/792100/7_Billion_Humans/) (2018)
- [**Lightbot**](https://en.wikipedia.org/wiki/Lightbot) (2008)
- [**Robocode**](http://robowiki.net/wiki/Robocode) (2001) -> **BattleBots (JOGO PROIBIDO 2015-2)**
- [Crobots](http://crobots.deepthought.it/home.php) (1985)
- [Robot Odyssey](https://www.myabandonware.com/game/robot-odyssey-6g) (1984)
- [**Color Robot Battle**](https://programminggames.org/Color_Robot_Battle) (1981)
- [**RobotWar**](https://en.wikipedia.org/wiki/RobotWar) (1981)

## [Infraestrutura](https://github.com/baioc/ArchwizardDuel/projects/1)

### [NetGamesNRT](http://www.labsoft.ufsc.br/~netgames/NetGamesNRT)

- Jogo em turnos
  - Modelo servidor-cliente
- Multijogador
  - Sessões
  - Partidas
  - Jogadas

## [Interface](https://github.com/baioc/ArchwizardDuel/projects/2)

1. [LibGDX](https://github.com/libgdx/libgdx/wiki)
   - Mais bem documentado
   - [Renderização descontínua](https://github.com/libgdx/libgdx/wiki/Continuous-%26amp%3B-Non-Continuous-Rendering)
   - Mais potente
2. [FXGL](https://github.com/AlmasB/FXGL/wiki/FXGL-0.5.4)
   - Parece mais simples de usar

## [Linguagem](https://github.com/baioc/ArchwizardDuel/projects/3)

- Interpretada
  - [Bytecode Pattern](http://gameprogrammingpatterns.com/bytecode.html)
  - [Circular Evaluator](https://mitpress.mit.edu/sites/default/files/sicp/full-text/book/book-Z-H-26.html)
- Baixo/Alto nível?
  1. Assembly
  2. Forth
  3. C
  4. LISP
  5. Python
