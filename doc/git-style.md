# Controle de Versão

Toda a nova feature inserida no código deve ser justificada em uma **issue** e construída em uma **nova branch**.

A nova branch deve ser criada a partir de outra branch de desenvolvimento que tenha a infraestrutura sobre a qual será adicionada a nova feature.
Por padrão, utilizar a ***dev***.

Para nomear sua branch, use um nome curto que represente o que está sendo feito, lembrando de **referenciar o número da issue** no padrão `type/issue/name`.
Onde `type` é alguma categoria entre:

- *feature*
- *bug*
- *hotfix*
- *test*
- *build*
- *documentation*

`issue` é o número que o GitHub deu para a issue criada e `name` é alguma palavra-chave.

`hotfix/3/organize` seria um exemplo de uma branch de tipo *hotfix*, que está relacionada à *issue #3* e fará algum tipo de organização; para mais detalhes, bastaria olhar a issue #3.

Quando a feature estiver finalizada, basta colocar algum integrante como *assignee* na issue e criar um **pull request** do GitHub.
Esse outro integrante irá então fazer o merge da sua branch e fechar a issue relacionada.
