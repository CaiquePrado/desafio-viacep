# Desafio ViaCEP

Este é um projeto que utiliza Rest-Assured e JUnit5 para testar a API ViaCEP. Ele é destinado para a criação de uma suíte de testes automatizados para validar os endpoints da API.

## Documentação de Testes

Aqui estão alguns links para a documentação de teste do projeto:

- Cenários de Teste: https://docs.google.com/document/d/15nMZZvUnj5t1zbOlHINSHMvs-IfOLP3CSM3PzA0SQok/edit?usp=sharing
- Planejamento de Testes: https://docs.google.com/document/d/1XqmYyhbd-2yo6LLHg0ee7cuohOkkAlUZxofXNYtylJM/edit?usp=sharing
- Tabelas de Decisão: https://docs.google.com/spreadsheets/d/1fwqJ1LNm20Fgfo6l-bGZI7a89619hAiULIhKR3hL8Gk/edit?usp=sharing

## Pré-requisitos

Certifique-se de ter os seguintes pré-requisitos instalados antes de começar:

- **Java 17**: Certifique-se de ter o JDK 17 instalado em sua máquina. Você pode baixá-lo e instalá-lo a partir do site oficial da Oracle ou de outras fontes confiáveis.

- **Gradle**: Este projeto utiliza o Gradle como sistema de build. Certifique-se de ter o Gradle instalado em sua máquina. Você pode instalá-lo seguindo as instruções disponíveis no site oficial do Gradle.

- **IDE de sua preferência**: Recomenda-se o uso de uma IDE para desenvolvimento Java, como IntelliJ IDEA, VsCode, Eclipse ou Spring Tools Suite (STS).

- **Allure**: Este projeto utiliza Allure para gerar relatórios de testes. Certifique-se de ter o Allure instalado em sua máquina. Siga as instruções abaixo para instalar o Allure:

  1. Abra o PowerShell como administrador e execute o seguinte comando para alterar a política de execução:
     ```
     Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
     ```
  2. Pressione `s` para confirmar a alteração.
  3. Em seguida, instale o Scoop (um instalador de linha de comando) com o seguinte comando:
     ```
     irm get.scoop.sh | iex
     ```
  4. Por fim, instale o Allure com o comando:
     ```
     scoop install allure
     ```

## Executando os testes

Para executar os testes, você pode usar o comando `gradle test` no terminal. Os resultados dos testes serão salvos no diretório `allure-results`. Este diretório contém vários arquivos JSON que representam os resultados dos testes.

## Visualizando o relatório de testes

Depois de executar os testes, uma pasta chamada `allure-results` será gerada. Dentro desta pasta, você encontrará vários arquivos JSON. Localize um arquivo que termine com `-result.json`.

Para visualizar o relatório de testes, você pode usar o comando `allure serve` seguido do caminho completo para o diretório `allure-results`. Por exemplo:

```bash
allure serve C:\caminho\para\seu\projeto\allure-results

