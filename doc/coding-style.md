# Padrão de Código

Pelo amor de deus, código se escreve **em inglês**.
Português só onde precisarmos interagir com código tosco (e o ideal seria fazer um wrapper pra isso).

Nomes de **variáveis** e **métodos** em `camelCase`.

Nomes de **constantes** em `SCREAMING_SNAKE_CASE`.

Nomes de **Classes** em `PascalCase`.
Se houver um acrônimo, apenas a primeira letra é maiuscula, eg. `HttpClient`.

**Classes herdadas** tem o nome da classe pai como sufixo: `SpecialExpression` herda de `Expression`.

**Interfaces** são nomeadas pelo comportamento que as implementações devem ter: `Comparable`, `Serializable`, etc.

**Modificadores** de atributos na seguinte ordem:

  1. Acesso (`private`, `public` ou `protected`).
   Por padrão, usar `private` para atributos.
  2. `static` ou `abstract` ou nenhum deles;
  3. `final` ou não;
  4. `synchronized` ou não.

**Abre chaves** na mesma linha de `if`, `for`, `while`, **funções** (se a linha tiver de ser quebrada, aí pode variar dependendo de como foram formatados os argumentos, parâmetros, exceptions, etc.), `switch`, classes, etc.

**Fecha chaves** na mesma linha do `else` em if-elses.

Usar um **espaço** para separar parenteses, exceto em declarações e em chamadas de métodos.
`if (a < b + c) {` em vez de `if( a<b+c ){`.

**Comentários** com `//`.
Utilizar `/* */` apenas se ele estiver cercado por código na mesma linha ou se o objetivo for remover código temporariamente no desenvolvimento (para debug, por exemplo).
`/** */` reservado para JavaDocs.

Tamanho de **tabulação**: 4. Usar tabs.

Todo arquivo deve terminar com uma linha vazia.
Também é bom evitar passar de 80-90 caracteres por linha.
