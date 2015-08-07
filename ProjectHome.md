Realiza marcação de ponto automaticamente ao ligar o computador e durante o tempo de utilização.

Este sistema também gera relatórios com o apontamento mensal (excel) e calculo de banco de horas.

A intenção real da construção deste projeto, além de criar um sistema que possibilite a marcação de ponto e calculo de banco de horas sem a necessidade de ficarmos criando planilhazinhas e realizando o apontamento todo final de periodo, é criar um projeto de estudo para algumas técnologias, como, jxl, xStream, etc.

A versão atual do projeto está utilizando:
Log4J,
XStream,
JExcel.

Situação atual do sistema:
+ Gera sistema de marcação de ponto;
+ Gera arquivo de configuração do sistema;
+ Realiza calculos de saldo de banco de horas e quantidade de horas trabalhadas para o mês, apontamentos e total;
+ Monta relatório de horas trabalhadas informando saldo do banco de cada mês, para um mês ou para todos os meses que estão dentro do arquivo XML gerado pelo sistema.

--> FALTA:
+ Formatar as quantidade de horas em números decimais informados dentro do relatório com duas casas decimais;
+ Incluir regras no relatório de horas em Excel informando o saldo total restante do usuário;
+ Incluir gráfico de horas trabalhadas para cada mês no caso de geração de relatório completo;
+ incluir regra para contagem de apenas meio periodo ou uma quantidade de horas determinadas (maior ou menor que o normal estabelecido na configuração), as datas que contiverem regras específicas assim devem ser incluidas no arquivo de configuração;
+ Módulo Gráfico em "Swing" (ou similares offLine) para administração, onde o usuário possa ajustar as configurações do sistema e incluir observações nos apontamentos realizados;