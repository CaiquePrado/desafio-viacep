package dbserver.desafioviacep.desafioviacep;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import dbserver.desafioviacep.desafioviacep.config.BaseTest;
import dbserver.desafioviacep.desafioviacep.models.Endereco;
import dbserver.desafioviacep.desafioviacep.models.Menssagem;
import dbserver.desafioviacep.desafioviacep.stubs.EnderecoStubs;
import dbserver.desafioviacep.desafioviacep.stubs.MenssagensStubs;

public class ConsultaCepPorEnderecoTest extends BaseTest {

  private Menssagem menssagemDeErro;
  private Endereco enderecoEsperado;

  @DisplayName("CT002.001 - Consulta de CEP por endereço com sucesso")
  @Test
  public void DeveRetornarStatusCode200ECEPCorrespondenteQuandoEnderecoValido() {
    Endereco enderecoEsperado = EnderecoStubs.enderecoValidoEsperadoStub();

    given()
        .pathParam("uf", enderecoEsperado.getUf())
        .pathParam("cidade", enderecoEsperado.getLocalidade())
        .pathParam("logradouro", enderecoEsperado.getLogradouro())
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("$", hasSize(5))
        .body("cep", hasItems(enderecoEsperado.getCep()))
        .body("logradouro", hasItems(enderecoEsperado.getLogradouro()))
        .body("complemento", hasItems(enderecoEsperado.getComplemento()))
        .body("bairro", hasItems(enderecoEsperado.getBairro()))
        .body("localidade", hasItems(enderecoEsperado.getLocalidade()))
        .body("uf", hasItems(enderecoEsperado.getUf()))
        .body("ibge", hasItems(enderecoEsperado.getIbge()))
        .body("gia", hasItems(enderecoEsperado.getGia()))
        .body("ddd", hasItems(enderecoEsperado.getDdd()))
        .body("siafi", hasItems(enderecoEsperado.getSiafi()));
  }

  @DisplayName("CT002.002 - Consulta de CEP por endereço com UF, cidade e logradouro inválidos")
  @Test
  public void DeveRetornarStatusCode400QuandoUfCidadeLogradouroInvalidos() {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .pathParam("uf", "X X")
        .pathParam("cidade", "Terra do nunca")
        .pathParam("logradouro", "rua imaginaria")
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }

  @DisplayName("CT002.003 - Consulta de CEP por endereço com UF vazia")
  @Test
  public void DeveRetornarStatusCode400QuandoUfVazia() {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .pathParam("uf", "")
        .pathParam("cidade", "São Paulo")
        .pathParam("logradouro", "Praça da Sé")
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }

  @DisplayName("CT002.004 - Consulta de CEP por endereço com cidade vazia")
  @Test
  public void DeveRetornarStatusCode400QuandoCidadeVazia() {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .pathParam("uf", "SP")
        .pathParam("cidade", "")
        .pathParam("logradouro", "Praça da Sé")
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }

  @DisplayName("CT002.005 - Consulta de CEP por endereço com logradouro vazio")
  @Test
  public void DeveRetornarStatusCode400QuandoLogradouroVazio() {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .pathParam("uf", "SP")
        .pathParam("cidade", "São Paulo")
        .pathParam("logradouro", "")
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }

  @DisplayName("CT002.006 - Consulta de CEP por endereço com UF inválida")
  @ParameterizedTest
  @CsvSource({
      "R S, 8orto Alegre, Domingos",
      "$P, sp, Paulista",
      "B4, $alvador, Santana"
  })
  public void DeveRetornarStatusCode400QuandoUFInvalida(String uf, String cidade, String logradouro) {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .pathParam("uf", uf)
        .pathParam("cidade", cidade)
        .pathParam("logradouro", logradouro)
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }

  @DisplayName("CT002.007 - Consulta de CEP por endereço com cidade inválida")
  @ParameterizedTest
  @CsvSource({
      "RS, 8, Domingos",
      "SP, sp, Paulista",
      "BA, $alvador, Santana"
  })
  public void DeveRetornarStatusCode400QuandoCidadeInvalida(String uf, String cidade, String logradouro) {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .pathParam("uf", uf)
        .pathParam("cidade", cidade)
        .pathParam("logradouro", logradouro)
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }

  @DisplayName("CT002.008 - Consulta de CEP por endereço com logradouro inválido")
  @ParameterizedTest
  @CsvSource({
      "RS, Porto Alegre, 1",
      "SP, Sao Paulo, _Paulista",
      "BA, Salvador, $antana"
  })
  public void DeveRetornarStatusCode400QuandoLogradouroInvalido(String uf, String cidade, String logradouro) {

    menssagemDeErro = MenssagensStubs.menssagemDeErroStub();

    given()
        .pathParam("uf", uf)
        .pathParam("cidade", cidade)
        .pathParam("logradouro", logradouro)
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(containsString(menssagemDeErro.getStatus()))
        .body(containsString(menssagemDeErro.getTitle()))
        .body(containsString(menssagemDeErro.getDescricao()));
  }

  @DisplayName("CT002.009 - Consulta de CEP com cidade válida, porém inexistente no Estado")
  @Test
  public void DeveRetornarStatusCode200EArrayVazioQuandoCidadeValidaPoremInexistenteNoEstado() {

    enderecoEsperado = EnderecoStubs.cidadeExistenteInvalidaStub();

    given()
        .pathParam("uf", enderecoEsperado.getUf())
        .pathParam("cidade", enderecoEsperado.getLocalidade())
        .pathParam("logradouro", enderecoEsperado.getLogradouro())
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(matchesJsonSchemaInClasspath("ArrayVazioSchema.json"))
        .body(equalTo("[]"))
        .body("$", hasSize(0));
  }

  @DisplayName("CT002.010 - Consulta de CEP com logradouro válido, porém inexistente no Estado")
  @Test
  public void DeveRetornarStatusCode200EArrayVazioQuandoLogradouroValidaPoremInexistenteNaCidade() {

    enderecoEsperado = EnderecoStubs.logradouroExistenteInvalidaStub();

    given()
        .pathParam("uf", enderecoEsperado.getUf())
        .pathParam("cidade", enderecoEsperado.getLocalidade())
        .pathParam("logradouro", enderecoEsperado.getLogradouro())
        .when()
        .get(ContratoTest.BASE_URL_ENDERECO)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(equalTo("[]"))
        .body("$", hasSize(0));
  }
}
