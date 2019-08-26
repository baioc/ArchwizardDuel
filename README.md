# Archwizard Duel

[Projeto da disciplina INE5417](https://www.inf.ufsc.br/~ricardo.silva/INE5417e5608/) (Engenharia de Software I).
Consiste em um jogo multijogador distribuído desenvolvido em Java através de modelagem orientada a objetos em UML e utilizando a biblioteca [NetGamesNRT](http://www.labsoft.ufsc.br/~netgames/NetGamesNRT).

## Jogabilidade

### Descrição

Em ***Archwizard Duel***, cada jogador tem ao seu controle um mago com o qual deve provar seu valor ao vencer duelos contra seus inimigos.
O combate acontece **em turnos**, cada um consistindo na simulação de um intervalo de tempo em uma arena aberta.
As ações dos magos são **programadas por código** escrito pelos jogadores em uma linguagem específica.

### Referências

Jogos com ideias semelhantes:

- [Gladiabots](https://store.steampowered.com/app/871930/Gladiabots/) (2019)
  - Linguagem: visual
- [Robocode](http://robowiki.net/wiki/Robocode) (2001) -> **BattleBots (JOGO PROIBIDO 2015-2)**
  - Linguagem: estilo Java
- [Crobots](http://crobots.deepthought.it/home.php) (1985)
  - Linguagem: estilo C
- [RobotWar](https://en.wikipedia.org/wiki/RobotWar) (1981)
  - Linguagem: estilo BASIC
- [Color Robot Battle](https://programminggames.org/Color_Robot_Battle) (1981)
  - Linguagem: zoada

## Desenvolvimento

### [Tarefas](https://github.com/baioc/ArchwizardDuel/projects)

- [Geral](https://github.com/baioc/ArchwizardDuel/projects/4)
- [Infraestrutura](https://github.com/baioc/ArchwizardDuel/projects/1)
- [Interface](https://github.com/baioc/ArchwizardDuel/projects/2)
- [Linguagem](https://github.com/baioc/ArchwizardDuel/projects/3)

### Controle de Versão

Toda a nova feature inserida no código deve ser justificada em uma **issue**.
Então, deve-se criar - **por padrão, a partir da branch dev** - uma branch dedicada a essa feature se já não houver uma.

Para nomear sua branch, use um nome curto que represente o que está sendo feito, lembrando de colocar o número da issue.
`hotfix/3/organize` seria um exemplo de uma branch de tipo *hotfix* (*build*, *feature*, *test*, *bug*, *documentation* são outros exemplos de categoria), que está relacionada à *issue #3* e fará algum tipo de organização (para mais detalhes, bastaria olhar a issue #3).

Depois de finalizado, basta colocar algum integrante como *assignee* na issue e criar um **pull request** do GitHub.
O outro integrante irá então fazer o merge da sua branch e fechar a issue relacionada.

### Padrão de Código

- Pelo amor de deus, código se escreve **em inglês**
  - Português só onde precisarmos interagir com código tosco.
- **Classes** em `PascalCase`
  - Se houver um acrônimo, apenas a primeira letra é maiuscula, eg. `HttpClient`
- **Variáveis** e **métodos** em `camelCase`
- **Constantes** em `SCREAMING_SNAKE_CASE`
- **Classes herdadas** tem o nome da classe pai como sufixo: `SpecialExpression` herda de `Expression`
- **Interfaces** são nomeadas pelo comportamento que as implementações devem ter: `Comparable`, `Serializable`, etc.
- **Modificadores** de atributos na seguinte ordem:
  - Acesso (`private`, `public` ou `protected`);
    - Por padrão, usar `private` para atributos.
  - `static` ou `abstract` ou nenhum deles;
  - `final` ou não;
  - `synchronized` ou não.
- **Abre chaves** na mesma linha de:
  - `if`, `for`, `while`
    - Abre e fecha chaves é opcional quando o corpo dessas expressões só tiver um ponto e vírgula;
  - **funções**;
    - Se a linha tiver de ser quebrada, aí pode variar dependendo de como foram formatados os argumentos, parâmetros, exceptions, etc.
  - `switch`, classes, etc.
- **Fecha chaves** (`}`) na mesma linha do `else` em if-elses.
- Usar um **espaço** para separar parenteses, exceto em declarações e em chamadas de métodos.
  - `if (a < b + c) {` em vez de `if( a<b+c ){`
- **Comentários** com `//`. Utilizar `/* */` apenas se ele estiver cercado por código na mesma linha ou se o objetivo for remover código temporariamente no desenvolvimento (para debug, por exemplo).
  - `/** */` reservado para JavaDocs.
- Tamanho de **tabulação**: 4. Usar tabs.
- Todo arquivo deve terminar com uma linha vazia.
- É bom evitar passar de 80-90 caracteres por linha.

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
   - [Renderização descontínua](https://github.com/libgdx/libgdx/wiki/Continuous-%26amp%3B-Non-Continuous-Rendering)
   - Mais potente
2. Swing
   - Mais simples

## [Linguagem](https://github.com/baioc/ArchwizardDuel/projects/3)

- Interpretada
  - [Bytecode Pattern](http://gameprogrammingpatterns.com/bytecode.html)
    - aka Switch-Case.
  - [Circular Evaluator](https://mitpress.mit.edu/sites/default/files/sicp/full-text/book/book-Z-H-26.html)
    - Reduziria toda a expressão a uma sequência de primitivas (pode ser um bytecode próprio ou objetos-função).
- Baixo/Alto nível
  1. Assembly
  2. Forth
  3. C
  4. LISP
  5. Python
